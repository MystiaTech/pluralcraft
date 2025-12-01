package com.pluralcraft.client.gui;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Main GUI screen for managing alters
 */
public class AlterManagementScreen extends Screen {
    private final Screen lastScreen;
    private SystemProfile systemProfile;

    public AlterManagementScreen(Screen lastScreen) {
        super(Component.literal("Alter Management"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();

        // Get or create system profile for the player
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            this.systemProfile = SystemDataManager.getOrCreateProfile(
                    mc.player.getUUID(),
                    mc.player.getName().getString() + "'s System"
            );
        }

        // Add buttons
        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;
        int startY = 50;

        // System name display/edit button
        this.addRenderableWidget(Button.builder(
                Component.literal("System: " + (systemProfile != null ? systemProfile.getSystemName() : "Unknown")),
                button -> {
                    if (systemProfile != null) {
                        mc.setScreen(new EditSystemNameScreen(this, systemProfile));
                    }
                }
        ).bounds(centerX, startY, buttonWidth, buttonHeight).build());

        // Current alter display
        if (systemProfile != null && systemProfile.getCurrentAlter() != null) {
            this.addRenderableWidget(Button.builder(
                    Component.literal("Current: " + systemProfile.getCurrentAlter().getName()),
                    button -> {
                        // TODO: Open alter details screen
                    }
            ).bounds(centerX, startY + 30, buttonWidth, buttonHeight).build());
        }

        // Add new alter button
        this.addRenderableWidget(Button.builder(
                Component.literal("➕ Add New Alter"),
                button -> {
                    mc.setScreen(new AddAlterScreen(this, systemProfile));
                }
        ).bounds(centerX, startY + 60, buttonWidth, buttonHeight).build());

        // Switch alter button
        if (systemProfile != null && !systemProfile.getAlters().isEmpty()) {
            this.addRenderableWidget(Button.builder(
                    Component.literal("🔄 Switch Alter"),
                    button -> {
                        // Quick switch to next alter
                        systemProfile.switchToNextAlter();
                        SystemDataManager.saveData();
                        mc.player.sendSystemMessage(Component.literal("Switched to: " + systemProfile.getCurrentAlter().getName()));
                        this.init(mc, this.width, this.height); // Refresh screen
                    }
            ).bounds(centerX, startY + 90, buttonWidth, buttonHeight).build());
        }

        // Add clickable buttons for each alter
        if (systemProfile != null && !systemProfile.getAlters().isEmpty()) {
            int alterListY = 145;
            for (int i = 0; i < systemProfile.getAlters().size(); i++) {
                final AlterProfile alter = systemProfile.getAlters().get(i);
                boolean isCurrent = (i == systemProfile.getCurrentAlterIndex());

                String prefix = isCurrent ? "→ " : "  ";
                String buttonText = prefix + alter.getName() + " (" + alter.getPronouns() + ")";

                this.addRenderableWidget(Button.builder(
                        Component.literal(buttonText),
                        button -> {
                            // Open edit screen for this alter
                            mc.setScreen(new EditAlterScreen(this, alter));
                        }
                ).bounds(20, alterListY, 200, 18).build());

                alterListY += 20;
            }
        }

        // Close button
        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> this.onClose()
        ).bounds(centerX, this.height - 30, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw "Alters:" label if there are alters
        if (systemProfile != null && !systemProfile.getAlters().isEmpty()) {
            graphics.drawString(this.font, "Alters (click to edit):", 20, 130, 0xFFFFFF);
        } else if (systemProfile != null) {
            graphics.drawCenteredString(this.font, "No alters yet! Add one to get started!", this.width / 2, 150, 0xAAAAAA);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(lastScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Don't pause the game
    }
}
