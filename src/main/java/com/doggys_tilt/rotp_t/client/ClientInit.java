package com.doggys_tilt.rotp_t.client;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.client.render.RenderNothing;
import com.doggys_tilt.rotp_t.client.render.layers.ArmWormholeLayer;
import com.doggys_tilt.rotp_t.client.render.particle.NailSwipeParticle;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeRenderer;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeTinyRenderer;
import com.doggys_tilt.rotp_t.client.render.wormhole.WormholeWithArmRenderer;
import com.doggys_tilt.rotp_t.client.render.tusk.TuskRenderer;
import com.doggys_tilt.rotp_t.client.render.nail.NailRenderer;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.init.InitParticles;
import com.doggys_tilt.rotp_t.init.InitStands;

import com.doggys_tilt.rotp_t.util.TuskStandStats;
import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@EventBusSubscriber(modid = RotpTuskAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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

        RenderingRegistry.registerEntityRenderingHandler(
                InitEntities.BLOCK_SWAPPER.get(), RenderNothing::new);

        mc.getEntityRenderDispatcher().renderers.values().forEach(ClientInit::addLayersToEntities);
        Map<String, PlayerRenderer> skinMap = mc.getEntityRenderDispatcher().getSkinMap();
        addLayersToEntities(skinMap.get("default"));
        addLayersToEntities(skinMap.get("slim"));
        ClientEvents.init(mc);
        InitEffects.afterEffectsRegister();


        StandStatsRenderer.overrideCosmeticStats(
            InitStands.STAND_TUSK.getStandType().getRegistryName(),
            new StandStatsRenderer.ICosmeticStandStats() {
                public double statConvertedValue(StandStatsRenderer.StandStat stat, IStandPower standData, StandStats stats, float statLeveling) {
                    TuskStandStats actStats = (TuskStandStats) standData.getType().getStats();
                    if (standData.getUser() != null){
                        TuskCapability actCap = standData.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
                        if (actCap != null){
                            if (stat == StandStatsRenderer.StandStat.STRENGTH) {
                                return (actStats.getActPower(actCap.getAct())+ 1) / 3;
                            }
                            if (stat == StandStatsRenderer.StandStat.SPEED) {
                                return (actStats.getActAttackSpeed(actCap.getAct())+ 1) / 3;
                            }
                            if (stat == StandStatsRenderer.StandStat.DURABILITY) {
                                return (actStats.getActDurability(actCap.getAct())+ 1) / 3;
                            }
                            if (stat == StandStatsRenderer.StandStat.PRECISION) {
                                return (actStats.getActPrecision(actCap.getAct())+ 1) / 3;
                            }
                            if (stat == StandStatsRenderer.StandStat.RANGE) {
                                double value = (actStats.getActRange(actCap.getAct()) + (actStats.getActRangeMax(actCap.getAct()) - actStats.getActRange(actCap.getAct())) * 0.5);
                                value = Math.log(value / 1.5) + 1;
                                return value;
                            }
                        }
                    }
                    return StandStatsRenderer.ICosmeticStandStats.super.statConvertedValue(stat, standData, stats, statLeveling);
                }
            }
        );
        InitEffects.afterEffectsRegister();
    }

    @SubscribeEvent
    public static void onMcConstructor(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.register(InitParticles.NAIL_SWIPE.get(), NailSwipeParticle.Factory::new);
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addLayersToEntities(EntityRenderer<?> renderer) {
        if (renderer instanceof LivingRenderer && ((LivingRenderer<?, ?>) renderer).getModel() instanceof BipedModel<?>) {
            LivingRenderer<T, M> livingRenderer = (LivingRenderer<T, M>) renderer;
            livingRenderer.addLayer(new ArmWormholeLayer<>(livingRenderer));
        }
    }
}
