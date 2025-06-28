package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import com.doggys_tilt.rotp_t.entity.WormholeTeleporterEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ui.screen.standskin.StandSkinsScreen;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskCreateWormhole extends StandAction {
    public TuskCreateWormhole(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getNailCount() > 0 && !cap.get().hasWormholeWithArm() && !cap.get().hasWormhole() && cap.get().getNailWormhole() != null){
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.swinging = false;
        user.swing(Hand.MAIN_HAND);
        WormholeTeleporterEntity enter = new WormholeTeleporterEntity(world, user);
        WormholeTeleporterEntity exit = new WormholeTeleporterEntity(world, user);
        exit.setConnectedTeleporter(enter);
        enter.setConnectedTeleporter(exit);
        enter.moveTo(Vector3d.atCenterOf(user.blockPosition().above()));
        world.addFreshEntity(enter);
        user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            nailCapability.setHasWormhole(true);
            WormholeEntity wormholeToReplace = nailCapability.getNailWormhole();
            BlockPos endBlockPos = new BlockPos(wormholeToReplace.position());
            exit.moveTo(Vector3d.atBottomCenterOf(endBlockPos));
            world.addFreshEntity(exit);
            nailCapability.getNailWormhole().remove();
        });
    }
}
