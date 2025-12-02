package com.pluralcraft.items;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An ID card/passport item that displays system and alter information
 */
public class SystemIDItem extends Item {

    public SystemIDItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // Prevent using the item on blocks (like logs, trees, etc.)
        // Instead, only allow using it in the air
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            // Open GUI on client side
            SystemProfile profile = SystemDataManager.getProfile(player.getUUID());

            if (profile != null) {
                net.minecraft.client.Minecraft.getInstance().setScreen(
                    new com.pluralcraft.client.gui.SystemIDCardScreen(profile)
                );
            } else {
                player.sendSystemMessage(Component.literal("No system profile found!").withStyle(ChatFormatting.RED));
                player.sendSystemMessage(Component.literal("Use /pluralcraft system setname <name> to create one!").withStyle(ChatFormatting.GRAY));
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private void displayAlterInfo(Player player, AlterProfile alter) {
        player.sendSystemMessage(Component.literal("  Name: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(alter.getName()).withStyle(ChatFormatting.WHITE)));
        player.sendSystemMessage(Component.literal("  Age: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(alter.getAge())).withStyle(ChatFormatting.WHITE)));
        player.sendSystemMessage(Component.literal("  Pronouns: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(alter.getPronouns()).withStyle(ChatFormatting.WHITE)));

        if (!alter.getBio().isEmpty()) {
            player.sendSystemMessage(Component.literal("  Bio: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(alter.getBio()).withStyle(ChatFormatting.WHITE)));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to view your System ID").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Shows current alter and system info").withStyle(ChatFormatting.DARK_GRAY));
    }
}
