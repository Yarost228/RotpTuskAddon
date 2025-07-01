package com.doggys_tilt.rotp_t.capability;

import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import com.doggys_tilt.rotp_t.network.AddonPackets;
import com.doggys_tilt.rotp_t.network.NailDataPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class NailCapability {
    private final LivingEntity entity;
    private WormholeEntity wormhole = null;
    private int act = 0;
    private int prevAct = 0;
    private int nailCount = 10;
    private boolean hasWormholeWithArm = false;
    private boolean hasWormhole = false;
    private boolean chargedShot = false;

    public NailCapability(LivingEntity entity) {
        this.entity = entity;
    }

    public void useNail(){
        if (entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity;
            if (player.abilities.instabuild) return;
        }
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
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entity);
        }
    }

    public void addToNailWormholes(WormholeEntity entity){
        this.wormhole = entity;
    }
    

    public void syncWithClient(ServerPlayerEntity entityAsPlayer) {
        AddonPackets.sendToClient(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entityAsPlayer);
    }
    public boolean hasWormholeWithArm() {
        return hasWormholeWithArm;
    }

    public int getPrevAct(){
        return this.prevAct;
    }
    public void setPrevAct(int prevAct) {
        this.prevAct = prevAct;
    }
    public void hasWormholeWithArm(boolean hasWormholeWithArm) {
        this.hasWormholeWithArm = hasWormholeWithArm;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entity);
        }
    }
    public boolean chargedShot() {
        return chargedShot;
    }
    public void setChargedShot(boolean chargedShot) {
        this.chargedShot = chargedShot;
        if (!chargedShot){
            this.useNail();
        }
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entity);
        }
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.setPrevAct(this.act);
        this.act = act;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entity);
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Act", getAct());
        nbt.putInt("PrevAct", getPrevAct());
        nbt.putInt("NailCount", nailCount);
        nbt.putInt("NailWormholeId", wormhole != null ? wormhole.getId() : -1);
        return nbt;
    }



    public void fromNBT(CompoundNBT nbt) {
        this.setAct(nbt.getInt("Act"));
        prevAct = (nbt.getInt("PrevAct"));
        this.setNailCount(nbt.getInt("NailCount"));
        wormhole = (WormholeEntity) entity.level.getEntity(nbt.getInt("NailWormholeId"));
    }
    public WormholeEntity getWormhole() {
        return wormhole;
    }
    public void setWormhole(WormholeEntity wormhole){
        this.wormhole = wormhole;
    }
    public boolean hasWormhole() {
        return hasWormhole;
    }
    public void setHasWormhole(boolean hasWormhole) {
        this.hasWormhole = hasWormhole;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act), entity);
        }
    }
}


