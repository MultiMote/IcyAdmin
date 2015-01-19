package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */
public enum RequestEnum {
    NOTHING(false),
    ADMIN_LOGS(true),
    FLY(false),
    DEATH_LOGS(true),
    SEND_ACTION(false),
    SEND_MENU_HASH(false),
    REQUEST_MENU_HASH(false),
    SEND_MENU(false);

    private final boolean isInclude;

    private RequestEnum(boolean isInclude) {
        this.isInclude = isInclude;
    }

    public static RequestEnum getValueById(int n) {
        RequestEnum[] vals = values();
        return n > vals.length - 1 ? NOTHING : vals[n];
    }

    public static RequestEnum parseElement(String s) {
        if (s == null) return null;
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public boolean isInclude() {
        return isInclude;
    }
}
