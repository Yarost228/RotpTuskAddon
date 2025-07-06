package com.doggys_tilt.rotp_t.network;

import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SActSyncPacket {
    private final int entityId;
    private final int act;

    public SActSyncPacket(int entityId, int act){
        this.entityId = entityId;
        this.act = act;
    }

    public static class Handler implements IModPacketHandler<SActSyncPacket> {
        public void encode(SActSyncPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.act);
        }
        @Override
        public SActSyncPacket decode(PacketBuffer buf) {
            return new SActSyncPacket(buf.readInt(), buf.readInt());
        }
        @Override
        public void handle(SActSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity player = ctx.get().getSender();
            player.getCapability(TuskCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                IStandPower power = IStandPower.getPlayerStandPower(player);
                power.getType().unsummon(player, power);
                nailCapability.setShouldChangeAct(true);
                nailCapability.setAct(msg.act);
            });
        }
        @Override
        public Class<SActSyncPacket> getPacketClass() {
            return SActSyncPacket.class;
        }
    }
}
