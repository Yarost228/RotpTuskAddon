package com.doggys_tilt.rotp_t.capability;

import com.doggys_tilt.rotp_t.AddonMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation CAPABILITY_ID = new ResourceLocation(AddonMain.MOD_ID, "nail_data");

    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(NailCapability.class, new NailCapabilityStorage(), () -> new NailCapability(null));
    }
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity){
            event.addCapability(CAPABILITY_ID, new NailCapabilityProvider((LivingEntity)entity));
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
        });
    }
}
