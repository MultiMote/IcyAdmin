package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */
public class ItemListNode {
    private final String name;
    private NodeActionsEnum type;
    private IncludesEnum include;
    private String command_data;
    private int color = 0xFFFFFFFF;
    private boolean canBeSelected = true;
    private boolean doubleClickEventFired = true;
    private boolean clickEventFired = false;

    public ItemListNode(String name) {
        this.name = name;
    }

    public static ItemListNode create(String name) {
        return new ItemListNode(name == null ? "" : name);
    }

    public static ItemListNode include(IncludesEnum inc) {
        return new ItemListNode("").setInclude(inc);
    }

    public static ItemListNode separator() {
        return title("");
    }

    public static ItemListNode title(String s) {
        if (s == null) return separator();
        return new ItemListNode(s).setCanBeSelected(false).setDoubleClickEventFired(false).setClickEventFired(false);
    }

    public IncludesEnum getInclude() {
        return include;
    }

    public ItemListNode setInclude(IncludesEnum include) {
        this.include = include;
        return this;
    }

    public NodeActionsEnum getType() {
        return type;
    }

    public ItemListNode setType(NodeActionsEnum type) {
        this.type = type;
        return this;

    }

    public int getColor() {
        return color;
    }

    public ItemListNode setColor(int color) {
        this.color = color;
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
