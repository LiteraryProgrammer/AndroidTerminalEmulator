package com.example.grzegorz.androidterminalemulator.dns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.filter;

/**
 * Created by gpietrus on 01.08.15.
 */

public class DnsFrame extends Frame {

    public List getDnsQueryList() {
        return dnsQueryList;
    }

    private List<DnsResponsePayload> dnsQueryList;
    private List<DnsResponsePayload> dnsResponseList;
    private List<DnsResponsePayload> dnsAuthorityResponseList;
    private List<DnsResponsePayload> dnsAdditionalResponseList;

    public DnsHeader getFrameHeader() {
        return (DnsHeader) frameHeader;
    }

    public List<DnsResponsePayload> getDnsResponseList() {
        return dnsResponseList;
    }

    public List<DnsResponsePayload> getDnsAuthorityResponseList() {
        return dnsAuthorityResponseList;
    }

    public List<DnsResponsePayload> getDnsAdditionalResponseList() {
        return dnsAdditionalResponseList;
    }

    DnsFrame(String domainName, DnsPayload.RecordType recordType) { //query frame
        //header
        frameHeader = new DnsHeader(DnsHeader.QR.query);

        //payload
        DnsQueryPayload dnsQueryPayload = new DnsQueryPayload(domainName, recordType);
        framePayloadList = new ArrayList<FramePayload>();
        framePayloadList.add(dnsQueryPayload);
    }

    DnsFrame(byte[] bytes) throws Exception { //response frame created from bytes
        DnsHeader dnsHeader = new DnsHeader(Arrays.copyOfRange(bytes, 0, 12)); //first 12 bytes - header
        frameHeader = dnsHeader;
        byte[] payloadsBytes = Arrays.copyOfRange(bytes, 12, bytes.length);

        if(!dnsHeader.getRcode().toString().equals("0011")) { //error code
            //analysing payloads
            framePayloadList = new ArrayList<FramePayload>();
            DnsPayloadAnalyzer dnsPayloadAnalyzer = new DnsPayloadAnalyzer(payloadsBytes, dnsHeader);
            dnsPayloadAnalyzer.analyze();
            dnsQueryList = dnsPayloadAnalyzer.getDnsQueryList();
            dnsResponseList = dnsPayloadAnalyzer.getDnsResponseList();
            dnsAuthorityResponseList = dnsPayloadAnalyzer.getDnsAuthorityResponseList();
            dnsAdditionalResponseList = dnsPayloadAnalyzer.getDnsAdditionalResponseList();
        }
    }

    public List<DnsPayload> getPayloads(Class c) { //deprecated?
        //todo: change to iterable?
        List<DnsPayload> specificDnsPayloads = new ArrayList<DnsPayload>();
        for (FramePayload payload : framePayloadList) {
            if (payload.getClass() == c) {
                specificDnsPayloads.add((DnsPayload) payload);
            }
        }
        return specificDnsPayloads;
    }

    public String toResponseString() {
        //todo: refactor, don't use object?
        StringBuilder stringBuilder = new StringBuilder();
        for (Object dnsResponsePayload : dnsResponseList) {
            stringBuilder.append(dnsResponsePayload.toString());
        }
        return stringBuilder.toString();
    }

}
