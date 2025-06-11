package com.rotpaddon.exampleaddon.capability;

import com.rotpaddon.exampleaddon.network.AddonPackets;
import com.rotpaddon.exampleaddon.network.NailDataPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class NailCapability {
    private final LivingEntity entity;
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
        if (entity instanceof ServerPlayerEntity){
            syncWithEntityOnly((ServerPlayerEntity) entity);
        }
    }
    public void syncWithEntityOnly(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new NailDataPacket(entity.getId(), nailCount, hasWormhole, chargedShot), player);
    }
    public boolean hasWormhole() {
        return hasWormhole;
    }

    public void setWormhole(boolean hasWormhole) {
        this.hasWormhole = hasWormhole;
        if (entity instanceof ServerPlayerEntity){
            syncWithEntityOnly((ServerPlayerEntity) entity);
        }
    }
    public boolean chargedShot() {
        return chargedShot;
    }
    public void setChargedShot(boolean chargedShot) {
        this.chargedShot = chargedShot;
        if (entity instanceof ServerPlayerEntity){
            syncWithEntityOnly((ServerPlayerEntity) entity);
        }
    }
}


