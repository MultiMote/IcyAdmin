package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */

public enum NodeActionsEnum {
    NOTHING,
    TITLE,
    PLAYER,
    PAGE,
    CMD_EXEC,
    INVULNERABILITY_TOGGLE,
    INVINCIBILITY_TOGGLE,
    NAMEPLATE_TOGGLE,
    MOB_AURA_TOGGLE,
    FAST_SHOOTING_TOGGLE,
    INFINITE_ARROWS_TOGGLE,
    BAN(true),
    BAN_RESTART(true),
    THROW_UP(true),
    KICK,;

    private final boolean requiresTarget;

    private NodeActionsEnum() {
        requiresTarget = false;
    }

    private NodeActionsEnum(boolean requiresTarget) {
        this.requiresTarget = requiresTarget;
    }

    public static NodeActionsEnum parseElement(String s) {
        if (s == null) return null;
        s = s.trim();
        for (NodeActionsEnum element : NodeActionsEnum.values()) {
            if (element.toString().equals(s)) return element;
        }
        return null;
    }

    public static NodeActionsEnum getValueById(int n) {
        NodeActionsEnum[] vals = values();
        return n > vals.length - 1 ? NOTHING : vals[n];
    }

    public boolean isRequiresTarget() {
        return requiresTarget;
    }

}
