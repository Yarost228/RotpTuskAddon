package com.doggys_tilt.rotp_t.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class NailCapabilityStorage implements Capability.IStorage<NailCapability> {

    @Override
    public INBT writeNBT(Capability<NailCapability> capability, NailCapability instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        return nbt;
    }

    @Override
    public void readNBT(Capability<NailCapability> capability, NailCapability instance, Direction side, INBT nbt) {
    }
}