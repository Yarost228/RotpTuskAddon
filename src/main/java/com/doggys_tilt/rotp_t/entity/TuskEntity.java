package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.util.TuskStandStats;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModEntityAttributes;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class TuskEntity extends StandEntity {
    protected static final DataParameter<Integer> ACT = EntityDataManager.defineId(TuskEntity.class, DataSerializers.INT);
    public TuskEntity(StandEntityType<TuskEntity> type, World world) {
        super(type, world);
    }

    public void onStandSummonServerSide() {
        super.onStandSummonServerSide();
        LivingEntity user = getUser();
        if (user != null) {
            user.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(tuskCap -> this.setAct(tuskCap.getAct()));
        }
        updateStatsFromAct(getAct());
    }

    public int getAct() {
        return entityData.get(ACT);
    }

    protected void setAct(int act) {
        entityData.set(ACT, act);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ACT, 0);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        super.writeSpawnData(buffer);
        buffer.writeInt(getAct());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        super.readSpawnData(additionalData);
        setAct(additionalData.readInt());
    }

    @Override
    public EntitySize getDimensions(Pose p_213305_1_) {
        EntitySize size = super.getDimensions(p_213305_1_);
        if (this.getUser() != null){
            TuskCapability tuskCap = this.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
            size = size.scale((tuskCap.getAct() + 1) / 4F);
        }
        return size;
    }

    @Override
    public void modifiersFromResolveLevel(float ratio) {}

    private void updateStatsFromAct(int act){
        TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
        applyAttributeModifier(Attributes.ATTACK_DAMAGE, UUID.fromString("532a6cb6-0df0-44ea-a769-dba2db506545"),
                "Stand attack damage from experience", stats.getActPower(act) - stats.getBasePower(), AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(Attributes.ATTACK_SPEED, UUID.fromString("51f253cd-b440-48b1-aa60-89913963df51"),
                "Stand attack speed from experience", stats.getActAttackSpeed(act) - stats.getBaseAttackSpeed(), AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.fromString("cbb892a6-3390-4e75-b0a3-04cd253033e3"),
                "Stand movement speed from experience", stats.getActMovementSpeed(act) - stats.getBaseMovementSpeed(), AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(ModEntityAttributes.STAND_DURABILITY.get(), UUID.fromString("d6979cd9-1481-49a0-bd5d-6922049448b4"),
                "Stand durability from experience", stats.getActDurability(act) - stats.getBaseDurability(), AttributeModifier.Operation.ADDITION);

        applyAttributeModifier(ModEntityAttributes.STAND_PRECISION.get(), UUID.fromString("1dcdb98e-00fa-42d0-8867-242165e49832"),
                "Stand precision from experience", stats.getActPrecision(act) - stats.getBasePrecision(), AttributeModifier.Operation.ADDITION);
    
    }

    @Override
    public double getMaxRange() {
        LivingEntity user = getUser();
        if (user != null) {
            if (this.getUserPower().getType() != null && this.getUserPower().getType().getStats() instanceof TuskStandStats){
                TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
                return stats.getActRangeMax(getAct());
            }
        }
        return super.getMaxRange();
    }

    @Override
    public double getMaxEffectiveRange() {
        LivingEntity user = getUser();
        if (user != null) {
            if (this.getUserPower().getType() != null && this.getUserPower().getType().getStats() instanceof TuskStandStats){
                TuskStandStats stats = (TuskStandStats) this.getUserPower().getType().getStats();
                return stats.getActRange(getAct());
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
        if (!isArmsOnlyMode()){
            if (getAct() == 3) {
                return offsetAct4;
            }
            else {
                return StandRelativeOffset.withYOffset(-0.75, getAct() == 0 ? 1.5 : getAct() == 1 ? 0.75 : 0.2, -0.75);
            }
        }
        return super.getDefaultOffsetFromUser();
    }

}
