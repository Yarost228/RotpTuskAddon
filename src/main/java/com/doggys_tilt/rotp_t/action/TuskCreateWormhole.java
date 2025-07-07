package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import com.doggys_tilt.rotp_t.entity.WormholeTeleporterEntity;
import com.doggys_tilt.rotp_t.init.InitSounds;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class TuskCreateWormhole extends StandAction {
    public TuskCreateWormhole(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if ((cap.get().getNailCount() > 0 && !cap.get().hasWormholeWithArm() && !cap.get().hasWormhole() && cap.get().getWormhole() != null) && cap.get().getAct() >= 2) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Nullable
    @Override
    public Action<IStandPower> getVisibleAction(IStandPower power, ActionTarget target) {
        LivingEntity user = power.getUser();
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        return (cap.isPresent() && cap.get().hasWormhole()) ? InitStands.MOVE_WORMHOLE.get() : super.replaceAction(power, target);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.swinging = false;
        user.swing(Hand.MAIN_HAND);
        WormholeTeleporterEntity enter = new WormholeTeleporterEntity(world, user);
        enter.setExit(false);
        WormholeTeleporterEntity exit = new WormholeTeleporterEntity(world, user);
        exit.setExit(true);
        world.playSound(null, enter.getX(), enter.getEyeY(), enter.getZ(), InitSounds.WORMHOLE_OPEN.get(), user.getSoundSource(), 1.0F, 1.0F);
        world.playSound(null, exit.getX(), exit.getEyeY(), exit.getZ(), InitSounds.WORMHOLE_OPEN.get(), user.getSoundSource(), 1.0F, 1.0F);
        exit.setConnectedTeleporter(enter);
        enter.setConnectedTeleporter(exit);
        enter.moveTo(Vector3d.atCenterOf(user.blockPosition().above()));
        world.addFreshEntity(enter);
        user.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            nailCapability.setHasWormhole(true);
            WormholeEntity wormholeToReplace = nailCapability.getWormhole();
            BlockPos endBlockPos = new BlockPos(wormholeToReplace.position());
            exit.moveTo(Vector3d.atBottomCenterOf(endBlockPos));
            world.addFreshEntity(exit);
            nailCapability.getWormhole().remove();
            nailCapability.setWormhole(null);
        });
    }
}
