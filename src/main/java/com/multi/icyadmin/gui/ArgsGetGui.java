package com.multi.icyadmin.gui;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.Defines;
import com.multi.icyadmin.gui.elements.SimpleButton;
import com.multi.icyadmin.utils.FileProcessor;
import com.multi.icyadmin.utils.GLUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MultiMote on 07.01.2015.
 */
public class ArgsGetGui extends Gui {
    private static final ResourceLocation POP = new ResourceLocation(Defines.MOD_ID, "other.popup");

    private InfiPanelGui parent;

    private HashMap<String, GuiTextField> textFields;

    private boolean visible;
    private boolean confirmed;

    private String command;
    private List<String> parsedVars;
    private SimpleButton close;

    private int xCenter;
    private int yCenter;

    private int frameWidth;
    private int frameHeight;
    private int maxStringWidth;
    private FontRenderer fontRenderer;

    public ArgsGetGui(InfiPanelGui parent) {
        textFields = new HashMap<String, GuiTextField>();
        this.parent = parent;
    }

    private void prepareFields() {
        textFields.clear();
        if (parsedVars == null) return;

        maxStringWidth = 0;
        frameHeight = parsedVars.size() * 20 + 20;


        int num = 0;

        for (String s : parsedVars) {
            maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(s));
        }
        frameWidth = maxStringWidth + 100;

        for (String s : parsedVars) {
            GuiTextField textField = new GuiTextField(fontRenderer, xCenter + frameWidth / 2 - 80 - 3, (yCenter - frameHeight / 2) + num * 20 + 4, 80, 14);
            if (Core.dynStorage.vars_cache.containsKey(s)) {
                textField.setText(Core.dynStorage.vars_cache.get(s));
                //textField.setSelectionPos(0);
            }
            textFields.put(s, textField);
            num++;
        }

    }

    public void initGui() {
        xCenter = parent.width / 2;
        yCenter = parent.height / 2;
        fontRenderer = parent.mc.fontRenderer;
        prepareFields();
        close = new SimpleButton(0, xCenter + frameWidth / 2 - 62, yCenter + frameHeight / 2 - 16, 60, 14, 0xFF747474, 0xFF3a3a3a, 0xFFa0a0a0, I18n.format("icyadmin.confirm"));
        close.setSound(new ResourceLocation(Defines.MOD_ID, "other.enterkey"));
    }

    public String makeCmd() {
        String out = command + " "; //костыль
        for (String s : textFields.keySet()) { //костыль
            String fieldText = textFields.get(s).getText(); //костыль //костыль //костыль
            if (!fieldText.trim().equals("")) Core.dynStorage.vars_cache.put(s, fieldText); //костыль
            out = out.replaceAll("\\$" + s + "\\s", fieldText + " "); //костыль //костыль //костыль //костыль
            FileProcessor.writeVars(); //костыль
        } //костыль
        return out.trim(); //костыль
    } //костыль

    //костыль
    public boolean isVisible() {
        return visible;
    }

    //костыль
    public void setVisible(boolean visible) {
        if (visible) {
            Core.logger.info("Args GUI is now visible.");
            parent.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(POP, 1.0F));

            initGui(); //костыль
        }
        this.visible = visible;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setParsedVars(List<String> parsedVars) {
        this.parsedVars = parsedVars;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void draw(int mx, int my, float f) {
        if (!isVisible()) return;
        drawRect(0, 0, parent.width, parent.height, 0xBB000000);
        GLUtils.drawBorderedRect(xCenter - frameWidth / 2, yCenter - frameHeight / 2, xCenter + frameWidth / 2, yCenter + frameHeight / 2, 0xFF1C1C1C, 1, 0xFF747474);

        close.drawButton(parent.mc, mx, my);

        for (String s : textFields.keySet()) {
            GuiTextField field = textFields.get(s);
            fontRenderer.drawString(s.replaceAll("_", " ") + ":", xCenter - frameWidth / 2 + 4, field.yPosition + 3, 0xFFFFFF);
            field.drawTextBox();
        }
    }

    protected void mouseClicked(int mx, int my, int mb) {
        if (close.mousePressed(parent.mc, mx, my)) {
            close.func_146113_a(parent.mc.getSoundHandler());
            closeClicked();
        } else {
            for (GuiTextField f : textFields.values()) f.mouseClicked(mx, my, mb);
        }
    }

    private void closeClicked() {
        parent.argsConfirmed();
        setVisible(false);
    }

    protected void keyTyped(char keyChar, int keyId) {
        if (keyId == 1) {
            setVisible(false);
        } else {
            for (GuiTextField f : textFields.values()) f.textboxKeyTyped(keyChar, keyId);
        }
    }


}
