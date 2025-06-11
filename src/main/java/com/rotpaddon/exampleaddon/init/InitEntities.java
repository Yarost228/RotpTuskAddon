package com.rotpaddon.exampleaddon.init;

import com.rotpaddon.exampleaddon.AddonMain;

import com.rotpaddon.exampleaddon.entity.NailEntity;
import com.rotpaddon.exampleaddon.entity.WormholeArmEntity;
import com.rotpaddon.exampleaddon.entity.WormholeTeleporterEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, AddonMain.MOD_ID);

    public static final RegistryObject<EntityType<NailEntity>> NAIL = ENTITIES.register("nail",
            () -> EntityType.Builder.<NailEntity>of(NailEntity::new, EntityClassification.MISC)
                    .sized(0.2F, 0.2F)
                    .setUpdateInterval(10)
                    .build(new ResourceLocation(AddonMain.MOD_ID, "nail").toString()));

    public static final RegistryObject<EntityType<WormholeTeleporterEntity>> WORMHOLE = ENTITIES.register("wormhole",
            () -> EntityType.Builder.<WormholeTeleporterEntity>of(WormholeTeleporterEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 0.1F)
                    .build(new ResourceLocation(AddonMain.MOD_ID, "wormhole").toString()));

    public static final RegistryObject<EntityType<WormholeArmEntity>> WORMHOLE_WITH_ARM = ENTITIES.register("wormhole_with_arm",
            () -> EntityType.Builder.<WormholeArmEntity>of(WormholeArmEntity::new, EntityClassification.MISC)
                    .sized(1.0F, 0.1F)
                    .build(new ResourceLocation(AddonMain.MOD_ID, "wormhole").toString()));

};
