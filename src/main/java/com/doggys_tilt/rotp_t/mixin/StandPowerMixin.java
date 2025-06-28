package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.StandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StandPower.class, remap = false)
public abstract class StandPowerMixin extends PowerBaseImpl {
    @Shadow public abstract StandType<?> getType();

    @Shadow public abstract IStandManifestation getStandManifestation();
    public StandPowerMixin(LivingEntity user) {
        super(user);
    }

    @Inject(method = "canLeap", at = @At(value = "RETURN"), cancellable = true)
    private void makeTuskLeapOnAct4(CallbackInfoReturnable<Boolean> cir){
        user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            if (this.getStandManifestation() instanceof TuskEntity){
                cir.setReturnValue(nailCapability.getAct() == 3);
            }
        });
    }
}
