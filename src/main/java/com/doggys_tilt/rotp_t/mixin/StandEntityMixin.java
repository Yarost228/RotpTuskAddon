package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.util.TuskStandStats;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = StandEntity.class, remap = false)
public abstract class StandEntityMixin {

    @Shadow
    public abstract LivingEntity getUser();

    @Shadow
    public abstract IStandPower getUserPower();

    @ModifyArg(method = "updateStrengthMultipliers", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/entity/stand/StandStatFormulas;rangeStrengthFactor(DDD)F"), index = 0)
    private double changeTuskEffectiveRangeInStrengthMultiplier(double rangeEffective){
        LivingEntity user = getUser();
        if (user != null) {
            NailCapability actCap = user.getCapability(NailCapabilityProvider.CAPABILITY).orElse(null);
            if (actCap != null && getUserPower().getType().getStats() instanceof TuskStandStats) {
                TuskStandStats stats = (TuskStandStats) getUserPower().getType().getStats();
                return stats.getActRange(actCap.getAct());
            }
        }
        return rangeEffective;
    }
}
