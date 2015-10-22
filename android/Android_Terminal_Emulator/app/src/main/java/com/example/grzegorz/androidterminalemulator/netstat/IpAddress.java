package com.example.grzegorz.androidterminalemulator.netstat;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by grzegorz on 15.10.15.
 */
public class IpAddress {

    public static String getAddress(final String hexAddress) throws Exception {

        //todo: rozpoznawanie ipv4 ipv6
        int addressLength = hexAddress.length();
        if (addressLength == 8) {//ipv4
            try {
                final long v = Long.parseLong(hexAddress, 16);
                final long adr = (v >>> 24) | (v << 24) |
                        ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
                return ((adr >> 24) & 0xff) + "." + ((adr >> 16) & 0xff) + "." + ((adr >> 8) & 0xff) + "." + (adr & 0xff);
            } catch (Exception e) {
                System.out.println("NetworkLog" + e.toString() + e);
                return "-1.-1.-1.-1";
            }
        } else if (addressLength == 32) { //ipv6
            return Ipv6AddressConverter.getCompressedAddress(Ipv6AddressConverter.addColons(Ipv6AddressConverter.changeByteOrder(hexAddress)));
        } else {
            throw new Exception("unknown address length");
        }

        //todo:
        //::100:0 amiast ::1:0? sprawdzic kolejnosc bajtow
        //todo: move to single function, chain of resposibility?
        //todo: upewnic sie ze dobrze liczy bo ostatni baj cos sie nie zgdza?
        //todo: netstat zle podaje? bo z ifconfig sie zgadza
        //todo: 8da0 nie ma w netstat - zamiast tego jest dodatkowe pole 0000
        //todo: bug w netstacie?
        //todo: sprawdzic na androridzie

    }

    //todo: refactor classes?
    public static class Ipv6AddressConverter {
        public static String getCompressedAddress(String longAddress) throws UnknownHostException {
//            longAddress = Inet6Address.getByName(longAddress).getHostAddress();
//            return longAddress.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
            return longAddress; //todo: temporarly disabled due, not working well
        }

        public static String changeByteOrder(String address) {
            //expects address without colons
            //http://serverfault.com/questions/592574/why-does-proc-net-tcp6-represents-1-as-1000

            return Joiner.on("").join(FluentIterable.from(Splitter.fixedLength(8).split(address)).transform(new Function<String, String>() {
                @Override
                public String apply(String part) {
                    List<String> bytes = Arrays.asList(Iterables.toArray(Splitter.fixedLength(2).split(part), String.class));
                    Collections.reverse(bytes);
                    return Joiner.on("").join(bytes);
                }
            }).toArray(String.class));

        }

        public static String addColons(String address) {
            return Joiner.on(":").join(Iterables.toArray(Splitter.fixedLength(4).split(address), String.class));
        }

    }

}
