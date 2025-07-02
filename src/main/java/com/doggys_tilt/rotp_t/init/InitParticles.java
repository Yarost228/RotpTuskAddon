package com.doggys_tilt.rotp_t.init;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RotpTuskAddon.MOD_ID);

    public static final RegistryObject<BasicParticleType> NAIL_SWIPE = PARTICLES.register("nail_swipe", () -> new BasicParticleType(false));
}
