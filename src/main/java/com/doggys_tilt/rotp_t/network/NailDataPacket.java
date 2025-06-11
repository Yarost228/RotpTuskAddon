package com.doggys_tilt.rotp_t.network;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
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
    private final boolean hasWormhole;
    private final boolean chargedShot;

    public NailDataPacket(int entityId, int nailCount, boolean hasWormhole, boolean chargedShot){
        this.entityId = entityId;
        this.nailCount = nailCount;
        this.hasWormhole = hasWormhole;
        this.chargedShot = chargedShot;
    }

    public static class Handler implements IModPacketHandler<NailDataPacket> {
        public void encode(NailDataPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.nailCount);
            buf.writeBoolean(msg.hasWormhole);
            buf.writeBoolean(msg.chargedShot);
        }
        @Override
        public NailDataPacket decode(PacketBuffer buf) {
            return new NailDataPacket(buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean());
        }
        @Override
        public void handle(NailDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                entity.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    nailCapability.setNailCount(msg.nailCount);
                    nailCapability.setWormhole(msg.hasWormhole);
                    nailCapability.setChargedShot(msg.chargedShot);
                });
            }
        }
        @Override
        public Class<NailDataPacket> getPacketClass() {
            return NailDataPacket.class;
        }
    }
}
