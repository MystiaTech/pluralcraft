package com.pluralcraft.client.gui;

import com.pluralcraft.compat.WildfireCompat;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Screen for editing an existing alter
 */
public class EditAlterScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;

    public EditAlterScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Edit Alter: " + alter.getName()));
        this.parentScreen = parentScreen;
        this.alter = alter;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 60;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 30;

        // Customize Body button - ALWAYS show ours!
        this.addRenderableWidget(Button.builder(
                Component.literal(WildfireCompat.getBodyCustomizationButtonText()),
                button -> {
                    Minecraft.getInstance().setScreen(new BodyCustomizationScreen(this, alter));
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        startY += spacing;

        // If Wildfire is loaded, show their button too!
        if (WildfireCompat.shouldShowWildfireButton()) {
            this.addRenderableWidget(Button.builder(
                    Component.literal(WildfireCompat.getWildfireButtonText()),
                    button -> {
                        if (!WildfireCompat.tryOpenWildfireGUI()) {
                            Minecraft.getInstance().player.sendSystemMessage(
                                    Component.literal(WildfireCompat.getWildfireGUIMessage()));
                        }
                    }
            ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

            startY += spacing;
        }

        // Customize Skin button
        this.addRenderableWidget(Button.builder(
                Component.literal("🎨 Customize Skin"),
                button -> {
                    Minecraft.getInstance().setScreen(new SkinCustomizationScreen(this, alter));
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        startY += spacing;

        // Edit Details button
        this.addRenderableWidget(Button.builder(
                Component.literal("✏ Edit Details"),
                button -> {
                    Minecraft.getInstance().setScreen(new EditDetailsScreen(this, alter));
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        startY += spacing;

        // Communication Method button
        this.addRenderableWidget(Button.builder(
                Component.literal("💬 Communication: " + alter.getCommunicationMethod().getDisplayName()),
                button -> {
                    Minecraft.getInstance().setScreen(new CommunicationMethodScreen(this, alter));
                }
        ).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> {
                    SystemDataManager.saveData();
                    this.onClose();
                }
        ).bounds(centerX - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw alter info
        graphics.drawCenteredString(this.font,
                alter.getName() + " (" + alter.getAge() + ", " + alter.getPronouns() + ")",
                this.width / 2, 40, 0xAAAAAA);

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
