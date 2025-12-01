package com.pluralcraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pluralcraft.PluralCraft;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Commands for managing alters without GUI
 * Useful for testing and server-only setups
 */
@Mod.EventBusSubscriber(modid = PluralCraft.MOD_ID)
public class PluralCraftCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // Register with .requires() returning true = available to everyone, no OP needed!
        dispatcher.register(Commands.literal("pluralcraft")
                .requires(source -> true) // Everyone can use these commands!
                .then(Commands.literal("system")
                        .then(Commands.literal("setname")
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            String systemName = StringArgumentType.getString(context, "name");

                                            SystemProfile profile = SystemDataManager.getOrCreateProfile(
                                                    player.getUUID(),
                                                    systemName
                                            );
                                            profile.setSystemName(systemName);
                                            SystemDataManager.saveData();

                                            player.sendSystemMessage(Component.literal("System name set to: " + systemName));
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("alter")
                        .then(Commands.literal("add")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .then(Commands.argument("age", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("pronouns", StringArgumentType.string())
                                                        .executes(context -> {
                                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                                            String name = StringArgumentType.getString(context, "name");
                                                            int age = IntegerArgumentType.getInteger(context, "age");
                                                            String pronouns = StringArgumentType.getString(context, "pronouns");

                                                            SystemProfile profile = SystemDataManager.getOrCreateProfile(
                                                                    player.getUUID(),
                                                                    player.getName().getString() + "'s System"
                                                            );

                                                            AlterProfile alter = new AlterProfile(name, age, pronouns, "");
                                                            profile.addAlter(alter);
                                                            SystemDataManager.saveData();

                                                            player.sendSystemMessage(Component.literal("Added alter: " + name + " (" + pronouns + ")"));
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("switch")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            String alterName = StringArgumentType.getString(context, "name");

                                            SystemProfile profile = SystemDataManager.getProfile(player.getUUID());
                                            if (profile != null) {
                                                if (profile.switchToAlter(alterName)) {
                                                    SystemDataManager.saveData();
                                                    player.sendSystemMessage(Component.literal("Switched to alter: " + alterName));
                                                    return 1;
                                                } else {
                                                    player.sendSystemMessage(Component.literal("Alter not found: " + alterName));
                                                    return 0;
                                                }
                                            } else {
                                                player.sendSystemMessage(Component.literal("No system profile found!"));
                                                return 0;
                                            }
                                        })
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    SystemProfile profile = SystemDataManager.getProfile(player.getUUID());

                                    if (profile != null && !profile.getAlters().isEmpty()) {
                                        player.sendSystemMessage(Component.literal("Alters in " + profile.getSystemName() + ":"));
                                        for (int i = 0; i < profile.getAlters().size(); i++) {
                                            AlterProfile alter = profile.getAlters().get(i);
                                            String current = (i == profile.getCurrentAlterIndex()) ? " (current)" : "";
                                            player.sendSystemMessage(Component.literal("  - " + alter.getName() + " (" + alter.getPronouns() + ")" + current));
                                        }
                                    } else {
                                        player.sendSystemMessage(Component.literal("No alters found!"));
                                    }
                                    return 1;
                                })
                        )
                )
        );

        PluralCraft.LOGGER.info("Registered PluralCraft commands!");
    }
}
