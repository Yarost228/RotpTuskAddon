package com.doggys_tilt.rotp_t;

import com.doggys_tilt.rotp_t.capability.CapabilityHandler;
import com.doggys_tilt.rotp_t.init.*;
import com.doggys_tilt.rotp_t.network.AddonPackets;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.github.standobyte.jojo.init.ModStatusEffects.setEffectAsTracked;

// Your addon's main file

@Mod(RotpTuskAddon.MOD_ID)
public class RotpTuskAddon {
    // The mod's id. Used quite often, mostly when creating ResourceLocation (objects).
    // Its value should match the "modid" entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_t";
    public static final Logger LOGGER = LogManager.getLogger();

    public RotpTuskAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // All DeferredRegister objects are registered here.
        // A DeferredRegister needs to be created for each type of objects that need to be registered in the game 
        // (see ForgeRegistries or JojoCustomRegistries)
        InitEntities.ENTITIES.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
        InitParticles.PARTICLES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::preInit);
    }
    private void preInit(FMLCommonSetupEvent event) {
        AddonPackets.init();
        CapabilityHandler.commonSetupRegister();

    }
}
