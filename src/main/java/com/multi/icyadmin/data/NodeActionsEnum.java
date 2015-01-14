package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */

public enum NodeActionsEnum {
    NOTHING(false, false),
    TITLE(false, false),
    PLAYER(false, false),
    PAGE(false, false),
    CMD_EXEC(false, false),
    INVULNERABILITY_TOGGLE(false, true, CapabilitiesEnum.INVULNERABILITY),
    INVINCIBILITY_TOGGLE(false, true, CapabilitiesEnum.INVINCIBILITY),
    NAMEPLATE_TOGGLE(false, true, CapabilitiesEnum.HIDE_NAME),
    MOB_AURA_TOGGLE(false, true, CapabilitiesEnum.REMOVE_HOSTILES),
    FAST_SHOOTING_TOGGLE(false, true, CapabilitiesEnum.FAST_SHOOTING),
    INFINITE_ARROWS_TOGGLE(false, true),
    BAN(true, false),
    BAN_RESTART(true, false),
    THROW_UP(true, false),
    KICK(true, false);

    private final boolean requiresTarget;
    private final boolean canListen;
    private final CapabilitiesEnum prop;


    private NodeActionsEnum(boolean requiresTarget, boolean canListen) {
        this(requiresTarget, canListen, null);
    }

    private NodeActionsEnum(boolean requiresTarget, boolean canListen, CapabilitiesEnum cap) {
        this.requiresTarget = requiresTarget;
        this.canListen = canListen;
        this.prop = cap;
    }

    public static NodeActionsEnum parseElement(String s) {
        if (s == null) return null;
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ignored) {
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

    public boolean canListen() {
        return canListen;
    }

    public CapabilitiesEnum getProp() {
        return prop;
    }
}
