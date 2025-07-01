package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.WormholeArmEntity;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class TuskCreateWormholeWithArm extends StandAction {
    public TuskCreateWormholeWithArm(StandAction.Builder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public Action<IStandPower> getVisibleAction(IStandPower power, ActionTarget target) {
        LivingEntity user = power.getUser();
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        return (cap.isPresent() && cap.get().hasWormholeWithArm()) ? InitStands.MOVE_WORMHOLE_WITH_ARM.get() : super.replaceAction(power, target);
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getNailCount() > 0 && !cap.get().hasWormholeWithArm() && !cap.get().hasWormhole() && cap.get().getWormhole() != null){
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            nailCapability.hasWormholeWithArm(true);
            WormholeArmEntity wormholeArm = new WormholeArmEntity(user, world);
            wormholeArm.moveTo(nailCapability.getWormhole().position());
            world.addFreshEntity(wormholeArm);
            nailCapability.getWormhole().remove();
            nailCapability.setWormhole(null);
        });
    }
}
