package com.doggys_tilt.rotp_t.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class TuskEntity extends StandEntity {
    private static final DataParameter<Boolean> HAS_PICKAXE = 
            EntityDataManager.defineId(TuskEntity.class, DataSerializers.BOOLEAN);

    public TuskEntity(StandEntityType<TuskEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HAS_PICKAXE, false);
    }
    
    public boolean hasPickaxe() {
        return entityData.get(HAS_PICKAXE);
    }
    
    public void setHasPickaxe(boolean hasPickaxe) {
        entityData.set(HAS_PICKAXE, hasPickaxe);
    }
}
