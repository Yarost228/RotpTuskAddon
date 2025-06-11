package com.rotpaddon.exampleaddon.client.render.layers;

import com.github.standobyte.jojo.client.render.entity.layerrenderer.IFirstPersonHandLayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ArmRotationLayer<T extends LivingEntity, M extends BipedModel<T> & IHasArm> extends LayerRenderer<T, M> implements IFirstPersonHandLayer {
    public ArmRotationLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }
    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot){

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
