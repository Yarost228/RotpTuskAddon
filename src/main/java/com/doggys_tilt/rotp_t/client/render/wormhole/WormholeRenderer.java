package com.doggys_tilt.rotp_t.client.render.wormhole;

import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.entity.WormholeTeleporterEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WormholeRenderer extends SimpleEntityRenderer<WormholeTeleporterEntity, WormholeModel> {

    public WormholeRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new WormholeModel(), new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/entity/wormhole.png"));
    }

    protected void doRender(WormholeTeleporterEntity entity, WormholeModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        renderModel(entity, model, partialTick, matrixStack, buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity))), packedLight);
    }
}
