package com.example.grzegorz.androidterminalemulator.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gpietrus on 01.08.15.
 */
public class Nslookup {

    private DatagramSocket datagramSocket;
    private InetAddress address;
    private DatagramPacket sendPacket;
    private DatagramPacket recvPacket;
    private DnsFrame responseDnsFrame;
    private byte responseBytes[];
    private String defaultDnsServer = "8.8.8.8"; //todo: make it an argument IMPORTANT!!!
    private int port = 53;

    public String run(String domainName, DnsPayload.RecordType recordType, String dnsServer) throws Exception {

        DnsFrame queryDnsFrame = new DnsFrame(domainName, recordType);
        datagramSocket = new DatagramSocket();

        address = InetAddress.getByName(dnsServer != null ? dnsServer : defaultDnsServer);
        //todo: socket error print sth

        //todo: check if received frame serial number is the same as sent
        send(queryDnsFrame);
        recv();
        return printResponse();
    }

    private String printResponse() {
        String consoleResponse = buildConsoleResponse(responseDnsFrame, responseBytes);
        System.out.println(consoleResponse);
        return consoleResponse;
    }

    public void send(DnsFrame dnsFrame) throws IOException {
        byte[] frameBytes = dnsFrame.getBytes();
        sendPacket = new DatagramPacket(frameBytes, frameBytes.length, address, port);
        datagramSocket.send(sendPacket);
    }

    public String buildConsoleResponse(DnsFrame responseDnsFrame, byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if(!responseDnsFrame.getFrameHeader().getRcode().toString().equals("0011")) { //error code

            //get all response payloads
            List<DnsResponsePayload> dnsResponseList = responseDnsFrame.getDnsResponseList();
            List<DnsResponsePayload> dnsAuthorityResponseList = responseDnsFrame.getDnsAuthorityResponseList();
            List<DnsResponsePayload> dnsAdditionalResponseList = responseDnsFrame.getDnsAdditionalResponseList();

            stringBuilder.append("Non-authoritative response:\n");
            for (DnsResponsePayload dnsResponsePayload : dnsResponseList) {
                stringBuilder.append(dnsResponsePayload.toConsoleString(bytes));
            }

            stringBuilder.append("Authoritative response:\n");
            for (DnsResponsePayload dnsAuthorityResponsePayload : dnsAuthorityResponseList) {
                stringBuilder.append(dnsAuthorityResponsePayload.toConsoleString(bytes));
            }

            stringBuilder.append("Additional response:\n");
            for (DnsResponsePayload dnsAdditionalResponsePayload : dnsAdditionalResponseList) {
                stringBuilder.append(dnsAdditionalResponsePayload.toConsoleString(bytes));
            }
        }
        else {
            stringBuilder.append("No such domain.\n");
        }

        return stringBuilder.toString();
    }

    public void recv() throws Exception {
        byte[] responseBuf = new byte[2048]; //todo: max of udp?
        DatagramPacket responseDatagramPacket = new DatagramPacket(responseBuf, responseBuf.length);
        datagramSocket.setSoTimeout(5000); //5seconds timeout
        datagramSocket.receive(responseDatagramPacket);
        int receivedLength = responseDatagramPacket.getLength();

        responseBytes = Arrays.copyOfRange(responseBuf, 0, receivedLength);
        responseDnsFrame = new DnsFrame(responseBytes);
    }
}

//todo: implement reverse dns in general
//todo: move reverse from traceroute to dns? !!!
//todo: important!!