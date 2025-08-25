package com.doggys_tilt.rotp_t.effects;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.doggys_tilt.rotp_t.init.InitParticles;
import com.github.standobyte.jojo.client.particle.LightModeFlashParticle;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.potion.UncurableEffect;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = RotpTuskAddon.MOD_ID)
public class InfiniteRotationEffect extends Effect {
    public InfiniteRotationEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int k = 10 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        }
        else {
            return true;
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level.isClientSide()) {
            if (livingEntity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) livingEntity;
                player.abilities.invulnerable = false;
            }
            else {
                livingEntity.setInvulnerable(false);
            }
            if (livingEntity.getVehicle() != null && livingEntity.getVehicle() instanceof LivingEntity){
                LivingEntity vehicle = (LivingEntity) livingEntity.getVehicle();
                vehicle.addEffect(new EffectInstance(this));
            }

//            if (IStandPower.getStandPowerOptional(livingEntity).isPresent()) {
//                IStandPower power = IStandPower.getStandPowerOptional(livingEntity).resolve().get();
//            }
        }
        livingEntity.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(tuskCapability ->
                livingEntity.setDeltaMovement((livingEntity.position().vectorTo(tuskCapability.getInfiniteRotationPos())).scale(0.25)));
    }
}
