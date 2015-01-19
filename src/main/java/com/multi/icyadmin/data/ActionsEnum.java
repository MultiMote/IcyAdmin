package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 03.01.2015.
 */

public enum ActionsEnum {
    NOTHING(false, false),
    HEAL(true, false),
    KILL(true, false),
    POISON(true, false),
    FEED(true, false),
    STARVE(true, false),
    DISMOUNT(true, false),
    IGNITE(true, false),
    FREEZE(true, false),
    UNFREEZE(true, false),
    CLEAR_INVENTORY(true, false),
    UNPOTION(true, false),
    TITLE(false, false),
    LINK(false, false),
    PLAYER(false, false),
    PAGE(false, false),
    CMD_EXEC(false, false),
    INVULNERABILITY_TOGGLE(false, true, CapabilitiesEnum.INVULNERABILITY),
    INVINCIBILITY_TOGGLE(false, true, CapabilitiesEnum.INVINCIBILITY),
    NAMEPLATE_TOGGLE(false, true, CapabilitiesEnum.HIDE_NAME),
    MOB_AURA_TOGGLE(false, true, CapabilitiesEnum.REMOVE_HOSTILES),
    FAST_SHOOTING_TOGGLE(false, true, CapabilitiesEnum.FAST_SHOOTING),
    FLY_TOGGLE(false, true),
    INFINITE_ARROWS_TOGGLE(false, true);

    private final boolean requiresTarget;
    private final boolean canListen;
    private final CapabilitiesEnum prop;


    private ActionsEnum(boolean requiresTarget, boolean canListen) {
        this(requiresTarget, canListen, null);
    }

    private ActionsEnum(boolean requiresTarget, boolean canListen, CapabilitiesEnum cap) {
        this.requiresTarget = requiresTarget;
        this.canListen = canListen;
        this.prop = cap;
    }

    public static ActionsEnum parseElement(String s) {
        if (s == null) return null;
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public static ActionsEnum getValueById(int n) {
        ActionsEnum[] vals = values();
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
