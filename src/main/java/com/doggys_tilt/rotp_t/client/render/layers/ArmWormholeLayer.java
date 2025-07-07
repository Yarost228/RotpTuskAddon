package com.doggys_tilt.rotp_t.client.render.layers;

import com.github.standobyte.jojo.client.render.entity.layerrenderer.IFirstPersonHandLayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class ArmWormholeLayer<T extends LivingEntity, M extends BipedModel<T> & IHasArm> extends LayerRenderer<T, M> implements IFirstPersonHandLayer {
    public ArmWormholeLayer(LivingRenderer<T, M> renderer) {
        super(renderer);
    }

    public boolean shouldRender(T entity){
        Optional<TuskCapability> cap = entity.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        return entity.isAlive() && cap.map(TuskCapability::hasWormholeWithArm).orElse(false);
    }

    private ArmWormholeLayerModel wmodel = new ArmWormholeLayerModel(1F);
    public ArmWormholeLayerModel getLayerModel(){
        return wmodel;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot){
        if (shouldRender(entity)){
            matrixStack.pushPose();
            wmodel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
            M model = getParentModel();
            model.copyPropertiesTo(wmodel);
            wmodel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);
            wmodel.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/entity/tiny_wormhole.png"))), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
    public void renderHandFirstPerson(HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, AbstractClientPlayerEntity player, PlayerRenderer playerRenderer) {
        PlayerModel<AbstractClientPlayerEntity> model = playerRenderer.getModel();
        IFirstPersonHandLayer.defaultRender(side, matrixStack, buffer, light, player, playerRenderer, model, this.getTexture(model, player));
    }
    @Nullable
    private ResourceLocation getTexture(EntityModel<?> model, LivingEntity entity) {
        return null;
    }
}
