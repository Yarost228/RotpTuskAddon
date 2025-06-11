package com.rotpaddon.exampleaddon.client;

import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.client.render.ExampleStandRenderer;
import com.rotpaddon.exampleaddon.client.render.layers.ArmWormholeLayer;
import com.rotpaddon.exampleaddon.client.render.nail.NailRenderer;
import com.rotpaddon.exampleaddon.client.render.wormhole.WormholeRenderer;
import com.rotpaddon.exampleaddon.client.render.wormhole.WormholeWithArmRenderer;
import com.rotpaddon.exampleaddon.init.InitEntities;
import com.rotpaddon.exampleaddon.init.InitStands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
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
                InitStands.STAND_EXAMPLE_STAND.getEntityType(), ExampleStandRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.NAIL.get(), NailRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.WORMHOLE.get(), WormholeRenderer::new);

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
