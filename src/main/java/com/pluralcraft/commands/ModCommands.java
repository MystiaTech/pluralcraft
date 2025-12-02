package com.pluralcraft.commands;

import com.pluralcraft.PluralCraft;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers all PluralCraft commands
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        WhosFrontingCommand.register(event.getDispatcher());
        PluralCraft.LOGGER.info("PluralCraft commands registered!");
    }
}
