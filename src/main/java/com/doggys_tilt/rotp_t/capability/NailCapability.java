package com.doggys_tilt.rotp_t.capability;

import com.doggys_tilt.rotp_t.network.AddonPackets;
import com.doggys_tilt.rotp_t.network.NailDataPacket;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromserver.TrDoubleShiftPacket;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class NailCapability {
    private final LivingEntity entity;
    private int act = 0;
    private int prevAct = 0;
    private int nailCount = 999;
    private boolean hasWormhole = false;
    private boolean chargedShot = false;

    public NailCapability(LivingEntity entity) {
        this.entity = entity;
    }
    public void useNail(){
        if (this.getNailCount() > 0) {
            this.setNailCount(this.getNailCount()-1);
        }
    }
    public int getNailCount(){
        return this.nailCount;
    }
    public void setNailCount(int nailCount){
        this.nailCount = nailCount;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTracking(new NailDataPacket(entity.getId(), nailCount, hasWormhole, chargedShot, act), entity);
        }
    }
    public void syncWithEntityOnly(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new NailDataPacket(player.getId(), nailCount, hasWormhole, chargedShot, act), player);
    }
    public boolean hasWormhole() {
        return hasWormhole;
    }

    public int getPrevAct(){
        return this.prevAct;
    }
    public void setWormhole(boolean hasWormhole) {
        this.hasWormhole = hasWormhole;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTracking(new NailDataPacket(entity.getId(), nailCount, hasWormhole, chargedShot, act), entity);
        }
    }
    public boolean chargedShot() {
        return chargedShot;
    }
    public void setChargedShot(boolean chargedShot) {
        this.chargedShot = chargedShot;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTracking(new NailDataPacket(entity.getId(), nailCount, hasWormhole, chargedShot, act), entity);
        }
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTracking(new NailDataPacket(entity.getId(), nailCount, hasWormhole, chargedShot, act), entity);
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Act", getAct());
        nbt.putInt("PrevAct", getPrevAct());
        nbt.putInt("NailCount", nailCount);
        return nbt;
    }



    public void fromNBT(CompoundNBT nbt) {
        this.setAct(nbt.getInt("Act"));
        prevAct =(nbt.getInt("PrevAct"));
        this.setNailCount(nbt.getInt("NailCount"));
    }
}


