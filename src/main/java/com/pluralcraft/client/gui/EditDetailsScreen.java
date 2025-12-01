package com.pluralcraft.client.gui;

import com.pluralcraft.data.AlterProfile;
import com.pluralcraft.data.SystemDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Screen for editing an alter's basic details (name, age, pronouns, bio)
 */
public class EditDetailsScreen extends Screen {
    private final Screen parentScreen;
    private final AlterProfile alter;

    private EditBox nameField;
    private EditBox ageField;
    private EditBox pronounsField;
    private EditBox bioField;

    public EditDetailsScreen(Screen parentScreen, AlterProfile alter) {
        super(Component.literal("Edit Details: " + alter.getName()));
        this.parentScreen = parentScreen;
        this.alter = alter;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int leftX = centerX - 150;
        int startY = 60;
        int fieldWidth = 300;
        int spacing = 30;

        // Name field
        this.nameField = new EditBox(this.font, leftX, startY, fieldWidth, 20,
                Component.literal("Name"));
        this.nameField.setValue(alter.getName());
        this.nameField.setMaxLength(50);
        this.addRenderableWidget(nameField);

        startY += spacing;

        // Age field
        this.ageField = new EditBox(this.font, leftX, startY, fieldWidth, 20,
                Component.literal("Age"));
        this.ageField.setValue(String.valueOf(alter.getAge()));
        this.ageField.setMaxLength(3);
        this.addRenderableWidget(ageField);

        startY += spacing;

        // Pronouns field
        this.pronounsField = new EditBox(this.font, leftX, startY, fieldWidth, 20,
                Component.literal("Pronouns"));
        this.pronounsField.setValue(alter.getPronouns());
        this.pronounsField.setMaxLength(50);
        this.addRenderableWidget(pronounsField);

        startY += spacing;

        // Bio field (multiline would be nice but EditBox is single line)
        this.bioField = new EditBox(this.font, leftX, startY, fieldWidth, 20,
                Component.literal("Bio"));
        this.bioField.setValue(alter.getBio());
        this.bioField.setMaxLength(200);
        this.addRenderableWidget(bioField);

        // Save button
        this.addRenderableWidget(Button.builder(
                Component.literal("✓ Save Changes"),
                button -> {
                    saveChanges();
                }
        ).bounds(centerX - 100, this.height - 40, 200, 20).build());
    }

    private void saveChanges() {
        String name = nameField.getValue().trim();
        String ageStr = ageField.getValue().trim();
        String pronouns = pronounsField.getValue().trim();
        String bio = bioField.getValue().trim();

        // Validate
        if (name.isEmpty()) {
            minecraft.player.sendSystemMessage(
                    Component.literal("Name cannot be empty!"));
            return;
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 0 || age > 999) {
                minecraft.player.sendSystemMessage(
                        Component.literal("Age must be between 0-999!"));
                return;
            }
        } catch (NumberFormatException e) {
            minecraft.player.sendSystemMessage(
                    Component.literal("Please enter a valid age number!"));
            return;
        }

        if (pronouns.isEmpty()) {
            minecraft.player.sendSystemMessage(
                    Component.literal("Pronouns cannot be empty!"));
            return;
        }

        // Apply changes
        alter.setName(name);
        alter.setAge(age);
        alter.setPronouns(pronouns);
        alter.setBio(bio);

        // Save to disk
        SystemDataManager.saveData();

        minecraft.player.sendSystemMessage(
                Component.literal("Details updated for " + name + "!"));

        // Go back
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw field labels
        graphics.drawString(this.font, "Name:", this.width / 2 - 150, 50, 0xFFFFFF);
        graphics.drawString(this.font, "Age:", this.width / 2 - 150, 80, 0xFFFFFF);
        graphics.drawString(this.font, "Pronouns:", this.width / 2 - 150, 110, 0xFFFFFF);
        graphics.drawString(this.font, "Bio:", this.width / 2 - 150, 140, 0xFFFFFF);

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
