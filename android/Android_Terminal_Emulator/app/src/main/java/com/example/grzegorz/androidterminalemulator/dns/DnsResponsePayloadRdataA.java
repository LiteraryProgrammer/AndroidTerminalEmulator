package com.example.grzegorz.androidterminalemulator.dns;

import java.net.InetAddress;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataA extends DnsResponsePayloadRdata {

    private String ipAddress;

    public DnsResponsePayloadRdataA(byte[] bytes) throws Exception {
        ipAddress = InetAddress.getByAddress(bytes).toString();
    }

    //todo: refactor, niepotrzebne przekazywanie bytes w niektorych typach?
    public String toConsoleString(byte[] bytes) {
        return "IPv4 Address:\t" + ipAddress;
    }
}
