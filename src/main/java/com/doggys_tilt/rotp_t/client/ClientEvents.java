package com.doggys_tilt.rotp_t.client;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayer;
import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    private final Minecraft mc;
    private ClientEvents(Minecraft mc) {
        this.mc = mc;
    }

    public static void init(Minecraft mc){
        MinecraftForge.EVENT_BUS.register(new ClientEvents(mc));
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        EntityModel model = event.getRenderer().getModel();
        LivingEntity entity = event.getEntity();
        if (model instanceof BipedModel) {
            ArmWormholeLayer wormholeLayer = new ArmWormholeLayer(event.getRenderer());
            if (wormholeLayer.shouldRender(entity)){
                ArmWormholeLayerModel wormholeLayerModel = wormholeLayer.getLayerModel();
                entity.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    switch (entity.getMainArm()){
                        case LEFT:
                            if (nailCapability.getNailCount() > 5){
                                ((BipedModel<?>) model).leftArm.visible = false;
                                wormholeLayerModel.wormhole2.visible = false;
                            }
                            else if (nailCapability.getNailCount() > 0){
                                ((BipedModel<?>) model).rightArm.visible = false;
                                wormholeLayerModel.wormhole.visible = false;
                            }
                            break;
                        case RIGHT:
                            if (nailCapability.getNailCount() > 5){
                                ((BipedModel<?>) model).rightArm.visible = false;
                                wormholeLayerModel.wormhole.visible = false;
                            }
                            else if (nailCapability.getNailCount() > 0){
                                ((BipedModel<?>) model).leftArm.visible = false;
                                wormholeLayerModel.wormhole2.visible = false;
                            }
                            break;
                    }
                });
            }
        }
    }
}
