package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.WormholeArmEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.doggys_tilt.rotp_t.util.TuskUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskCreateWormholeWithArm extends StandAction {
    public TuskCreateWormholeWithArm(StandAction.Builder builder) {
        super(builder.holdType());
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getNailCount() > 0 && !cap.get().hasWormhole()){
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    WormholeArmEntity wormholeArm;

    @Override
    public void startedHolding(World world, LivingEntity user, IStandPower power, ActionTarget target, boolean requirementsFulfilled) {
        if (requirementsFulfilled){
            wormholeArm = new WormholeArmEntity(user, world);
            wormholeArm.moveTo(user.position());
            world.addFreshEntity(wormholeArm);
            user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                nailCapability.setWormhole(true);
            });
        }
    }

    @Override
    public void onHoldTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (requirementsFulfilled){
            RayTraceResult result = TuskUtil.rayTrace(user, 12, entity -> false, 1);
            Vector3d movement = wormholeArm.position().vectorTo(result.getLocation());
            BlockPos pos = new BlockPos(result.getLocation());
            boolean isInAir = world.getBlockState(pos).isAir();
            wormholeArm.setDeltaMovement(movement.multiply(0.25, 1, 0.25).add(0, isInAir ? 0 : 0.7F, 0));
        }
    }
    @Override
    public void stoppedHolding(World world, LivingEntity user, IStandPower power, int ticksHeld, boolean willFire) {
        wormholeArm.setDeltaMovement(Vector3d.ZERO);
    }
}
