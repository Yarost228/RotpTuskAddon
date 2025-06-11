package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.NailEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskNailShot extends StandAction {
    public TuskNailShot(StandAction.Builder builder) {
        super(builder.holdType());
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent() && cap.get().getNailCount() > 0){
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected ActionConditionResult checkHeldItems(LivingEntity user, IStandPower power) {
        if (!(MCUtil.isHandFree(user, Hand.MAIN_HAND) && MCUtil.isHandFree(user, Hand.OFF_HAND))) {
            return conditionMessage("hands");
        }
        return ActionConditionResult.POSITIVE;
    }

    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (requirementsFulfilled){
            user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    if (nailCapability.getNailCount() > 0) {
                        if (ticksHeld % 5 == 0) {
                            user.swinging = false;
                        if (nailCapability.getNailCount() > 5) {
                            user.swing(Hand.MAIN_HAND);
                        } else {
                            user.swing(Hand.OFF_HAND);
                        }
                        if (!nailCapability.hasWormhole()){
                            NailEntity nail = new NailEntity(user, world, 0);
                            float velocity = 2.0F + (float)user.getDeltaMovement().length();
                            nail.shootFromRotation(user, velocity, 1.0F);
                            world.addFreshEntity(nail);
                            nailCapability.useNail();
                        }
                    }
                }
                else {
                    power.stopHeldAction(false);
                }
            });
        }
    }
}

