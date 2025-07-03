package com.doggys_tilt.rotp_t.util;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RotpTuskAddon.MOD_ID)
public class GameplayEventHandler {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        entity.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(tuskCapability -> {
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
