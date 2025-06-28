package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.init.InitEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Optional;

public class WormholeTeleporterEntity extends AbstractWormholeEntity {
    protected static final DataParameter<Integer> CONNECTED_TELEPORTER_ID = EntityDataManager.defineId(WormholeTeleporterEntity.class, DataSerializers.INT);
    private WormholeTeleporterEntity connectedTeleporter;
    private int ticksUntilTeleport = 120;

    public WormholeTeleporterEntity(EntityType<? extends WormholeTeleporterEntity> type, World world) {
        super(type, world, 600);
    }
    public WormholeTeleporterEntity(World world, LivingEntity owner) {
        super(InitEntities.WORMHOLE.get(), world, 600);
        this.setOwnerUUID(owner.getUUID());

    }

    @Override
    public void tick() {
        super.tick();
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox());
        if (!entities.isEmpty()) {
            ticksUntilTeleport--;
        }
        else ticksUntilTeleport = 60;
        entities.forEach(entity -> {
            if (ticksUntilTeleport <= 0 && !entities.isEmpty() && entity != null && connectedTeleporter != null){
                entity
                        .teleportToWithTicket(
                                connectedTeleporter.position().x,
                                connectedTeleporter.position().y,
                                connectedTeleporter.position().z);
                ticksUntilTeleport = 60;
            }
        });

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CONNECTED_TELEPORTER_ID, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
    }




    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public WormholeTeleporterEntity getConnectedTeleporter() {
        return (WormholeTeleporterEntity) this.level.getEntity(entityData.get(CONNECTED_TELEPORTER_ID));
    }

    public void setConnectedTeleporter(WormholeTeleporterEntity connectedTeleporter) {
        this.connectedTeleporter = connectedTeleporter;
        entityData.set(CONNECTED_TELEPORTER_ID, connectedTeleporter.getId());
    }
}
