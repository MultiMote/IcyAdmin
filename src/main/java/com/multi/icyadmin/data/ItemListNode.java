package com.multi.icyadmin.data;

import com.multi.icyadmin.handlers.PlayerProps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class ItemListNode {
    private final String name;
    private ListensEnum listens;
    private ActionsEnum type;
    private RequestEnum include;
    private String command_data;
    private int color = 0xFFFFFFFF;
    private int activeColor = 0xFFFFFFFF;
    private boolean canBeSelected = true;
    private boolean doubleClickEventFired = true;
    private boolean clickEventFired = false;

    public ItemListNode(String name) {
        this.name = name;
    }

    public static ItemListNode create(String name) {
        return new ItemListNode(name == null ? "" : name);
    }

    public static ItemListNode include(RequestEnum inc) {
        return new ItemListNode("").setInclude(inc);
    }

    public static ItemListNode separator() {
        return title("");
    }

    public static ItemListNode title(String s) {
        if (s == null) return separator();
        return new ItemListNode(s).setCanBeSelected(false).setDoubleClickEventFired(false).setClickEventFired(false);
    }

    public RequestEnum getInclude() {
        return include;
    }

    public ItemListNode setInclude(RequestEnum include) {
        this.include = include;
        return this;
    }

    public ActionsEnum getType() {
        return type;
    }

    public ItemListNode setType(ActionsEnum type) {
        this.type = type;
        return this;

    }

    public int getColor() {
        if (getListens() != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return color;
            PlayerProps p = PlayerProps.get(player);

            if (getListens() == ListensEnum.PROP && type.getProp() != null && p != null && p.isPropEnabled(type.getProp())) {
                return activeColor;
            } else if (getListens() == ListensEnum.GAMEMODE_CREATIVE && player.capabilities.isCreativeMode) {
                return activeColor;
            } else if (getListens() == ListensEnum.GAMEMODE_ADVENTURE && !player.capabilities.isCreativeMode && !player.capabilities.allowEdit) {
                return activeColor;
            } else if (getListens() == ListensEnum.GAMEMODE_SURVIVAL && !player.capabilities.isCreativeMode && player.capabilities.allowEdit) {
                return activeColor;
            } else if (getListens() == ListensEnum.FLY && player.capabilities.allowFlying) {
                return activeColor;
            }
        }
        return color;
    }

    public ItemListNode setColor(int color) {
        this.color = color;
        return this;
    }

    public ItemListNode setActiveColor(int color) {
        this.activeColor = color;
        return this;
    }

    public ListensEnum getListens() {
        return listens;
    }

    public ItemListNode setListens(ListensEnum listens) {
        this.listens = listens;
        return this;
    }

    public boolean isCanBeSelected() {
        return canBeSelected;
    }

    public ItemListNode setCanBeSelected(boolean isSelectable) {
        this.canBeSelected = isSelectable;
        return this;
    }

    public boolean isDoubleClickEventFired() {
        return doubleClickEventFired;
    }

    public ItemListNode setDoubleClickEventFired(boolean doubleClickEventFired) {
        this.doubleClickEventFired = doubleClickEventFired;
        return this;
    }

    public boolean isClickEventFired() {
        return clickEventFired;
    }

    public ItemListNode setClickEventFired(boolean clickEventFired) {
        this.clickEventFired = clickEventFired;
        return this;
    }

    public String getCommandData() {
        return command_data;
    }

    public ItemListNode setCommandData(String command_data) {
        this.command_data = command_data;
        return this;
    }

    public String getName() {
        return name;
    }
}
