package com.rotpaddon.exampleaddon.client.render.wormhole;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.rotpaddon.exampleaddon.entity.WormholeTeleporterEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WormholeModel extends EntityModel<WormholeTeleporterEntity> {

    private final ModelRenderer wormhole;

    public WormholeModel() {
        texWidth = 128;
        texHeight = 128;

        wormhole = new ModelRenderer(this);
        wormhole.setPos(0, -1F, 0);
        wormhole.texOffs(0, 0).addBox(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(WormholeTeleporterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        wormhole.yRot = ageInTicks/4;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        wormhole.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
