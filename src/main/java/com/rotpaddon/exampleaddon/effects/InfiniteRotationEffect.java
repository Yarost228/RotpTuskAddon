package com.rotpaddon.exampleaddon.effects;

import com.github.standobyte.jojo.potion.UncurableEffect;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class InfiniteRotationEffect extends UncurableEffect {
    public InfiniteRotationEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int level) {
        int j = 25 >> level;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level.isClientSide()) {
            if (IStandPower.getStandPowerOptional(livingEntity).isPresent()){
                IStandPower power = IStandPower.getStandPowerOptional(livingEntity).resolve().get();
//                power.
            }
        }
    }
}
