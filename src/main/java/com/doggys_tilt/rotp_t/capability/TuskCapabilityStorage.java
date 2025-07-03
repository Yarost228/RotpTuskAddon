package com.doggys_tilt.rotp_t.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class TuskCapabilityStorage implements Capability.IStorage<TuskCapability> {

    @Override
    public INBT writeNBT(Capability<TuskCapability> capability, TuskCapability instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<TuskCapability> capability, TuskCapability instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}