package com.pluralcraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.pluralcraft.data.ServerFrontingTracker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Command to check who's fronting for a player
 * Usage: /pluralcraft whosfronting <player>
 */
public class WhosFrontingCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pluralcraft")
            .then(Commands.literal("whosfronting")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(WhosFrontingCommand::execute)
                )
            )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");
            ServerFrontingTracker.FrontingInfo info = ServerFrontingTracker.getFronting(targetPlayer.getUUID());

            if (info != null) {
                context.getSource().sendSuccess(() ->
                    Component.literal(targetPlayer.getName().getString() + " is fronting as: " +
                        info.alterEmoji + " " + info.alterName),
                    false
                );
            } else {
                context.getSource().sendSuccess(() ->
                    Component.literal(targetPlayer.getName().getString() + " hasn't set their fronting alter yet."),
                    false
                );
            }

            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
