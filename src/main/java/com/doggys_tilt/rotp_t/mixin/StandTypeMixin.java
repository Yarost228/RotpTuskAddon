package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.doggys_tilt.rotp_t.init.InitSounds;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    @Redirect(method = "summon", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/util/mod/JojoModUtil;sayVoiceLine(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/SoundEvent;)V"))
    private void changeSummonShoutByACT(LivingEntity entity, SoundEvent shout, LivingEntity user, IStandPower standPower, boolean withoutNameVoiceLine){
        if ((StandType) (Object) this == InitStands.STAND_TUSK.getStandType()){
            NailCapability capability = user.getCapability(NailCapabilityProvider.CAPABILITY).orElse(null);
            if (capability != null){
                switch (capability.getAct()){
                    case 0:
                        shout = InitSounds.JOHNNY_SUMMON_ACT_1.get();
                        break;
                    case 1:
                        shout = InitSounds.JOHNNY_SUMMON_ACT_2.get();
                        break;
                    case 2:
                        shout = InitSounds.JOHNNY_SUMMON_ACT_3.get();
                        break;
                    case 3:
                        shout = InitSounds.JOHNNY_SUMMON_ACT_4.get();
                        break;
                    default:
                        break;
                }
            }
            JojoModUtil.sayVoiceLine(user, shout);
        }

    }
}
