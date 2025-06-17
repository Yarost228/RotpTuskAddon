package com.doggys_tilt.rotp_t.client;

import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayer;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeRenderer;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeTinyRenderer;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeWithArmRenderer;
import com.doggys_tilt.rotp_t.client.render.tusk.TuskRenderer;
import com.doggys_tilt.rotp_t.AddonMain;
import com.doggys_tilt.rotp_t.client.render.nail.NailRenderer;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.init.InitStands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@EventBusSubscriber(modid = AddonMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    private static Minecraft mc = Minecraft.getInstance();
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(
                InitStands.STAND_TUSK.getEntityType(), TuskRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.NAIL.get(), NailRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.WORMHOLE.get(), WormholeRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.DAMAGING_WORMHOLE.get(), WormholeTinyRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.WORMHOLE_WITH_ARM.get(), WormholeWithArmRenderer::new);

        mc.getEntityRenderDispatcher().renderers.values().forEach(ClientInit::addLayersToEntities);
        Map<String, PlayerRenderer> skinMap = mc.getEntityRenderDispatcher().getSkinMap();
        addLayersToEntities(skinMap.get("default"));
        addLayersToEntities(skinMap.get("slim"));
        ClientEvents.init(mc);
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addLayersToEntities(EntityRenderer<?> renderer) {
        if (renderer instanceof LivingRenderer && ((LivingRenderer<?, ?>) renderer).getModel() instanceof BipedModel<?>) {
            LivingRenderer<T, M> livingRenderer = (LivingRenderer<T, M>) renderer;
            livingRenderer.addLayer(new ArmWormholeLayer<>(livingRenderer));
        }
    }
}
