package com.multi.icyadmin.data;

/**
 * Created by MultiMote on 08.01.2015.
 */
public enum CapabilitiesEnum {
    INVINCIBILITY(0x1),
    INVULNERABILITY(0x2),
    HIDE_NAME(0x4),
    REMOVE_HOSTILES(0x8),
    FAST_SHOOTING(0x10),
    FROZEN(0x20);

    /*
    -0x1 (1)
    -0x2 (10)
    -0x4 (100)
    -0x8 (1000)
    -0x10 (10000)
    -0x20 (100000)
    0x40 (1000000)
    0x80 (10000000)
    0x100 (100000000)
    0x200 (1000000000)
    0x400 (10000000000)
    0x800 (100000000000)
    0x1000 (1000000000000)
    0x2000 (10000000000000)
    0x4000 (100000000000000)
    0x8000 (1000000000000000)
    0x10000 (10000000000000000)
    0x20000 (100000000000000000)
    0x40000 (1000000000000000000)
    0x80000 (10000000000000000000)
    0x100000 (100000000000000000000)
    0x200000 (1000000000000000000000)
    0x400000 (10000000000000000000000)
    0x800000 (100000000000000000000000)
    0x1000000 (1000000000000000000000000)
    0x2000000 (10000000000000000000000000)
    0x4000000 (100000000000000000000000000)
    0x8000000 (1000000000000000000000000000)
    0x10000000 (10000000000000000000000000000)
    0x20000000 (100000000000000000000000000000)
    0x40000000 (1000000000000000000000000000000)
    0x80000000 (10000000000000000000000000000000)
         */

    private final int code;

    private CapabilitiesEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }


}
