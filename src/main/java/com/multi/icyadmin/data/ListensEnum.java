package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 08.01.2015.
 */
public enum ListensEnum {
    PROP,
    GAMEMODE_CREATIVE,
    GAMEMODE_SURVIVAL,
    GAMEMODE_ADVENTURE;


    public static ListensEnum parseElement(String s) {
        if (s == null) return null;
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }
}
