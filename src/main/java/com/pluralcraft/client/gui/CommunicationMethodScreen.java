package com.pluralcraft.client.gui;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.CommunicationMethod;
import com.pluralcraft.data.SystemDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Screen for selecting an alter's communication method
 */
public class CommunicationMethodScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;
    private CommunicationMethod selectedMethod;

    public CommunicationMethodScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Communication Method: " + alter.getName()));
        this.parentScreen = parentScreen;
        this.alter = alter;
        this.selectedMethod = alter.getCommunicationMethod();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 60;
        int buttonWidth = 300;
        int buttonHeight = 30;
        int spacing = 35;

        // Create a button for each communication method
        for (CommunicationMethod method : CommunicationMethod.values()) {
            boolean isSelected = (method == selectedMethod);

            this.addRenderableWidget(Button.builder(
                    Component.literal((isSelected ? "→ " : "  ") + method.getDisplayName()),
                    button -> {
                        selectedMethod = method;
                        this.rebuildWidgets(); // Refresh to update selection markers
                    }
            ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

            startY += spacing;
        }

        // Save button
        this.addRenderableWidget(Button.builder(
                Component.literal("✓ Save"),
                button -> {
                    alter.setCommunicationMethod(selectedMethod);
                    SystemDataManager.saveData();
                    minecraft.player.sendSystemMessage(
                            Component.literal("Communication method set to: " + selectedMethod.getDisplayName()));
                    this.onClose();
                }
        ).bounds(centerX - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw subtitle
        graphics.drawCenteredString(this.font,
                "Select how " + alter.getName() + " communicates:",
                this.width / 2, 35, 0xAAAAAA);

        // Draw description for selected method
        if (selectedMethod != null) {
            graphics.drawCenteredString(this.font,
                    selectedMethod.getDescription(),
                    this.width / 2, this.height - 65, 0xAAAA00);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
