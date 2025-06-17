package com.doggys_tilt.rotp_t.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class TuskEntity extends StandEntity {
    public TuskEntity(StandEntityType<TuskEntity> type, World world) {
        super(type, world);
    }
}
