package com.example.grzegorz.androidterminalemulator.dns;


import java.util.Arrays;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataRP extends DnsResponsePayloadRdata {

    private DomainNameLabels mailbox;
    private DomainNameLabels txt; //todo: naming?


    public DnsResponsePayloadRdataRP(byte[] bytes) throws Exception {

        int iter = 0;
        mailbox = new DomainNameLabels(bytes);
        iter += mailbox.length();
        txt = new DomainNameLabels(Arrays.copyOfRange(bytes, iter, bytes.length));
    }

    public String toConsoleString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("Mailbox:\t" + mailbox.toFullString(bytes) + "\n");
            stringBuilder.append("TXT:\t" + txt.toFullString(bytes) + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
