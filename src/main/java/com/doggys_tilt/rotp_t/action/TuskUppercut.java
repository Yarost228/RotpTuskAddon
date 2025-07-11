package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class TuskUppercut extends StandEntityHeavyAttack {
    public TuskUppercut(Builder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public Action<IStandPower> replaceAction(IStandPower power, ActionTarget target) {
        TuskCapability tuskCapability = power.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        if (tuskCapability != null
                && tuskCapability.getAct() >= 3
                && tuskCapability.isHasInfiniteRotationCharge()) {
            return InitStands.TUSK_INFINITE_ROTATION.get();
        }
        return this;
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getAct() >= 3) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        StandEntityPunch punch = super.punchEntity(stand, target, dmgSource);
        double strength = stand.getAttackDamage();
        return punch
                .addKnockback(0.5F + (float) strength / 16 * stand.getLastHeavyFinisherValue())
                .knockbackXRot(-60F)
                .disableBlocking(1.0F);
    }
}
