package com.doggys_tilt.rotp_t.effects;

import com.github.standobyte.jojo.potion.UncurableEffect;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class InfiniteRotationEffect extends UncurableEffect {
    public InfiniteRotationEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int level) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level.isClientSide()) {
            if (IStandPower.getStandPowerOptional(livingEntity).isPresent()) {
                IStandPower power = IStandPower.getStandPowerOptional(livingEntity).resolve().get();

            }
        }
    }
}
