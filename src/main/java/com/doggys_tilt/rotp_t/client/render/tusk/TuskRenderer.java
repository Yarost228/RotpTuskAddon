package com.doggys_tilt.rotp_t.client.render.tusk;

import com.doggys_tilt.rotp_t.AddonMain;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class TuskRenderer extends StandEntityRenderer<TuskEntity, StandEntityModel<TuskEntity>> {
    public TuskRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(AddonMain.MOD_ID, "tusk"), TuskModel::new),
                new ResourceLocation(AddonMain.MOD_ID, "textures/entity/stand/tusk.png"), 0);
    }
}
