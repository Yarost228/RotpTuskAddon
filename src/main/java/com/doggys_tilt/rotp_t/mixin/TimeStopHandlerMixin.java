package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TimeStopHandler.class, remap = false)
public class TimeStopHandlerMixin {
    @Inject(method = "updateEntityTimeStop", at = @At(value = "HEAD"), cancellable = true)
    private void injected(Entity entity, boolean canMove, boolean checkEffect, CallbackInfo ci){
        if (entity instanceof TuskEntity) {
            TuskEntity tusk = (TuskEntity) entity;
            if (tusk.getUser() != null){
                tusk.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    if (nailCapability.getAct() == 3){
                        ci.cancel();
                    }
                });
            }
        }
    }

    @Inject(method = "canPlayerSeeInStoppedTime(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("RETURN"), cancellable = true)
    private static void act4UserCanSee(PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
        player.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            if (nailCapability.getAct() == 3){
                cir.setReturnValue(true);
            }
        });
    }
}
