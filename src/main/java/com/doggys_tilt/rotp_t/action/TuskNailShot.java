package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.NailEntity;
import com.doggys_tilt.rotp_t.init.InitSounds;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.entity.stand.stands.HierophantGreenEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.ResolveCounter;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskNailShot extends StandAction {
    public TuskNailShot(StandAction.Builder builder) {
        super(builder.holdType());
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        if ((cap.isPresent()
                && ((MCUtil.isHandFree(user, Hand.MAIN_HAND) && cap.get().getNailCount() > 5)
                || (MCUtil.isHandFree(user, Hand.OFF_HAND) && cap.get().getNailCount() > 0 && cap.get().getNailCount() <= 5)))
                || (user instanceof PlayerEntity && ((PlayerEntity)user).abilities.instabuild)) {
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

//    @Override
//    protected ActionConditionResult checkHeldItems(LivingEntity user, IStandPower power) {
//        if (!(MCUtil.isHandFree(user, Hand.MAIN_HAND) && MCUtil.isHandFree(user, Hand.OFF_HAND))) {
//            return conditionMessage("hands");
//        }
//        return ActionConditionResult.POSITIVE;
//    }
    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        TuskCapability tuskCap = power.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        int nails = tuskCap != null ? tuskCap.getNailCount() : 0;
        return new TranslationTextComponent(key, nails, 10);
    }

    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (requirementsFulfilled){
            user.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                if (nailCapability.getNailCount() > 0 && ticksHeld % 5 == 0) {
                    if (world.isClientSide()){
                        user.swinging = false;
                        if (nailCapability.getNailCount() > 5) {
                            user.swing(Hand.MAIN_HAND);
                        } else {
                            user.swing(Hand.OFF_HAND);
                        }
                    }
                    else {
                        nailCapability.useNail();
                        if (!nailCapability.hasWormholeWithArm()){
                            NailEntity nail = new NailEntity(user, world, 0);
                            float velocity = 2.0F + (float)user.getDeltaMovement().length();
                            nail.shootFromRotation(user, velocity, 1.0F);
                            world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), InitSounds.NAIL_SHOT.get(), user.getSoundSource(), 1.0F, 1.0F);
                            world.addFreshEntity(nail);
                        }
                    }
                }
            });
        }
    }
}

