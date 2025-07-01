package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StandStats.class, remap = false)
public class StandStatsMixin {
    @Inject(method = "getBasePower", at = @At(value = "RETURN"), cancellable = true)
    private void tuskPowerChangeStatsByAct(CallbackInfoReturnable<Double> cir){
        Minecraft mc = Minecraft.getInstance();
        mc.player.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            switch (nailCapability.getAct()){
                case 0:
                    break;
                case 1:
                    cir.setReturnValue(5.0D);
                    break;
                case 2:
                    cir.setReturnValue(11.0D);
                    break;
                case 3:
                    cir.setReturnValue(16.0D);
                    break;
            }
        });

    }
}
