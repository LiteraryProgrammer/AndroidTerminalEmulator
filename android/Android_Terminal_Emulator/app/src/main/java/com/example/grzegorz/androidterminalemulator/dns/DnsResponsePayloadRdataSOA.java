package com.example.grzegorz.androidterminalemulator.dns;

import java.util.Arrays;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataSOA extends DnsResponsePayloadRdata {

    private DomainNameLabels primaryNSLabels;
    private DomainNameLabels adminMBLabels;
    private long serialNumber;
    private long refreshInterval;
    private long retryInterval;
    private long expirationLimit;
    private long minimumTtl;

    public DnsResponsePayloadRdataSOA(byte[] bytes) throws Exception {

        int iter = 0;
        primaryNSLabels = new DomainNameLabels(bytes);
//        System.out.println(primaryNSLabels.length());
        iter += primaryNSLabels.length();

        adminMBLabels = new DomainNameLabels(Arrays.copyOfRange(bytes, iter, bytes.length));
        iter += adminMBLabels.length();
//        System.out.println(adminMBLabels.length());

        serialNumber = Utils.byteArrayToInt(Arrays.copyOfRange(bytes, iter, iter + 4));
        iter += 4;

        refreshInterval = Utils.byteArrayToInt(Arrays.copyOfRange(bytes, iter, iter + 4));
        iter += 4;

        retryInterval = Utils.byteArrayToInt(Arrays.copyOfRange(bytes, iter, iter + 4));
        iter += 4;

        expirationLimit = Utils.byteArrayToInt(Arrays.copyOfRange(bytes, iter, iter + 4));
        iter += 4;

        minimumTtl = Utils.byteArrayToInt(Arrays.copyOfRange(bytes, iter, iter + 4));
        iter += 4;

    }

    public String toConsoleString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("Primary:\t" + primaryNSLabels.toFullString(bytes) + "\n");
            stringBuilder.append("Admin:\t" + adminMBLabels.toFullString(bytes) + "\n");
            stringBuilder.append("Serial number:\t" + serialNumber + "\n");
            stringBuilder.append("Refresh interval:\t" + refreshInterval + "\n");
            stringBuilder.append("Retry interval:\t" + retryInterval + "\n");
            stringBuilder.append("Expiration limit:\t" + expirationLimit + "\n");
            stringBuilder.append("Minimum ttl:\t" + minimumTtl + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
