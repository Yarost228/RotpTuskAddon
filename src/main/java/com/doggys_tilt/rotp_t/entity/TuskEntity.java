package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.util.TuskStandStats;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.world.World;

import java.util.UUID;

public class TuskEntity extends StandEntity {
    public TuskEntity(StandEntityType<TuskEntity> type, World world) {
        super(type, world);
    }

    public void onStandSummonServerSide() {
        super.onStandSummonServerSide();
        LivingEntity user = getUser();
        if (user != null) {
            updateStatsFromAct(user);
        }
    }
    @Override
    public void modifiersFromResolveLevel(float ratio) {}

    private void updateStatsFromAct(LivingEntity user){
        user.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCap -> {
            TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
            applyAttributeModifier(Attributes.ATTACK_DAMAGE, UUID.fromString("532a6cb6-0df0-44ea-a769-dba2db506545"),
                    "Stand attack damage from experience", stats.getActPower(nailCap.getAct()) - stats.getBasePower(), AttributeModifier.Operation.ADDITION);

            applyAttributeModifier(Attributes.ATTACK_SPEED, UUID.fromString("51f253cd-b440-48b1-aa60-89913963df51"),
                    "Stand attack speed from experience", stats.getActAttackSpeed(nailCap.getAct()) - stats.getBaseAttackSpeed(), AttributeModifier.Operation.ADDITION);

            applyAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.fromString("cbb892a6-3390-4e75-b0a3-04cd253033e3"),
                    "Stand movement speed from experience", stats.getActMovementSpeed(nailCap.getAct()) - stats.getBaseMovementSpeed(), AttributeModifier.Operation.ADDITION);

            applyAttributeModifier(ModEntityAttributes.STAND_DURABILITY.get(), UUID.fromString("d6979cd9-1481-49a0-bd5d-6922049448b4"),
                    "Stand durability from experience", stats.getActDurability(nailCap.getAct()) - stats.getBaseDurability(), AttributeModifier.Operation.ADDITION);

            applyAttributeModifier(ModEntityAttributes.STAND_PRECISION.get(), UUID.fromString("1dcdb98e-00fa-42d0-8867-242165e49832"),
                    "Stand precision from experience", stats.getActPrecision(nailCap.getAct()) - stats.getBasePrecision(), AttributeModifier.Operation.ADDITION);
        });
    }

    @Override
    public double getMaxRange() {
        LivingEntity user = getUser();
        if (user != null) {
            TuskCapability actCap = user.getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
            if (actCap != null && this.getUserPower().getType() != null && this.getUserPower().getType().getStats() instanceof TuskStandStats){
                TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
                return stats.getActRangeMax(actCap.getAct());
            }
        }
        return super.getMaxRange();
    }

    @Override
    public double getMaxEffectiveRange() {
        LivingEntity user = getUser();
        if (user != null) {
            TuskCapability actCap = user.getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
            if (actCap != null && this.getUserPower().getType() != null && this.getUserPower().getType().getStats() instanceof TuskStandStats){
                TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
                return stats.getActRange(actCap.getAct());
            }
        }
        return super.getMaxEffectiveRange();
    }

    private void applyAttributeModifier(Attribute attribute, UUID modifierId, String name, double value, AttributeModifier.Operation operation) {
        ModifiableAttributeInstance attributeInstance = getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(modifierId);
            attributeInstance.addTransientModifier(new AttributeModifier(modifierId, name, value, operation));
        }
    }

    private final StandRelativeOffset offsetAct4 = StandRelativeOffset.withYOffset(0, .5, -1.5);

    public StandRelativeOffset getDefaultOffsetFromUser() {
        TuskCapability capability = this.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        if (capability != null && capability.getAct() == 3){
            return offsetAct4;
        }
        else {
            return super.getDefaultOffsetFromUser();
        }
    }

}
