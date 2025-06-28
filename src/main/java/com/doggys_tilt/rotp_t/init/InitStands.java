package com.doggys_tilt.rotp_t.init;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.action.*;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpTuskAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpTuskAddon.MOD_ID);
    
 // ======================================== Example Stand ========================================
    
    
    // Create all the abilities here...
    public static final RegistryObject<StandEntityAction> TUSK_PUNCH = ACTIONS.register("tusk_punch", 
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .punchSound(InitSounds.TUSK_PUNCH_LIGHT)));
    
    public static final RegistryObject<StandEntityAction> TUSK_BARRAGE = ACTIONS.register("tusk_barrage", 
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .barrageHitSound(InitSounds.TUSK_PUNCH_BARRAGE)));

    public static final RegistryObject<StandEntityHeavyAttack> TUSK_FINISHER_PUNCH = ACTIONS.register("tusk_finisher_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder() // TODO finisher ability
                    .punchSound(InitSounds.TUSK_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityHeavyAttack> TUSK_HEAVY_PUNCH = ACTIONS.register("tusk_heavy_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder()
                    .shiftVariationOf(TUSK_PUNCH).shiftVariationOf(TUSK_BARRAGE)
                    .setFinisherVariation(TUSK_FINISHER_PUNCH)
                    .punchSound(InitSounds.TUSK_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));
    
    public static final RegistryObject<StandEntityAction> TUSK_BLOCK = ACTIONS.register("tusk_block",
            () -> new StandEntityBlock());

    public static final RegistryObject<StandAction> NAIL_SHOT = ACTIONS.register("tusk_nail_shot",
            () -> new TuskNailShot(new StandAction.Builder()));

    public static final RegistryObject<StandAction> NAIL_SCRATCH = ACTIONS.register("tusk_nail_scratch",
            () -> new TuskNailScratch(new StandAction.Builder()));

    public static final RegistryObject<StandAction> WORMHOLE = ACTIONS.register("wormhole",
            () -> new TuskCreateWormhole(new StandAction.Builder()));

    public static final RegistryObject<StandAction> WORMHOLE_WITH_ARM = ACTIONS.register("wormhole_with_arm",
            () -> new TuskCreateWormholeWithArm(new StandAction.Builder()));

    public static final RegistryObject<StandAction> MOVE_WORMHOLE_WITH_ARM = ACTIONS.register("move_wormhole_with_arm",
            () -> new TuskMoveWormholeWithArm(new StandAction.Builder()));

    public static final RegistryObject<StandAction> CHARGED_NAIL_SHOT = ACTIONS.register("tusk_charged_nail_shot",
            () -> new TuskChargedNailShot(new StandAction.Builder().shiftVariationOf(NAIL_SHOT)));
    

    public static final RegistryObject<StandAction> TUSK_SELECT_ACT = ACTIONS.register("tusk_select_act",
            () -> new TuskSelectAct(new StandAction.Builder()));

    

    // ...then create the Stand type instance. Moves, stats, entity sizes, and a few other things are determined here.
    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<TuskEntity>> STAND_TUSK =
            new EntityStandRegistryObject<>("tusk",
                    STANDS, 
                    () -> new EntityStandType.Builder<StandStats>()
                    .color(0xff719a)
                    .storyPartName(ModStandsInit.PART_7_NAME)
                    .leftClickHotbar(
                            TUSK_PUNCH.get(),
                            TUSK_BARRAGE.get(),
                            NAIL_SCRATCH.get(),
                            NAIL_SHOT.get()

                            )
                    .rightClickHotbar(
                            TUSK_BLOCK.get(),
                            TUSK_SELECT_ACT.get(),
                            WORMHOLE.get(),
                            WORMHOLE_WITH_ARM.get()
                            )
                    .defaultStats(StandStats.class, new StandStats.Builder()
                            .tier(6)
                            .power(20)
                            .speed(20)
                            .range(50, 100)
                            .durability(20)
                            .precision(20)
                            .build())
//                    .addSummonShout(InitSounds.JOHNNY_SUMMON_VOICELINE)
                    .addOst(InitSounds.TUSK_OST)
                    .build(),
                    
                    InitEntities.ENTITIES,
                    () -> new StandEntityType<TuskEntity>(TuskEntity::new, 0.7F, 2.1F)
                    .summonSound(InitSounds.TUSK_SUMMON_SOUND)
                    .unsummonSound(InitSounds.TUSK_UNSUMMON_SOUND))
            .withDefaultStandAttributes();
    

    
    // ======================================== ??? ========================================
    
    
    
}
