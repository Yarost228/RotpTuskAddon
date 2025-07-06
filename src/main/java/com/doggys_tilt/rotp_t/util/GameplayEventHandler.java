package com.doggys_tilt.rotp_t.util;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RotpTuskAddon.MOD_ID)
public class GameplayEventHandler {

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        TuskCapability tuskCapability = event.player.getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        if (tuskCapability != null && tuskCapability.hasWormholeWithArm()) {
            ItemStack handItem = tuskCapability.getNailCount() > 5 ? event.player.getMainHandItem() : event.player.getOffhandItem();
            if (!handItem.isEmpty()) {
                ItemStack droppedItem = handItem.copy();
                event.player.setItemInHand(tuskCapability.getNailCount() > 5 ? Hand.MAIN_HAND : Hand.OFF_HAND, ItemStack.EMPTY);
                ItemEntity itemEntity = new ItemEntity(
                        event.player.level,
                        event.player.getX(),
                        event.player.getY() + event.player.getEyeHeight(),
                        event.player.getZ(),
                        droppedItem
                );
                itemEntity.setPickUpDelay(40);
                event.player.level.addFreshEntity(itemEntity);
            }
        }
    }



    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        entity.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(tuskCapability -> {
            tuskCapability.tick();
            if (!entity.hasEffect(InitEffects.INFINITE_ROTATION.get())){
                tuskCapability.setInfiniteRotationPos(entity.position());
            }
            if (IStandPower.getStandPowerOptional(entity).isPresent()){
                IStandPower power = IStandPower.getStandPowerOptional(entity).resolve().get();
                if (entity.getVehicle() instanceof HorseEntity
                        && entity.hasEffect(ModStatusEffects.RESOLVE.get())
                        && power.getResolveLevel() >= 4
                        && tuskCapability.getAct() >= 3){
                    tuskCapability.setHasInfiniteRotationCharge(true);
                }
            }
        });
    }
}
