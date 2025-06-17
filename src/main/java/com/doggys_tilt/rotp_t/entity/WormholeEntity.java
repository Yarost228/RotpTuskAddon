package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.util.TuskUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WormholeEntity extends Entity {
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(WormholeArmEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Integer> LIFETIME = EntityDataManager.defineId(WormholeArmEntity.class, DataSerializers.INT);
    public WormholeEntity(EntityType<? extends WormholeEntity> type, World world) {
        super(type, world);
    }
    private int lifetime;
    public WormholeEntity(LivingEntity owner, World world, int lifetime) {
        super(InitEntities.DAMAGING_WORMHOLE.get(), world);
        this.setOwnerUUID(owner.getUUID());
        this.setLifetime(lifetime);
    }



    public boolean checkTargets(Entity entity){
        return this.getOwner() != null && entity != this.getOwner() && !entity.isAlliedTo(this.getOwner());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getLifetime() <= 0){
            this.remove();
        }
        List<Entity> entitiesAround = this.level.getEntities(this, this.getBoundingBox().inflate(12),
                entity -> (entity instanceof LivingEntity && this.checkTargets(entity)));
        if (!entitiesAround.isEmpty()) {
            Entity closestEntity = entitiesAround.stream()
                    .min(Comparator.comparingDouble(
                            target -> target.distanceToSqr(this)))
                    .get();
            Vector3d movement = this.position().vectorTo(closestEntity.position());
            this.move(MoverType.SELF, this.getDeltaMovement().add(movement.multiply(0.25, 1, 0.25)));
            if (closestEntity.distanceToSqr(this) < 0.2){
                closestEntity.hurt(DamageSource.mobAttack(getOwner()), 2F);
            }
        }
        this.move(MoverType.SELF, this.getDeltaMovement().add(0.0D, -0.7D, 0.0D));
        updateLifetime();
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

    @Nullable
    private UUID getOwnerUUID() {
        return entityData.get(OWNER_UUID).orElse(null);
    }

    private void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }
    private int getLifetime() {
        return entityData.get(LIFETIME);
    }

    private void setLifetime(int lifetime) {
        entityData.set(LIFETIME, lifetime);
    }

    private void updateLifetime() {
        entityData.set(LIFETIME, getLifetime()-1);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(OWNER_UUID, Optional.empty());
        entityData.define(LIFETIME, 1);
    }
    @Override
    protected void readAdditionalSaveData (CompoundNBT nbt){
        UUID ownerId = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        if (ownerId != null) {
            setOwnerUUID(ownerId);
        }
    }
    @Override
    protected void addAdditionalSaveData (CompoundNBT nbt){
        if (getOwnerUUID() != null) {
            nbt.putUUID("Owner", getOwnerUUID());
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
