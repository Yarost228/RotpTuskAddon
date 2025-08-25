package com.doggys_tilt.rotp_t.network;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.WormholeEntity;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NailDataPacket {
    private final int entityId;
    private final int nailCount;
    private final boolean hasWormholeWithArm;
    private final boolean hasWormhole;
    private final boolean chargedShot;
    private final int act;
    private final int wormholeEntityId;
    private final boolean hasInfiniteRotationCharge;

    public NailDataPacket(TuskCapability cap, int entityId){
        this.entityId = entityId;
        this.nailCount = cap.getNailCount();
        this.hasWormholeWithArm = cap.hasWormholeWithArm();
        this.hasWormhole = cap.hasWormhole();
        this.chargedShot = cap.chargedShot();
        this.act = cap.getAct();
        this.wormholeEntityId = cap.getWormhole() != null ? cap.getWormhole().getId() : -1;
        this.hasInfiniteRotationCharge = cap.isHasInfiniteRotationCharge();
    }

    public NailDataPacket(int entityId, int nailCount, boolean hasWormholeWithArm, boolean hasWormhole, boolean chargedShot, int act, int wormholeEntityId, boolean hasInfiniteRotationCharge){
        this.entityId = entityId;
        this.nailCount = nailCount;
        this.hasWormholeWithArm = hasWormholeWithArm;
        this.hasWormhole = hasWormhole;
        this.chargedShot = chargedShot;
        this.act = act;
        this.wormholeEntityId = wormholeEntityId;
        this.hasInfiniteRotationCharge = hasInfiniteRotationCharge;
    }

    public static class Handler implements IModPacketHandler<NailDataPacket> {
        public void encode(NailDataPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.nailCount);
            buf.writeBoolean(msg.hasWormholeWithArm);
            buf.writeBoolean(msg.hasWormhole);
            buf.writeBoolean(msg.chargedShot);
            buf.writeInt(msg.act);
            buf.writeInt(msg.wormholeEntityId);
            buf.writeBoolean(msg.hasInfiniteRotationCharge);
        }
        @Override
        public NailDataPacket decode(PacketBuffer buf) {
            return new NailDataPacket(buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readBoolean());
        }
        @Override
        public void handle(NailDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                entity.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    nailCapability.setNailCount(msg.nailCount);
                    nailCapability.hasWormholeWithArm(msg.hasWormholeWithArm);
                    nailCapability.setHasWormholePortal(msg.hasWormhole);
                    nailCapability.setChargedShot(msg.chargedShot);
                    nailCapability.setAct(msg.act);
                    nailCapability.setWormhole((WormholeEntity) ClientUtil.getEntityById(msg.wormholeEntityId));
                    nailCapability.setHasInfiniteRotationCharge(msg.hasInfiniteRotationCharge);
                });
            }
        }
        @Override
        public Class<NailDataPacket> getPacketClass() {
            return NailDataPacket.class;
        }
    }
}
