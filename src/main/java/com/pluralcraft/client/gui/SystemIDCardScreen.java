package com.pluralcraft.client.gui;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

/**
 * A visual ID card screen showing system and current alter information
 * Looks like an actual ID card!
 */
public class SystemIDCardScreen extends Screen {
    private final SystemProfile profile;

    public SystemIDCardScreen(SystemProfile profile) {
        super(Component.literal("System ID Card"));
        this.profile = profile;
    }

    @Override
    protected void init() {
        super.init();

        // Close button
        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> this.onClose()
        ).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int cardWidth = 400;
        int cardHeight = 280;
        int cardX = (this.width - cardWidth) / 2;
        int cardY = (this.height - cardHeight) / 2 - 20;

        // Draw card background (like a physical ID card)
        graphics.fill(cardX, cardY, cardX + cardWidth, cardY + cardHeight, 0xFF2C2C2C);

        // Draw card border
        graphics.fill(cardX - 2, cardY - 2, cardX + cardWidth + 2, cardY - 2, 0xFF00AAFF); // Top
        graphics.fill(cardX - 2, cardY + cardHeight, cardX + cardWidth + 2, cardY + cardHeight + 2, 0xFF00AAFF); // Bottom
        graphics.fill(cardX - 2, cardY, cardX, cardY + cardHeight, 0xFF00AAFF); // Left
        graphics.fill(cardX + cardWidth, cardY, cardX + cardWidth + 2, cardY + cardHeight, 0xFF00AAFF); // Right

        // Draw header bar
        graphics.fill(cardX, cardY, cardX + cardWidth, cardY + 35, 0xFF1A1A1A);

        // Title
        graphics.drawCenteredString(this.font, "SYSTEM ID CARD", this.width / 2, cardY + 12, 0xFFFFFF);

        int leftX = cardX + 20;
        int rightX = cardX + cardWidth - 150;
        int textY = cardY + 50;
        int lineHeight = 16;

        // System Information (Left side)
        graphics.drawString(this.font, "SYSTEM NAME", leftX, textY, 0xAAAA00);
        graphics.drawString(this.font, profile.getSystemName(), leftX, textY + 10, 0xFFFFFF);

        textY += 40;

        // Current Alter Info
        AlterProfile currentAlter = profile.getCurrentAlter();
        if (currentAlter != null) {
            graphics.drawString(this.font, "CURRENTLY FRONTING", leftX, textY, 0xAAAA00);
            textY += 12;

            graphics.drawString(this.font, "Name: " + currentAlter.getName(), leftX, textY, 0xFFFFFF);
            textY += lineHeight;

            graphics.drawString(this.font, "Age: " + currentAlter.getAge(), leftX, textY, 0xFFFFFF);
            textY += lineHeight;

            graphics.drawString(this.font, "Pronouns: " + currentAlter.getPronouns(), leftX, textY, 0xFFFFFF);
            textY += lineHeight;

            graphics.drawString(this.font, "Communication: " + currentAlter.getCommunicationMethod().getDisplayName(),
                    leftX, textY, 0xFFFFFF);
            textY += lineHeight;

            if (!currentAlter.getBio().isEmpty()) {
                graphics.drawString(this.font, "Bio:", leftX, textY, 0xAAAA00);
                textY += 10;

                // Wrap bio text if too long
                String bio = currentAlter.getBio();
                if (bio.length() > 35) {
                    bio = bio.substring(0, 32) + "...";
                }
                graphics.drawString(this.font, bio, leftX, textY, 0xAAAAAA);
            }
        }

        // Player model on the right side (like a photo on an ID)
        if (minecraft != null && minecraft.player != null) {
            try {
                InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    rightX + 65,
                    cardY + 160,
                    50, // size
                    (float)(rightX + 65 - mouseX),
                    (float)(cardY + 100 - mouseY),
                    minecraft.player
                );
            } catch (Exception e) {
                graphics.drawString(this.font, "Photo", rightX + 40, cardY + 100, 0xAAAAAA);
            }
        }

        // System statistics (bottom right)
        textY = cardY + cardHeight - 70;
        graphics.drawString(this.font, "SYSTEM INFO", rightX, textY, 0xAAAA00);
        textY += 12;
        graphics.drawString(this.font, "Total Alters: " + profile.getAlters().size(), rightX, textY, 0xAAAAAA);
        textY += lineHeight;

        // List alter names
        graphics.drawString(this.font, "Members:", rightX, textY, 0xAAAA00);
        textY += 10;
        int count = 0;
        for (AlterProfile alter : profile.getAlters()) {
            if (count >= 3) {
                graphics.drawString(this.font, "...and " + (profile.getAlters().size() - 3) + " more",
                        rightX, textY, 0xAAAAAA);
                break;
            }
            String marker = (alter == currentAlter) ? "→ " : "  ";
            graphics.drawString(this.font, marker + alter.getName(), rightX, textY, 0xAAAAAA);
            textY += 12;
            count++;
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
