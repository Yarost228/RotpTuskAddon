package com.doggys_tilt.rotp_t.client.render.wormhole;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WormholeTinyModel extends EntityModel<WormholeEntity> {

    private final ModelRenderer hole;

    public WormholeTinyModel() {
        texWidth = 64;
        texHeight = 64;

        hole = new ModelRenderer(this);
        hole.texOffs(-16, 0).addBox(-8.5F, -0.1F, -7.5F, 16.0F, 0.0F, 16.0F, 0.0F, false);

    }
    @Override
    public void setupAnim(WormholeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        hole.yRot = ageInTicks/4;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hole.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
