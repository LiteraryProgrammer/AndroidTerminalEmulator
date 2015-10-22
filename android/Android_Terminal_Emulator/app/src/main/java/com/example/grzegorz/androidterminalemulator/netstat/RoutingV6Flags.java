package com.example.grzegorz.androidterminalemulator.netstat;

/**
 * Created by grzegorz on 15.10.15.
 */
//todo: move somewhere else?
public enum RoutingV6Flags {
    DEFAULT(0x00010000),
    ALLONLINK(0x00020000),
    ADDRCONF(0x00040000),
    PREFIX_RT(0x00080000),
    ANYCAST(0x00100000),
    NONEXTHOP(0x00200000),
    EXPIRES(0x00400000),
    ROUTEINFO(0x00800000),
    CACHE(0x01000000),
    FLOW(0x02000000),
    POLICY(0x04000000),
    //    PREF(pref)  ((pref) << 27) //todo: ????
    PREF_MASK(0x18000000),
    LOCAL(0x80000000);

    private final long value;

    public long getValue() {
        return value;
    }

    RoutingV6Flags(long value) {
        this.value = value;
    }

}


