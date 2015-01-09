package com.multi.icyadmin.gui.elements;

import com.multi.icyadmin.Defines;
import com.multi.icyadmin.data.ItemListNode;
import com.multi.icyadmin.gui.InfiPanelGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.util.List;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class ItemList extends Gui {

    private final List<ItemListNode> items;
    public int xPos;
    public int yPos;
    public int width;
    public int height;
    public int sliderPos;
    private FontRenderer fontRenderer;
    private Minecraft mc;
    private int indexSelected = 0;
    private int lastClickedIndex = -1;
    private long lastClickTime;
    private int elementsDisplayed;
    private int firstIndex;
    private InfiPanelGui parent;
    private boolean isDragging;
    private boolean isClicked;
    private boolean sliderDrawn;

    public ItemList(InfiPanelGui parent, Minecraft mc, int x, int y, int width, int height, List<ItemListNode> items) {
        this.parent = parent;
        this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.items = items;
    }

    public int getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(int indexSelected) {
        this.indexSelected = indexSelected;
    }

    public boolean isMouseIn(int x, int y) {
        return x >= this.xPos && x < this.xPos + this.width && y >= this.yPos && y < this.yPos + this.height;
    }

    public boolean isMouseInSliderZone(int x, int y) {
        return isMouseIn(x, y) && x > xPos + width - 8 && x < xPos + width - 1;
    }

    private void drawSlider(int x, int y) {
        drawRect(x, y + 1, x + 5, y + getSliderHeight() - 3, 0xFF747474);
    }

    public int getSliderHeight() {
        return items.size() == 0 ? 5 : this.height / Math.max((items.size()) / 8, 8);
    }

    public float getRelativeSliderPos() {
        return height == 0 ? 0 : (float) sliderPos / (float) (height - getSliderHeight());
    }

    public void mouseClicked(int x, int y) {
        if (isMouseIn(x, y) && x < xPos + width - 10) {
            int yIn = Math.round((float) ((y - yPos) + 2) / 8) - 1;
            int tempIndex;
            if (yIn < elementsDisplayed) tempIndex = yIn + firstIndex < 0 ? 0 : yIn + firstIndex;
            else return;

            if (items.get(tempIndex).isCanBeSelected()) indexSelected = tempIndex;

            ItemListNode clicked = items.get(tempIndex);

            if (clicked.isDoubleClickEventFired() && lastClickedIndex == tempIndex && (Minecraft.getSystemTime() - lastClickTime < Defines.DOUBLECLICK_MS))
                parent.elementDoubleClicked(clicked);
            else if (clicked.isClickEventFired()) parent.elementClicked(clicked);
            lastClickedIndex = tempIndex;
            lastClickTime = Minecraft.getSystemTime();
        }
    }

    private int getSliderStep() {
        return height / items.size();
    }

    public void drawList(int x, int y, float f, boolean ignoreMouse) {

        if (Defines.DEBUG_LINES) {
            drawRect(xPos, yPos, xPos + width, yPos + height, 0xFFFFFFFF);
            drawRect(xPos + 1, yPos + 1, xPos + width - 1, yPos + height - 1, 0xFF000000);
            drawRect(xPos + width - 9, yPos, xPos + width - 8, yPos + height, 0xFFFFFFFF);
        }


        firstIndex = (int) ((float) items.size() * getRelativeSliderPos());

        int p = 0;

        for (int i = firstIndex; i < items.size(); i++) {

            int yString = yPos + 2 + p * 8;
            if (yString + 8 < yPos + height) {
                String str = items.get(i).getName();
                int color = items.get(i).getColor();
                String toDisplay = fontRenderer.getStringWidth(str) > this.width - 10 ? fontRenderer.trimStringToWidth(str, this.width - 15) + "~" : str;
                if (i == indexSelected)
                    drawRect(xPos + 1, yString, xPos + width - (sliderDrawn ? 9 : 2), yString + 8, 0xFF555555);
                fontRenderer.drawString(toDisplay, xPos + 2, yString, color);
                p++;
            }
        }
        elementsDisplayed = p;

        sliderDrawn = elementsDisplayed < items.size();

        if (indexSelected > items.size() - 1) indexSelected = items.size() - 1;
        if (indexSelected < 0) indexSelected = 0;

        if (ignoreMouse) return;

        boolean leftClick = Mouse.isButtonDown(0);

        if (!isClicked && leftClick && isMouseInSliderZone(x, y)) isDragging = true;

        if (!leftClick) isDragging = false;

        this.isClicked = leftClick;

        if (!sliderDrawn) return;

        if (Mouse.isButtonDown(0) && isDragging) {
            sliderPos = y - getSliderHeight() / 2 - yPos;
            if (yPos + sliderPos < yPos) sliderPos = 0;
            if (yPos + sliderPos + getSliderHeight() > yPos + height) sliderPos = height - getSliderHeight();
        }

        if (isMouseIn(x, y)) {
            for (; !this.mc.gameSettings.touchscreen && Mouse.next(); this.mc.currentScreen.handleMouseInput()) {
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0) {
                    int step = getSliderStep();
                    sliderPos = wheel > 0 ? sliderPos - step : sliderPos + step;
                    if (yPos + sliderPos < yPos) sliderPos = 0;
                    if (yPos + sliderPos + getSliderHeight() > yPos + height) sliderPos = height - getSliderHeight();
                }
            }

        }

        drawSlider(xPos + width - 7, yPos + sliderPos + 1);

    }

}
