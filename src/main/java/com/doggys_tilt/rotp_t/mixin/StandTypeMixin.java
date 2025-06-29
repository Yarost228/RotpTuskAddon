package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = StandType.class, remap = false)
public abstract class StandTypeMixin {

    @Unique
    private static final ResourceLocation[] rotpTuskAddon_main$tuskActTextures = {
            new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act1.png"),
            new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act2.png"),
            new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act3.png"),
            new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act4.png")
    };

    @Inject(method = "getIconTexture(Lcom/github/standobyte/jojo/power/impl/stand/IStandPower;)Lnet/minecraft/util/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void onGetIconTexture(@Nullable IStandPower power, CallbackInfoReturnable<ResourceLocation> cir) {
        if (power != null) {
            IStandManifestation stand = power.getStandManifestation();
            if (stand instanceof TuskEntity) {
                LivingEntity user = power.getUser();
                if (user != null) {
                    user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCap -> {
                        int act = nailCap.getAct();
                        if (act >= 0 && act < rotpTuskAddon_main$tuskActTextures.length) {
                            cir.setReturnValue(rotpTuskAddon_main$tuskActTextures[act]);
                        }
                    });
                }
            }
        }
    }
}
