package com.doggys_tilt.rotp_t.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class WormholeTeleporterEntity extends Entity {
    private WormholeTeleporterEntity connectedTeleport;
    public WormholeTeleporterEntity(EntityType<? extends WormholeTeleporterEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }




    @Override
    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
