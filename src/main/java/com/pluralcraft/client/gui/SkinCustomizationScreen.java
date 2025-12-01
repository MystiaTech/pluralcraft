package com.pluralcraft.client.gui;

import com.pluralcraft.client.skin.SkinManager;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

/**
 * Screen for customizing an alter's skin
 * Supports both URL-based skins and file upload
 */
public class SkinCustomizationScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;

    private EditBox skinURLField;
    private Button applyURLButton;
    private Button uploadFileButton;
    private Button resetToDefaultButton;

    // Preview
    private int modelX;
    private int modelY;
    private float modelRotation = 0f;

    public SkinCustomizationScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Skin Customization: " + alter.getName()));
        this.parentScreen = parentScreen;
        this.alter = alter;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int leftX = 30;
        int startY = 60;
        int spacing = 30;

        // Right side: Model preview
        this.modelX = this.width - 80;
        this.modelY = this.height / 2 + 30;

        // Skin URL input field
        this.skinURLField = new EditBox(this.font, leftX, startY, 300, 20,
                Component.literal("Skin URL"));
        this.skinURLField.setHint(Component.literal("Enter skin URL (e.g., from NameMC)"));
        this.skinURLField.setValue(alter.getSkinURL());
        this.skinURLField.setMaxLength(500);
        this.addRenderableWidget(skinURLField);

        startY += spacing;

        // Apply URL button
        this.applyURLButton = this.addRenderableWidget(Button.builder(
                Component.literal("✓ Apply Skin from URL"),
                button -> {
                    String url = skinURLField.getValue().trim();
                    if (!url.isEmpty()) {
                        alter.setSkinURL(url);
                        SystemDataManager.saveData();
                        minecraft.player.sendSystemMessage(
                                Component.literal("Skin URL saved! It will apply when you switch to this alter."));
                    } else {
                        minecraft.player.sendSystemMessage(
                                Component.literal("Please enter a valid URL!"));
                    }
                }
        ).bounds(leftX, startY, 200, 20).build());

        startY += spacing;

        // Upload file button
        this.uploadFileButton = this.addRenderableWidget(Button.builder(
                Component.literal("📁 Upload Skin File (.png)"),
                button -> {
                    openFileChooser();
                }
        ).bounds(leftX, startY, 200, 20).build());

        startY += spacing;

        // Reset to default (player's original skin)
        this.resetToDefaultButton = this.addRenderableWidget(Button.builder(
                Component.literal("↺ Reset to Default"),
                button -> {
                    alter.setSkinURL("");
                    skinURLField.setValue("");
                    SystemDataManager.saveData();
                    minecraft.player.sendSystemMessage(
                            Component.literal("Skin reset to default!"));
                }
        ).bounds(leftX, startY, 200, 20).build());

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> {
                    SystemDataManager.saveData();
                    this.onClose();
                }
        ).bounds(leftX, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw subtitle
        graphics.drawCenteredString(this.font,
                "Customize skin for: " + alter.getName(),
                this.width / 2, 35, 0xAAAAAA);

        // Draw instructions
        graphics.drawString(this.font, "Skin URL:", 30, 50, 0xFFFFFF);

        // Draw help text
        graphics.drawString(this.font,
                "Tip: Get skin URLs from NameMC, Minecraft Heads, or texture sites",
                30, this.height - 70, 0xAAAA00);

        // Draw current status
        if (!alter.getSkinURL().isEmpty()) {
            graphics.drawString(this.font,
                    "Current: Using custom skin",
                    30, this.height - 55, 0x55FF55);
        } else {
            graphics.drawString(this.font,
                    "Current: Using default skin",
                    30, this.height - 55, 0xAAAAAA);
        }

        // Draw model preview label
        graphics.drawString(this.font, "Preview:", this.width - 140, 60, 0xFFFFFF);

        // Draw player model preview
        if (minecraft != null && minecraft.player != null) {
            try {
                // Update rotation
                modelRotation += partialTick * 2.0f;

                // Draw the player model
                InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    this.modelX,
                    this.modelY,
                    40, // size
                    (float)(this.modelX - mouseX),
                    (float)(this.modelY - 50 - mouseY),
                    minecraft.player
                );
            } catch (Exception e) {
                graphics.drawString(this.font, "Model", this.width - 110, this.height / 2, 0xAAAAAA);
            }
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void openFileChooser() {
        // Run file chooser in a separate thread to not block Minecraft
        new Thread(() -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Skin File (.png)");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
                    }

                    @Override
                    public String getDescription() {
                        return "PNG Images (*.png)";
                    }
                });

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Path skinPath = selectedFile.toPath();

                    // Load the skin on the main thread
                    Minecraft.getInstance().execute(() -> {
                        SkinManager.loadSkinFromFile(skinPath, alter);
                        minecraft.player.sendSystemMessage(
                                Component.literal("Skin loaded from file: " + selectedFile.getName()));
                    });
                }
            } catch (Exception e) {
                Minecraft.getInstance().execute(() -> {
                    minecraft.player.sendSystemMessage(
                            Component.literal("Error opening file chooser: " + e.getMessage()));
                });
            }
        }, "SkinFileChooser").start();
    }

    @Override
    public void onClose() {
        SystemDataManager.saveData();
        Minecraft.getInstance().setScreen(parentScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
