package com.rotpaddon.exampleaddon.action;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.rotpaddon.exampleaddon.capability.NailCapability;
import com.rotpaddon.exampleaddon.capability.NailCapabilityProvider;
import com.rotpaddon.exampleaddon.entity.NailEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskChargedNailShot extends StandAction {
    public TuskChargedNailShot(StandAction.Builder builder) {
        super(builder.holdType(60));
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getNailCount() > 0){
                return ActionConditionResult.POSITIVE;
            }
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

    public void stoppedHolding(World world, LivingEntity user, IStandPower power, int ticksHeld, boolean willFire) {
        user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            user.swinging = false;
            if (nailCapability.getNailCount() > 0){
                if (!nailCapability.hasWormhole()){
                if (nailCapability.getNailCount() > 5){
                    user.swing(Hand.MAIN_HAND);
                }
                else {
                    user.swing(Hand.OFF_HAND);
                }
                    NailEntity nail = new NailEntity(user, world, ticksHeld/5);
                    float velocity = 2.0F + (float)user.getDeltaMovement().length() + ticksHeld/5F;
                    nail.shootFromRotation(user, velocity, 1.0F);
                    world.addFreshEntity(nail);
                    nailCapability.useNail();
                }
                nailCapability.setChargedShot(true);
            }
        });
    }
}
