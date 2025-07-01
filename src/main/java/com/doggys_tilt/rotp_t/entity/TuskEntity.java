package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.util.TuskStandStats;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModEntityAttributes;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class TuskEntity extends StandEntity {
    public TuskEntity(StandEntityType<TuskEntity> type, World world) {
        super(type, world);

    }

    private final StandRelativeOffset offsetAct4 = StandRelativeOffset.withYOffset(0, .5, -1.5);

    public StandRelativeOffset getDefaultOffsetFromUser() {
        NailCapability capability = this.getUser().getCapability(NailCapabilityProvider.CAPABILITY).orElse(null);
        if (capability != null && capability.getAct() == 3){
            return offsetAct4;
        }
        else {
            return super.getDefaultOffsetFromUser();
        }
    }

}
