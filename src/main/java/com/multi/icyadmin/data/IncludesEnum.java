package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */
public enum IncludesEnum {
    ADMIN_LOGS,
    DEATH_LOGS;

    public static IncludesEnum parseElement(String s) {
        if (s == null) return null;
        s = s/*.replaceAll(":", "")*/.trim();
        if (s.equals("")) return null;
        for (IncludesEnum element : IncludesEnum.values()) {
            if (element.toString().equals(s)) return element;
        }
        return null;
    }
}
