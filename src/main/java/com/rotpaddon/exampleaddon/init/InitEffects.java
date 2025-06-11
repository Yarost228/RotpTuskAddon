package com.rotpaddon.exampleaddon.init;

import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.effects.InfiniteRotationEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, AddonMain.MOD_ID);
    public static final RegistryObject<InfiniteRotationEffect> INFINITE_ROTATION = EFFECTS.register("infinite_rotation",
            () -> new InfiniteRotationEffect(EffectType.HARMFUL, 0xFFC10A));
}
