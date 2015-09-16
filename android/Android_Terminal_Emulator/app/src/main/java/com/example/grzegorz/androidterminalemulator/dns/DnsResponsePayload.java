package com.example.grzegorz.androidterminalemulator.dns;

import java.util.Arrays;

/**
 * Created by gpietrus on 02.08.15.
 */
public class DnsResponsePayload extends DnsPayload {

    DomainNameLabels domainNameLabels;
    private short type;
    private short dnsClass;
    private int ttl;
    private int dataLength;
    private DnsResponsePayloadRdata dnsResponsePayloadRdata;


    public DnsResponsePayload(DomainNameLabels domainNameLabels, short type, short dnsClass, int ttl, int dataLength, DnsResponsePayloadRdata dnsResponsePayloadRdata) {
        this.domainNameLabels = domainNameLabels;
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
        this.dataLength = dataLength;
        this.dnsResponsePayloadRdata = dnsResponsePayloadRdata;
    }

    public String toConsoleString(byte[] bytes) {

        StringBuilder stringBuilder = new StringBuilder();

        //todo: refactor exceptions
        try {
            stringBuilder.append(domainNameLabels.toFullString(bytes) + " \n");
        stringBuilder.append("Type:\t" + RecordType.fromValue(type) + "\n");
            stringBuilder.append("Class:\t" + dnsClass + "\n");
            stringBuilder.append("TTL:\t" + ttl + "\n");
            stringBuilder.append("DataLength:\t" + dataLength + "\n");
            stringBuilder.append("Payload:\t" + dnsResponsePayloadRdata.toConsoleString(bytes) + "\n");
            stringBuilder.append("\n");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public int getTtl() {
        return ttl;
    }


}
