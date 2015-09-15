package com.example.grzegorz.androidterminalemulator.dns;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataNS extends DnsResponsePayloadRdata {

    DomainNameLabels name;

    public DnsResponsePayloadRdataNS(byte[] bytes) throws Exception {
        name = new DomainNameLabels(bytes);
    }

    public String toConsoleString(byte[] bytes) {
        //todo: refactor exceptions
        try {
            return "Domain:\t" + name.toFullString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
