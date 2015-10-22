package com.example.grzegorz.androidterminalemulator.netstat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grzegorz on 15.10.15.
 */
//todo: move somewhere else?
public enum RoutingFlags {
    UP(0x0001),
    GATEWAY(0x0002),
    HOST(0x0004),
    REINSTATE(0x0008),
    DYNAMIC(0x0010),
    MODIFIED(0x020),
    MTU(0x040),
    MSS(0x040), //compatilibity
    WINDOW(0x080),
    IRTT(0x0100),
    REJECT(0x0200);

    private final int value;

    public int getValue() {
        return value;
    }

    RoutingFlags(int value) {
        this.value = value;
    }

}


