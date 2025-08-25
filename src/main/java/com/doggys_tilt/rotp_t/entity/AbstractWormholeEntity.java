package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class AbstractWormholeEntity extends Entity {
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(AbstractWormholeEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Integer> LIFETIME = EntityDataManager.defineId(AbstractWormholeEntity.class, DataSerializers.INT);
    public AbstractWormholeEntity(EntityType<? extends AbstractWormholeEntity> type, World world, int lifetime) {
        super(type, world);
        this.setLifetime(lifetime);
    }

    public boolean checkTargets(Entity entity){
        return this.getOwner() != null && entity != this.getOwner() && !entity.isAlliedTo(this.getOwner());
    }
    protected int getLifetime() {
        return entityData.get(LIFETIME);
    }

    protected void setLifetime(int lifetime) {
        entityData.set(LIFETIME, lifetime);
    }

    protected void updateLifetime() {
        entityData.set(LIFETIME, getLifetime()-1);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.getLifetime() <= 0 || getOwner() == null || !getOwner().isAlive()){
            this.remove();
            return;
        }

        if (this.verticalCollision){
            this.move(MoverType.SELF, this.getDeltaMovement().add(0.0D, 0.7D, 0.0D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement().add(0.0D, -0.7D, 0.0D));
        this.updateLifetime();
    }

    @Override
    public void remove(){
        if (getOwner() != null){
            getOwner().getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                nailCapability.setWormhole(null);
            });
        }
        super.remove();
    }
    @Nullable
    protected UUID getOwnerUUID() {
        return entityData.get(OWNER_UUID).orElse(null);
    }

    protected void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(OWNER_UUID, Optional.empty());
        entityData.define(LIFETIME, 0);
    }
    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt){
        UUID ownerId = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        if (ownerId != null) {
            setOwnerUUID(ownerId);
        }
        setLifetime(nbt.getInt("Lifetime"));
    }
    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt){
        if (getOwnerUUID() != null) {
            nbt.putUUID("Owner", getOwnerUUID());
        }
        nbt.putInt("Lifetime", getLifetime());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            if (uuid == null) return null;
            PlayerEntity owner = level.getPlayerByUUID(uuid);
            return owner;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
