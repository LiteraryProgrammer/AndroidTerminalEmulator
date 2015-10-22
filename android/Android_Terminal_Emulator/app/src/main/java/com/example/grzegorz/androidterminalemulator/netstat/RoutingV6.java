package com.example.grzegorz.androidterminalemulator.netstat;

import com.google.common.base.Joiner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by grzegorz on 15.10.15.
 */
//http://www.tldp.org/HOWTO/Linux+IPv6-HOWTO/proc-net.html
public class RoutingV6 {

    String destination;
    Integer dstPrefixLength;
    String source;
    Integer srcPrefixLength;
    String nextHop;
    long metric;
    Integer referenceCounter;
    Integer useCounter;
    String flags;
    String deviceName;

    @Override
    public String toString() {
        return "RoutingV6{" +
                "destination='" + destination + '\'' +
                ", dstPrefixLength='" + dstPrefixLength + '\'' +
                ", source='" + source + '\'' +
                ", srcPrefixLength='" + srcPrefixLength + '\'' +
                ", nextHop='" + nextHop + '\'' +
                ", metric='" + metric + '\'' +
                ", referenceCounter='" + referenceCounter + '\'' +
                ", useCounter='" + useCounter + '\'' +
                ", flags='" + flags + '\'' +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }

    public String toConsoleString() {
        return toString(); //todo: change
    }

    public RoutingV6(String destination, String dstPrefixLength, String source, String srcPrefixLength, String nextHop, String metric, String referenceCounter, String useCounter, String flags, String deviceName) {
        try {
            this.destination = IpAddress.Ipv6AddressConverter.getCompressedAddress(IpAddress.Ipv6AddressConverter.addColons(destination)); //todo: refacto
            this.dstPrefixLength = Integer.parseInt(dstPrefixLength, 16);
            this.source = IpAddress.Ipv6AddressConverter.getCompressedAddress(IpAddress.Ipv6AddressConverter.addColons(source));
            this.srcPrefixLength = Integer.parseInt(srcPrefixLength, 16);
            this.nextHop = IpAddress.Ipv6AddressConverter.getCompressedAddress(IpAddress.Ipv6AddressConverter.addColons(nextHop));
            this.metric = (int) Long.parseLong(metric,16);
            this.referenceCounter = (int) Long.parseLong(referenceCounter,10);
            this.useCounter = (int) Long.parseLong(useCounter,16);
            this.flags = interpreteFlags(flags);
            this.deviceName = deviceName;
        } catch (Exception e) {

        }
    }

/*

              U (trasa jest zestawiona [up])
              H (cel jest stacją [host])
              G (używa bramki [gateway])
              R (przywraca trasę na trasowanie dynamiczne [reinstate])
              D (dynamicznie instalowana przez demona lub przekierowanie)
              M (modyfikowana z demona trasowania lub przekierowania)
              A (instalowana przez addrconf)
              C (wpis bufora podręcznego [cache])
              ! (trasa odrzucenia [reject])

 */

    /*
    private String interpreteFlags(String flags) {
        //http://unix.stackexchange.com/questions/37757/route-and-e-flag
        ///usr/include/linux/ipv6_route.h
        //todo: invalid flags interpreting

        int flagsSum = Integer.parseInt(flags, 16);
        ArrayList<String> interpretedFlags = new ArrayList<>();

        List<RoutingV6Flags> routingFlags = Arrays.asList(RoutingV6Flags.values());
        Collections.reverse(routingFlags);

        for (RoutingV6Flags routingFlag : routingFlags) {
            long flagValue = routingFlag.getValue();
            if (flagsSum - flagValue >= 0) {
                interpretedFlags.add(routingFlag.toString());
            }

        }

        return Joiner.on(", ").join(interpretedFlags);
    }
*/
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
