package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.dns.DnsPayload;

import java.io.IOException;

/**
 * Created by grzegorz on 12.05.15.
 */

public class Nslookup extends ExtraCommand {

    private TextView tv = null;

    public Nslookup(String cmd) {
        super(cmd);
    }

    @Override
    protected void onPreExecute(TextView view) {
        tv = view;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        com.example.grzegorz.androidterminalemulator.dns.Nslookup nslookup = new com.example.grzegorz.androidterminalemulator.dns.Nslookup();
        String domainName = cmd.split(" ")[1];
//        String recordType = cmd.split(" ")[2];
        String response = null;
        try {
            response = nslookup.run(domainName, DnsPayload.RecordType.SOA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response != null) {
            publishProgress(response);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }
}
