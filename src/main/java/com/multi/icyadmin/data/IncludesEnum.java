package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */
public enum IncludesEnum {
    NOTHING,
    ADMIN_LOGS,
    SOMETHING_ELSE, DEATH_LOGS;

    public static IncludesEnum getValueById(int n) {
        IncludesEnum[] vals = values();
        return n > vals.length - 1 ? NOTHING : vals[n];
    }

    public static IncludesEnum parseElement(String s) {
        if (s == null) return null;
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }
}
