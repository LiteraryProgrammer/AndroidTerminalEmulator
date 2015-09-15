package com.example.grzegorz.androidterminalemulator.dns;
import java.io.IOException;

/**
 * Created by gpietrus on 01.08.15.
 */
public class Main {

    //todo: dla any nie dziala?
    //todo: jak dokladnie dziala dns - przekierowania?
    //todo: obsluga truncated?

    public static void main(String[] args) throws Exception {
        Nslookup nslookup = new Nslookup();
        //String arguments = "poczta.onet.pl";
        //String arguments = "google.pl";
//        String arguments = "ef-grp.tk";
        String arguments = "ns1.google.com";
        //String arguments = "google.pl";
//        nslookup.run(arguments, DnsPayload.RecordType.ANY);
        nslookup.run(arguments, DnsPayload.RecordType.AAAA);
    }
}
