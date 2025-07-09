package com.doggys_tilt.rotp_t.capability;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import com.doggys_tilt.rotp_t.network.AddonPackets;
import com.doggys_tilt.rotp_t.network.NailDataPacket;
import com.github.standobyte.jojo.client.InputHandler;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

public class TuskCapability {
    private final LivingEntity entity;
    private WormholeEntity wormhole = null;
    private int nextAct = 0;
    private int act = 0;
    private int prevAct = 0;
    private int nailCount = 10;
    private int nailRegenTimer = 400;
    private boolean hasWormholeWithArm = false;
    private boolean hasWormhole = false;
    private boolean hasInfiniteRotationCharge = false;
    private boolean shouldChangeAct = false;
    private boolean chargedShot = false;
    private Vector3d infiniteRotationPos;
    public TuskCapability(LivingEntity entity) {
        this.entity = entity;
        this.infiniteRotationPos = entity.position();
    }


    public void tick(){
        IStandPower standPower = IStandPower.getStandPowerOptional(entity).orElse(null);
        if (standPower != null && !standPower.isActive() && shouldChangeAct()) {
            setShouldChangeAct(false);
            setAct(nextAct);
            standPower.getType().summon(entity, standPower, false);
            if (entity.level.isClientSide() && entity instanceof PlayerEntity){
                ActionsOverlayGui.getInstance().onStandSummon();
            }
        }
        TuskCapability tuskCap = entity.getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        if (tuskCap != null && !entity.level.isClientSide()) {
            if (nailCount < 10) {
                nailRegenTimer --;
            }
            if (nailRegenTimer <= 0) {
                setNailCount(Math.min(getNailCount() + 1, 10));
                nailRegenTimer = (act + 1) * 100;
            }
        }
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
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
        }
    }

    public void addToNailWormholes(WormholeEntity entity){
        this.wormhole = entity;
    }
    

    public void syncWithClient(ServerPlayerEntity entityAsPlayer) {
        AddonPackets.sendToClient(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entityAsPlayer);
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
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
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
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
        }
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        IStandPower standPower = IStandPower.getStandPowerOptional(entity).orElse(null);
        if (standPower != null && !standPower.isActive()) {
            this.setPrevAct(this.act);
            this.act = act;
            if (!entity.level.isClientSide()) {
                AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
            }
        }
        else {
            this.nextAct = act;
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("NextAct", nextAct);
        nbt.putInt("Act", getAct());
        nbt.putInt("PrevAct", getPrevAct());
        nbt.putInt("NailCount", nailCount);
        nbt.putInt("NailRegenTimer", nailRegenTimer);
        nbt.putBoolean("hasInfiniteRotation", hasInfiniteRotationCharge);
        nbt.putInt("NailWormholeId", wormhole != null ? wormhole.getId() : -1);
        nbt.putDouble("InfRotPosX", getInfiniteRotationPos().x());
        nbt.putDouble("InfRotPosY", getInfiniteRotationPos().y());
        nbt.putDouble("InfRotPosZ", getInfiniteRotationPos().z());
        return nbt;
    }



    public void fromNBT(CompoundNBT nbt) {
        this.nextAct = nbt.getInt("NextAct");
        this.setAct(nbt.getInt("Act"));
        prevAct = (nbt.getInt("PrevAct"));
        nailRegenTimer = nbt.getInt("NailRegenTimer");
        this.setNailCount(nbt.getInt("NailCount"));
        wormhole = (WormholeEntity) entity.level.getEntity(nbt.getInt("NailWormholeId"));
        this.setHasInfiniteRotationCharge(nbt.getBoolean("hasInfiniteRotation"));
        this.setInfiniteRotationPos(new Vector3d(
                nbt.getDouble("InfRotPosX"),
                nbt.getDouble("InfRotPosY"),
                nbt.getDouble("InfRotPosZ")));
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
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
        }
    }
    public Vector3d getInfiniteRotationPos() {
        return infiniteRotationPos;
    }
    public void setInfiniteRotationPos(Vector3d infiniteRotationPos) {
        this.infiniteRotationPos = infiniteRotationPos;
    }
    public boolean isHasInfiniteRotationCharge() {
        return hasInfiniteRotationCharge;
    }
    public void setHasInfiniteRotationCharge(boolean hasInfiniteRotationCharge) {
        this.hasInfiniteRotationCharge = hasInfiniteRotationCharge;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new NailDataPacket(entity.getId(), nailCount, hasWormholeWithArm, hasWormhole, chargedShot, act, hasInfiniteRotationCharge), entity);
        }
    }
    public boolean shouldChangeAct() {
        return shouldChangeAct;
    }
    public void setShouldChangeAct(boolean shouldChangeAct) {
        this.shouldChangeAct = shouldChangeAct;
    }
}


