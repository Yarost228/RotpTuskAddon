package com.doggys_tilt.rotp_t.client.render.nail;

import com.doggys_tilt.rotp_t.entity.NailEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class NailModel extends EntityModel<NailEntity> {
    private final ModelRenderer nail;

    public NailModel() {
        texWidth = 8;
        texHeight = 8;
        nail = new ModelRenderer(this);
        nail.setPos(0.0F, -0.5F, 0.0F);
        nail.texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(NailEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        nail.yRot = yRotationOffset * ((float) Math.PI / 180F);
        nail.xRot = xRotation * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        nail.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}