package com.pluralcraft;

import com.pluralcraft.integration.ModIntegration;
import com.pluralcraft.items.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PluralCraft - A mod for DID systems to express themselves in Minecraft!
 *
 * This mod works on BOTH client and server sides:
 * - Client: GUI, skin switching, visual elements
 * - Server: Chat formatting, data sync
 * - Can work with just client OR just server OR both!
 */
@Mod(PluralCraft.MOD_ID)
public class PluralCraft {
    public static final String MOD_ID = "pluralcraft";
    public static final Logger LOGGER = LogManager.getLogger();

    public PluralCraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register items
        ModItems.register(modEventBus);

        // Common setup (runs on both sides)
        modEventBus.addListener(this::commonSetup);

        // Client setup (only runs on client)
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::clientSetup);
        }

        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("PluralCraft is loading! Ready to help systems express themselves! 💜");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("PluralCraft common setup - works on both client and server!");

        // Initialize mod integrations (Wildfire's Gender Mod, etc.)
        event.enqueueWork(() -> {
            ModIntegration.initializeIntegrations();

            // Register network packets
            com.pluralcraft.network.NetworkHandler.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("PluralCraft client setup - GUI and visual stuff!");
        // This is where we'll set up keybindings, GUI, skin handling, etc.

        event.enqueueWork(() -> {
            // Client-specific integration setup will go here
            LOGGER.info("Client-side mod integrations ready!");
        });
    }
}
