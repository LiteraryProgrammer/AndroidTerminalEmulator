package com.example.grzegorz.androidterminalemulator;

import com.example.grzegorz.androidterminalemulator.dns.DnsPayload;
import com.example.grzegorz.androidterminalemulator.dns.Nslookup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gpietrus on 01.11.15.
 */
public class Hostname {
    private String PTRResponseRegexp = "^(.*\\n)+(Payload:\\tDomain:\\t(.*)\\n)(.*\\n)+$";
    Pattern PTRResponsePattern = Pattern.compile(PTRResponseRegexp);
    String matchedHostname = null;

    String address;

    //note: parsing nslookup response may not work when nslookup response format got changed
    public Hostname(String ip) throws Exception {
        String responseString = new com.example.grzegorz.androidterminalemulator.dns.Nslookup().run(Nslookup.ipToFqdn(ip), DnsPayload.RecordType.PTR, null); //todo: ptr?
        Matcher matcher = PTRResponsePattern.matcher(responseString);

        if (responseString.contains("Domain") && matcher.matches()) {
            matchedHostname = matcher.group(3);
        }
    }

    public String getMatchedHostname() {
        return matchedHostname;
    }
}