package com.example.grzegorz.androidterminalemulator.netstat;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import java.util.*;

/**
 * Created by grzegorz on 15.10.15.
 */
public class RoutingV4 {

    private String iface;
    private String destination;
    private String gateway;
    private String flags;
    private String refCnt;
    private String use;
    private String metric;
    private String mask;
    private String mtu;
    private String window;
    private String irtt;

    @Override
    public String toString() {
        return "RoutingV4{" +
                "iface='" + iface + '\'' +
                ", destination='" + destination + '\'' +
                ", gateway='" + gateway + '\'' +
                ", flags='" + flags + '\'' +
//                ", refCnt='" + refCnt + '\'' + //unused
//                ", use='" + use + '\'' + //unused
                ", metric='" + metric + '\'' +
                ", mask='" + mask + '\'' +
                ", mtu='" + mtu + '\'' +
                ", window='" + window + '\'' +
                ", irtt='" + irtt + '\'' +
                '}';
    }

    public String toConsoleString() {
        return iface + '\t' +
                destination + '\t' +
                gateway + '\t' +
                flags + '\t' +
                metric + '\t' +
                mask + '\t' +
                mtu + '\t' +
                window + '\t' +
                irtt + '\t';
    }

    public RoutingV4(String iface, String destination, String gateway, String flags, /* String refCnt, String use, */ String metric, String mask, String mtu, String window, String irtt) {
        //todo: ipv6
        try {
            this.iface = iface;
            this.destination = IpAddress.getAddress(destination);
            this.gateway = IpAddress.getAddress(gateway);
            this.flags = interpreteFlags(flags);
//            this.refCnt = refCnt; //unused
//            this.use = use;
            this.metric = metric;
            this.mask = IpAddress.getAddress(mask);
            this.mtu = mtu;
            this.window = window;
            this.irtt = irtt;
            //todo: should it be converted from hex?
        } catch (Exception e) {

        }
    }

    private String interpreteFlags(String flags) {
        //http://www.thegeekstuff.com/2012/05/route-flags/
        //https://doc.pfsense.org/index.php/What_do_the_flags_on_the_routing_table_mean
        //http://www.onlamp.com/pub/a/linux/2000/11/16/LinuxAdmin.html?page=2
        //values from /usr/include/linux/route.h

        int flagsSum = Integer.parseInt(flags, 16);
        ArrayList<String> interpretedFlags = new ArrayList<>();

        List<RoutingFlags> routingFlags = Arrays.asList(RoutingFlags.values());
        Collections.reverse(routingFlags);

        for (RoutingFlags routingFlag : routingFlags) {
            int flagValue = routingFlag.getValue();
            if (flagsSum - flagValue >= 0) {
                interpretedFlags.add(routingFlag.toString());
            }

        }

        return Joiner.on(", ").join(interpretedFlags);
    }


}
