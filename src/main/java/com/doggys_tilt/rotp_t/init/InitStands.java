package com.doggys_tilt.rotp_t.init;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.action.*;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.doggys_tilt.rotp_t.util.TuskStandStats;
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
    
 // ======================================== Tusk ========================================
    

    // Create all the abilities here...
    public static final RegistryObject<StandEntityAction> TUSK_PUNCH = ACTIONS.register("tusk_punch",
            () -> new TuskLightPunch(new TuskLightPunch.Builder()
                    .shout(InitSounds.JOHNNY_LIGHT_ATTACK)
                    .resolveLevelToUnlock(3)
                    .punchSound(InitSounds.TUSK_PUNCH_LIGHT)));

    public static final RegistryObject<StandEntityAction> TUSK_BARRAGE = ACTIONS.register("tusk_barrage",
            () -> new TuskBarrage(new TuskBarrage.Builder()
                    .standSound(StandEntityAction.Phase.PERFORM, false, InitSounds.JOHNNY_BARRAGE)
                    .resolveLevelToUnlock(3)
                    .barrageHitSound(InitSounds.TUSK_PUNCH_BARRAGE)));

    public static final RegistryObject<TuskInfiniteRotation> TUSK_INFINITE_ROTATION = ACTIONS.register("tusk_infinite_rotation",
            () -> new TuskInfiniteRotation(new TuskInfiniteRotation.Builder()
                    .staminaCost(425)
                    .shout(InitSounds.JOHNNY_HEAVY_ATTACK)
                    .standSound(InitSounds.TUSK_CHIMIMIN)
                    .standPose(TuskInfiniteRotation.INFINITE_ROTATION_ANIM)
                    .punchSound(InitSounds.TUSK_PUNCH_HEAVY).standOffsetFromUser(0, 1, 0.5)
                    .partsRequired(StandPart.MAIN_BODY)));

    public static final RegistryObject<StandEntityHeavyAttack> TUSK_UPPERCUT = ACTIONS.register("tusk_uppercut",
            () -> new TuskUppercut(new TuskUppercut.Builder() // TODO finisher ability
                    .resolveLevelToUnlock(3)
                    .shiftVariationOf(TUSK_PUNCH).shiftVariationOf(TUSK_BARRAGE)
                    .shout(InitSounds.JOHNNY_HEAVY_ATTACK)
                    .punchSound(InitSounds.TUSK_PUNCH_HEAVY)
                    .standPose(StarPlatinumUppercut.UPPERCUT_POSE)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityHeavyAttack> TUSK_HEAVY_PUNCH = ACTIONS.register("tusk_heavy_punch",
            () -> new TuskHeavyPunch(new TuskHeavyPunch.Builder()
                    .resolveLevelToUnlock(3)
                    .shiftVariationOf(TUSK_PUNCH).shiftVariationOf(TUSK_BARRAGE)
                    .shout(InitSounds.JOHNNY_HEAVY_ATTACK)
                    .setFinisherVariation(TUSK_UPPERCUT)
                    .punchSound(InitSounds.TUSK_PUNCH_HEAVY)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityAction> TUSK_BLOCK = ACTIONS.register("tusk_block",
            () -> new StandEntityBlock());

    public static final RegistryObject<StandAction> NAIL_SHOT = ACTIONS.register("tusk_nail_shot",
            () -> new TuskNailShot(new StandAction.Builder()
                    .staminaCostTick(2f)
                    .resolveLevelToUnlock(0)));

    public static final RegistryObject<StandAction> NAIL_SCRATCH = ACTIONS.register("tusk_nail_scratch",
            () -> new TuskNailScratch(new StandAction.Builder()
                    .staminaCost(100)
                    .resolveLevelToUnlock(0)));

    public static final RegistryObject<StandAction> WORMHOLE = ACTIONS.register("wormhole",
            () -> new TuskCreateWormhole(new StandAction.Builder()
                    .staminaCost(150)
                    .resolveLevelToUnlock(2)));

    public static final RegistryObject<StandAction> MOVE_WORMHOLE = ACTIONS.register("move_wormhole",
            () -> new TuskMoveWormhole(new StandAction.Builder()
                    .resolveLevelToUnlock(2)));

    public static final RegistryObject<StandAction> REMOVE_WORMHOLE = ACTIONS.register("remove_wormhole",
            () -> new TuskRemoveWormhole(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .holdType(1)
                    .shiftVariationOf(MOVE_WORMHOLE)));

    public static final RegistryObject<StandAction> WORMHOLE_WITH_ARM = ACTIONS.register("wormhole_with_arm",
            () -> new TuskCreateWormholeWithArm(new StandAction.Builder()
                    .resolveLevelToUnlock(2)));

    public static final RegistryObject<StandAction> MOVE_WORMHOLE_WITH_ARM = ACTIONS.register("move_wormhole_with_arm",
            () -> new TuskMoveWormholeWithArm(new StandAction.Builder()
                    .resolveLevelToUnlock(2)));

    public static final RegistryObject<StandAction> REMOVE_WORMHOLE_WITH_ARM = ACTIONS.register("remove_wormhole_with_arm",
            () -> new TuskRemoveWormholeWithArm(new StandAction.Builder()
                    .resolveLevelToUnlock(2)
                    .holdType(1)
                    .shiftVariationOf(MOVE_WORMHOLE_WITH_ARM)));

    public static final RegistryObject<StandAction> CHARGED_NAIL_SHOT = ACTIONS.register("tusk_charged_nail_shot",
            () -> new TuskChargedNailShot(new StandAction.Builder().shiftVariationOf(NAIL_SHOT)
                    .staminaCost(175)
                    .resolveLevelToUnlock(1)));
    

    public static final RegistryObject<StandAction> TUSK_SELECT_ACT = ACTIONS.register("tusk_select_act",
            () -> new TuskSelectAct(new StandAction.Builder().resolveLevelToUnlock(1)));

    public static final RegistryObject<StandAction> TUSK_OPEN_SPACE = ACTIONS.register("tusk_open_space",
            () -> new TuskOpenSpace(new StandEntityAction.Builder().resolveLevelToUnlock(3).staminaCost(225)));



    // ...then create the Stand type instance. Moves, stats, entity sizes, and a few other things are determined here.
    public static final EntityStandRegistryObject<EntityStandType<TuskStandStats>, StandEntityType<TuskEntity>> STAND_TUSK =
            new EntityStandRegistryObject<>("tusk",
                    STANDS, 
                    () -> new EntityStandType.Builder<TuskStandStats>()
                    .color(0xff719a)
                    .storyPartName(ModStandsInit.PART_7_NAME)
                    .leftClickHotbar(
                            TUSK_PUNCH.get(),
                            TUSK_BARRAGE.get(),
                            NAIL_SCRATCH.get(),
                            NAIL_SHOT.get())
                    .rightClickHotbar(
                            TUSK_BLOCK.get(),
                            TUSK_SELECT_ACT.get(),
                            WORMHOLE.get(),
                            WORMHOLE_WITH_ARM.get(),
                            TUSK_OPEN_SPACE.get())
                    .defaultStats(TuskStandStats.class, new TuskStandStats.Builder()

                            /*
                             * "A" - 14+
                             * "B" - 11-14
                             * "C" - 8-11
                             * "D" - 5-8
                             * "E" - 0-5
                             * "∅" - 0
                             */

                            .power(3)
                            .powerForActs(5, 7, 16)
                            .speed(3)
                            .speedForActs(5, 7, 14)
                            .range(2, 2)
                            .rangeForActs(5, 7,
                                    5, 7,
                                    8, 11)
                            .durability(12)
                            .durabilityForActs(10, 7, 16)
                            .precision(3)
                            .precisionForActs(8, 10, 13)
                            .build())
                    .addSummonShout(InitSounds.JOHNNY_SUMMON_ACT_1)
                    .addOst(InitSounds.TUSK_OST)
                    .build(),
                    
                    InitEntities.ENTITIES,
                    () -> new StandEntityType<TuskEntity>(TuskEntity::new, 0.7F, 2.1F)
                    .summonSound(InitSounds.TUSK_SUMMON_SOUND)
                    .unsummonSound(InitSounds.TUSK_UNSUMMON_SOUND))
            .withDefaultStandAttributes();
    

    
    // ======================================== ??? ========================================
    
    
    
}
