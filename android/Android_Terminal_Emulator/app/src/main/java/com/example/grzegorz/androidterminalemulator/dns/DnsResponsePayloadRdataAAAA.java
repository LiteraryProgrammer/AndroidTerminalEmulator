package com.example.grzegorz.androidterminalemulator.dns;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;


/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataAAAA extends DnsResponsePayloadRdata {

    private String ipAddress;

    public DnsResponsePayloadRdataAAAA(byte[] bytes) throws Exception {
        ipAddress = InetAddress.getByAddress(bytes).toString();
    }

    public String toConsoleString(byte[] bytes) {
        return "IPv6 address:\t" + ipAddress;
    }
}
