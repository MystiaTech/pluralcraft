package com.pluralcraft.client.gui;

import com.pluralcraft.compat.WildfireCompat;
import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import com.pluralcraft.data.SystemProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Screen for adding a new alter to the system
 */
public class AddAlterScreen extends Screen {
    private final Screen parentScreen;
    private final SystemProfile systemProfile;

    private EditBox nameField;
    private EditBox ageField;
    private EditBox pronounsField;
    private EditBox bioField;

    // Temporary alter for body customization before creation
    private AlterProfile tempAlter;

    public AddAlterScreen(Screen parentScreen, SystemProfile systemProfile) {
        super(Component.literal("Add New Alter"));
        this.parentScreen = parentScreen;
        this.systemProfile = systemProfile;
        // Create temporary alter for customization
        this.tempAlter = new AlterProfile("", 0, "", "");
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = 40;
        int fieldWidth = 200;
        int fieldHeight = 20;

        // Name field
        nameField = new EditBox(this.font, centerX - 100, startY, fieldWidth, fieldHeight,
                Component.literal("Alter Name"));
        nameField.setHint(Component.literal("Alter Name"));
        nameField.setMaxLength(50);
        this.addRenderableWidget(nameField);

        // Age field
        ageField = new EditBox(this.font, centerX - 100, startY + 30, fieldWidth, fieldHeight,
                Component.literal("Age"));
        ageField.setHint(Component.literal("Age"));
        ageField.setMaxLength(3);
        this.addRenderableWidget(ageField);

        // Pronouns field
        pronounsField = new EditBox(this.font, centerX - 100, startY + 60, fieldWidth, fieldHeight,
                Component.literal("Pronouns"));
        pronounsField.setHint(Component.literal("Pronouns (e.g., she/her)"));
        pronounsField.setMaxLength(50);
        this.addRenderableWidget(pronounsField);

        // Bio field (optional)
        bioField = new EditBox(this.font, centerX - 100, startY + 90, fieldWidth, fieldHeight,
                Component.literal("Bio (Optional)"));
        bioField.setHint(Component.literal("Bio (Optional)"));
        bioField.setMaxLength(200);
        this.addRenderableWidget(bioField);

        // Body customization button - ALWAYS show ours!
        this.addRenderableWidget(Button.builder(
                Component.literal(WildfireCompat.getBodyCustomizationButtonText()),
                button -> {
                    // Open our body customization screen
                    Minecraft.getInstance().setScreen(new BodyCustomizationScreen(this, tempAlter));
                }
        ).bounds(centerX - 100, startY + 120, 200, 20).build());

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
            ).bounds(centerX - 100, startY + 145, 200, 20).build());
        }

        // Customize Skin button (for temp alter before creation)
        this.addRenderableWidget(Button.builder(
                Component.literal("🎨 Customize Skin"),
                button -> {
                    Minecraft.getInstance().setScreen(new SkinCustomizationScreen(this, tempAlter));
                }
        ).bounds(centerX - 100, startY + 175, 200, 20).build());

        // Create button
        this.addRenderableWidget(Button.builder(
                Component.literal("✓ Create Alter"),
                button -> {
                    String name = nameField.getValue().trim();
                    String ageStr = ageField.getValue().trim();
                    String pronouns = pronounsField.getValue().trim();
                    String bio = bioField.getValue().trim();

                    // Validate
                    if (name.isEmpty()) {
                        Minecraft.getInstance().player.sendSystemMessage(
                                Component.literal("Please enter a name!"));
                        return;
                    }

                    int age = 0;
                    try {
                        age = Integer.parseInt(ageStr);
                        if (age < 0 || age > 999) {
                            Minecraft.getInstance().player.sendSystemMessage(
                                    Component.literal("Age must be between 0 and 999!"));
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Minecraft.getInstance().player.sendSystemMessage(
                                Component.literal("Please enter a valid age!"));
                        return;
                    }

                    if (pronouns.isEmpty()) {
                        Minecraft.getInstance().player.sendSystemMessage(
                                Component.literal("Please enter pronouns!"));
                        return;
                    }

                    // Create the alter and copy body customization from temp!
                    AlterProfile newAlter = new AlterProfile(name, age, pronouns, bio);
                    newAlter.setBodyCustomization(tempAlter.getBodyCustomization().copy());
                    systemProfile.addAlter(newAlter);
                    SystemDataManager.saveData();

                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Created alter: " + name + " (" + pronouns + ")"));

                    // Go back to main screen
                    this.onClose();
                }
        ).bounds(centerX - 100, startY + 175, 95, 20).build());

        // Cancel button
        this.addRenderableWidget(Button.builder(
                Component.literal("✗ Cancel"),
                button -> this.onClose()
        ).bounds(centerX + 5, startY + 175, 95, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw field labels
        graphics.drawString(this.font, "Name:", this.width / 2 - 100, 30, 0xAAAAAA);
        graphics.drawString(this.font, "Age:", this.width / 2 - 100, 60, 0xAAAAAA);
        graphics.drawString(this.font, "Pronouns:", this.width / 2 - 100, 90, 0xAAAAAA);
        graphics.drawString(this.font, "Bio:", this.width / 2 - 100, 120, 0xAAAAAA);

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
