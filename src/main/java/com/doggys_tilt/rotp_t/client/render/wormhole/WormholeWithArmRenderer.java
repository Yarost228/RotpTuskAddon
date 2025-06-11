package com.doggys_tilt.rotp_t.client.render.wormhole;

import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.doggys_tilt.rotp_t.AddonMain;
import com.doggys_tilt.rotp_t.entity.WormholeArmEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class WormholeWithArmRenderer extends SimpleEntityRenderer<WormholeArmEntity, WormholeWithArmModel> {

    public WormholeWithArmRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WormholeWithArmModel(), DefaultPlayerSkin.getDefaultSkin());
    }
    @Override
    protected void doRender(WormholeArmEntity entity, WormholeWithArmModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        renderModel(entity, model, partialTick, matrixStack, buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity))), packedLight);
        model.renderWormholeToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucentCull(new ResourceLocation(AddonMain.MOD_ID, "textures/entity/wormhole.png"))), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void rotateModel(WormholeWithArmModel model, WormholeArmEntity entity, float partialTick, float yRotation, float xRotation, MatrixStack matrixStack) {
        model.setupAnim(entity, 0, 0, entity.tickCount + partialTick, yRotation, xRotation);
    }

    @Override
    public ResourceLocation getTextureLocation(WormholeArmEntity entity) {
        Entity owner = entity.getOwner();
        return owner != null ? entityRenderDispatcher.getRenderer(owner).getTextureLocation(owner) :
                Minecraft.getInstance().player.getSkinTextureLocation();
    }
}
