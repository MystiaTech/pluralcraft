package com.pluralcraft.network;

import com.pluralcraft.PluralCraft;
import com.pluralcraft.network.packets.SyncFrontingAlterPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Handles network communication for PluralCraft
 * Syncs fronting alter info between client and server
 */
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;

    /**
     * Register all network packets
     * MUST be called during FMLCommonSetupEvent!
     */
    public static void register() {
        // Initialize the channel HERE, not as a static field!
        INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PluralCraft.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
        );

        INSTANCE.messageBuilder(SyncFrontingAlterPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(SyncFrontingAlterPacket::encode)
            .decoder(SyncFrontingAlterPacket::decode)
            .consumerMainThread(SyncFrontingAlterPacket::handle)
            .add();

        PluralCraft.LOGGER.info("PluralCraft network packets registered!");
    }
}
