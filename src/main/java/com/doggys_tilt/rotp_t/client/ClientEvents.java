package com.doggys_tilt.rotp_t.client;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayer;
import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayerModel;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.github.standobyte.jojo.client.render.entity.layerrenderer.TornadoOverdriveEffectLayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
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
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void infiniteRotation(RenderLivingEvent.Pre event) {
        if (event.getEntity().hasEffect(InitEffects.INFINITE_ROTATION.get())){
            for (int i = 0; i < 3; ++i) {
                float f = event.getPartialRenderTick() * (float)(-(45 + i * 5));
                event.getMatrixStack().mulPose(Vector3f.YP.rotationDegrees(f));
            }
            event.getMatrixStack().translate((event.getEntity().level.random.nextFloat() - event.getEntity().level.random.nextFloat())*0.1, 0, (event.getEntity().level.random.nextFloat() - event.getEntity().level.random.nextFloat())*0.1);
        }
    }
}
