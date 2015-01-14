package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */
public enum RequestEnum {
    NOTHING(false),
    ADMIN_LOGS(true),
    SOMETHING_ELSE(true),
    DEATH_LOGS(true);

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
