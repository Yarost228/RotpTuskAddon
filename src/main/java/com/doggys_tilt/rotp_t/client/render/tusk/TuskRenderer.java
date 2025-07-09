package com.doggys_tilt.rotp_t.client.render.tusk;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TuskRenderer extends StandEntityRenderer<TuskEntity, StandEntityModel<TuskEntity>> {
    public TuskRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(RotpTuskAddon.MOD_ID, "tusk"), TuskModel::new),
                new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/entity/stand/tusk.png"), 0);
    }

}
