package com.doggys_tilt.rotp_t.client.render.layers;

import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ArmWormholeLayerModel<T extends LivingEntity> extends BipedModel<T>{
    public final ModelRenderer wormhole;
    public final ModelRenderer wormhole2;

    public ArmWormholeLayerModel(float inflate) {
        super(inflate);
        texWidth = 64;
        texHeight = 64;
        wormhole = new ModelRenderer(this);
        wormhole.setPos(4.1F, 2.0F, 0.0F);
        rightArm.addChild(wormhole);
        wormhole.texOffs(0, -16).addBox(0.0F, -8.5F, -7.5F, -0.5F, 16.0F, 16.0F, 0.0F, false);

        wormhole2 = new ModelRenderer(this);
        wormhole2.setPos(-4.1F, 2.0F, 0.0F);
        leftArm.addChild(wormhole2);
        wormhole2.texOffs(0, -16).addBox(0.0F, -8.0F, -7.5F, -0.5F, 16.0F, 16.0F, 0.0F, false);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.visible = false;
        wormhole.xRot = ageInTicks / 4;
        wormhole2.xRot = ageInTicks / 4;
        entity.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
            switch (entity.getMainArm()) {
                case LEFT:
                    if (nailCapability.getNailCount() > 5) {
                        wormhole2.visible = false;
                        wormhole.visible = true;
                    } else if (nailCapability.getNailCount() > 0) {
                        wormhole.visible = false;
                        wormhole2.visible = true;
                    }
                    break;
                case RIGHT:
                    if (nailCapability.getNailCount() > 5) {
                        wormhole.visible = false;
                        wormhole2.visible = true;
                    } else if (nailCapability.getNailCount() > 0) {
                        wormhole2.visible = false;
                        wormhole.visible = true;
                    }
                    break;
            }
        });
    }
    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        wormhole.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        wormhole2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}
