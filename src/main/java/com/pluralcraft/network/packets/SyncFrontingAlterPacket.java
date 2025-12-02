package com.pluralcraft.network.packets;

import com.pluralcraft.data.ServerFrontingTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Packet sent from client to server when a player switches their fronting alter
 * Server stores this info so other players can query it
 */
public class SyncFrontingAlterPacket {
    private final String alterName;
    private final String alterEmoji;

    public SyncFrontingAlterPacket(String alterName, String alterEmoji) {
        this.alterName = alterName;
        this.alterEmoji = alterEmoji;
    }

    /**
     * Encode packet to bytes
     */
    public static void encode(SyncFrontingAlterPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.alterName);
        buf.writeUtf(packet.alterEmoji);
    }

    /**
     * Decode packet from bytes
     */
    public static SyncFrontingAlterPacket decode(FriendlyByteBuf buf) {
        String alterName = buf.readUtf();
        String alterEmoji = buf.readUtf();
        return new SyncFrontingAlterPacket(alterName, alterEmoji);
    }

    /**
     * Handle packet on server
     */
    public static void handle(SyncFrontingAlterPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Get the server player who sent this packet
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                // Update server-side tracking
                ServerFrontingTracker.setFronting(player.getUUID(), packet.alterName, packet.alterEmoji);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
