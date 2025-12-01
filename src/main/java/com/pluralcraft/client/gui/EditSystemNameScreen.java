package com.pluralcraft.client.gui;

import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Screen for editing the system name
 */
public class EditSystemNameScreen extends Screen {
    private final Screen parentScreen;
    private final SystemProfile profile;
    private EditBox nameField;

    public EditSystemNameScreen(Screen parentScreen, SystemProfile profile) {
        super(Component.literal("Edit System Name"));
        this.parentScreen = parentScreen;
        this.profile = profile;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;

        // System name field
        this.nameField = new EditBox(this.font, centerX - 150, 80, 300, 20,
                Component.literal("System Name"));
        this.nameField.setValue(profile.getSystemName());
        this.nameField.setMaxLength(50);
        this.addRenderableWidget(nameField);

        // Save button
        this.addRenderableWidget(Button.builder(
                Component.literal("✓ Save"),
                button -> {
                    String newName = nameField.getValue().trim();
                    if (!newName.isEmpty()) {
                        profile.setSystemName(newName);
                        SystemDataManager.saveData();
                        minecraft.player.sendSystemMessage(
                                Component.literal("System name updated to: " + newName));
                        this.onClose();
                    } else {
                        minecraft.player.sendSystemMessage(
                                Component.literal("System name cannot be empty!"));
                    }
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
                "Enter your new system name:",
                this.width / 2, 50, 0xAAAAAA);

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
