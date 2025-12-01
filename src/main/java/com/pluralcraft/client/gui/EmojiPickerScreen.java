package com.pluralcraft.client.gui;

import com.pluralcraft.emotes.EmojiCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * GUI for selecting and sending emojis in chat
 * Perfect for nonverbal alters!
 */
public class EmojiPickerScreen extends Screen {
    private EmojiCategory currentCategory = EmojiCategory.COMMON;
    private int scrollOffset = 0;
    private static final int EMOJIS_PER_ROW = 8;
    private static final int EMOJI_SIZE = 24;

    public EmojiPickerScreen() {
        super(Component.literal("Emoji Picker"));
    }

    @Override
    protected void init() {
        super.init();

        int startX = 10;
        int startY = 30;

        // Category buttons
        int catX = startX;
        for (EmojiCategory category : EmojiCategory.values()) {
            this.addRenderableWidget(Button.builder(
                    Component.literal(category.getDisplayName()),
                    button -> {
                        currentCategory = category;
                        scrollOffset = 0;
                    }
            ).bounds(catX, startY, 80, 20).build());
            catX += 85;
        }

        // Close button
        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> this.onClose()
        ).bounds(this.width - 60, 5, 55, 20).build());

        // Info text for nonverbal users
        this.addRenderableWidget(Button.builder(
                Component.literal("💬 Send to Chat"),
                button -> {
                    // Placeholder - shows how to use
                }
        ).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, "Emoji Picker - " + currentCategory.getDisplayName(),
                this.width / 2, 10, 0xFFFFFF);

        // Draw emojis in grid
        int startX = 20;
        int startY = 60;
        int emojiIndex = 0;

        for (String emoji : currentCategory.getEmojis()) {
            if (emojiIndex < scrollOffset) {
                emojiIndex++;
                continue;
            }

            int row = (emojiIndex - scrollOffset) / EMOJIS_PER_ROW;
            int col = (emojiIndex - scrollOffset) % EMOJIS_PER_ROW;

            int x = startX + (col * EMOJI_SIZE);
            int y = startY + (row * EMOJI_SIZE);

            // Don't render if off screen
            if (y > this.height - 50) break;

            // Draw emoji background
            graphics.fill(x - 2, y - 2, x + EMOJI_SIZE - 2, y + EMOJI_SIZE - 2, 0x80000000);

            // Check if mouse is hovering
            if (mouseX >= x - 2 && mouseX <= x + EMOJI_SIZE - 2 &&
                mouseY >= y - 2 && mouseY <= y + EMOJI_SIZE - 2) {
                graphics.fill(x - 2, y - 2, x + EMOJI_SIZE - 2, y + EMOJI_SIZE - 2, 0x80FFFFFF);
            }

            // Draw emoji (larger than normal text)
            graphics.drawString(this.font, emoji, x, y, 0xFFFFFF);

            emojiIndex++;
        }

        // Draw instructions
        graphics.drawCenteredString(this.font,
                "Click an emoji to copy it to chat!",
                this.width / 2, this.height - 50, 0xAAAAAA);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if clicked on an emoji
        int startX = 20;
        int startY = 60;
        int emojiIndex = 0;

        for (String emoji : currentCategory.getEmojis()) {
            if (emojiIndex < scrollOffset) {
                emojiIndex++;
                continue;
            }

            int row = (emojiIndex - scrollOffset) / EMOJIS_PER_ROW;
            int col = (emojiIndex - scrollOffset) % EMOJIS_PER_ROW;

            int x = startX + (col * EMOJI_SIZE);
            int y = startY + (row * EMOJI_SIZE);

            if (y > this.height - 50) break;

            if (mouseX >= x - 2 && mouseX <= x + EMOJI_SIZE - 2 &&
                mouseY >= y - 2 && mouseY <= y + EMOJI_SIZE - 2) {

                // Send emoji to chat!
                Minecraft.getInstance().player.connection.sendChat(emoji);
                this.onClose();
                return true;
            }

            emojiIndex++;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollOffset = Math.max(0, scrollOffset - (int)delta);
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
