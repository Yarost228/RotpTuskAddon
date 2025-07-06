package com.doggys_tilt.rotp_t.mixin;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin extends BipedModel<PlayerEntity> {

    public PlayerModelMixin(float p_i1148_1_) {
        super(p_i1148_1_);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void rotateArmsForNailShot(LivingEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation, CallbackInfo ci){
        IStandPower power = IStandPower.getPlayerStandPower((PlayerEntity) entity);
        if (power.getHeldAction() == InitStands.CHARGED_NAIL_SHOT.get() || ActionsOverlayGui.getInstance().isActionSelectedAndEnabled(InitStands.NAIL_SHOT.get())){
            TuskCapability tuskCap = entity.getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
            if (tuskCap != null){
                ModelRenderer rotatingArm = head;
                switch (entity.getMainArm()){
                    case LEFT:
                        rotatingArm = tuskCap.getNailCount() > 5 ? leftArm : rightArm;
                        break;
                    case RIGHT:
                        rotatingArm = tuskCap.getNailCount() > 5 ? rightArm : leftArm;
                        break;
                }
                 rotatingArm.xRot = (xRotation - 90) * MathUtil.DEG_TO_RAD ;
                 rotatingArm.yRot = yRotationOffset * MathUtil.DEG_TO_RAD;
            }
        }
    }
}
