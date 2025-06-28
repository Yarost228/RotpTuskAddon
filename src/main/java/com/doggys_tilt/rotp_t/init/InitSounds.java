package com.doggys_tilt.rotp_t.init;

import java.util.function.Supplier;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS, RotpTuskAddon.MOD_ID); // TODO sounds.json

    public static final RegistryObject<SoundEvent> JOHNNY_SUMMON_ACT_1 = SOUNDS.register("johnny_act_1_summon",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "johnny_act_1_summon")));

    public static final RegistryObject<SoundEvent> JOHNNY_SUMMON_ACT_2 = SOUNDS.register("johnny_act_2_summon",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "johnny_act_2_summon")));

    public static final RegistryObject<SoundEvent> JOHNNY_SUMMON_ACT_3 = SOUNDS.register("johnny_act_3_summon",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "johnny_act_3_summon")));

    public static final RegistryObject<SoundEvent> JOHNNY_SUMMON_ACT_4 = SOUNDS.register("johnny_act_4_summon",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "johnny_act_4_summon")));

    public static final RegistryObject<SoundEvent> TUSK_CHIMIMIN = SOUNDS.register("chimimi",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "chimimi")));

    public static final RegistryObject<SoundEvent> SCRATCH = SOUNDS.register("scratch",
            () -> new SoundEvent(new ResourceLocation(RotpTuskAddon.MOD_ID, "scratch")));

    public static final Supplier<SoundEvent> TUSK_SUMMON_SOUND = ModSounds.STAND_SUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> TUSK_UNSUMMON_SOUND = ModSounds.STAND_UNSUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> TUSK_PUNCH_LIGHT = ModSounds.STAND_PUNCH_LIGHT;
    
    public static final Supplier<SoundEvent> TUSK_PUNCH_HEAVY = ModSounds.STAND_PUNCH_HEAVY;
    
    public static final Supplier<SoundEvent> TUSK_PUNCH_BARRAGE = ModSounds.STAND_PUNCH_LIGHT;


    public static final OstSoundList TUSK_OST = new OstSoundList(
            new ResourceLocation(RotpTuskAddon.MOD_ID, "tusk_ost"), SOUNDS);
}
