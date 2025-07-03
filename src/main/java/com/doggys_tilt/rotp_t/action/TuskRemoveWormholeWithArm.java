package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class TuskRemoveWormholeWithArm extends StandAction {
    public TuskRemoveWormholeWithArm(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public boolean isUnlocked(IStandPower power) {
        return InitStands.WORMHOLE_WITH_ARM.get().isUnlocked(power);
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().hasWormholeWithArm() && cap.get().getAct() >= 2) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }
}
