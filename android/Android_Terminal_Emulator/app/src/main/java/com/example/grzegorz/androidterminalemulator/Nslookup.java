package com.example.grzegorz.androidterminalemulator;

import android.widget.ScrollView;
import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.dns.DnsPayload;

/**
 * Created by grzegorz on 12.05.15.
 */

public class Nslookup extends ExtraCommand {

    public Nslookup(String cmd) {
        super(cmd);
    }

    public Boolean finished() {
        return true; //todo
    }

    @Override
    public void cancel() {

    }

    @Override
    //todo: add no answers case
    protected Object doInBackground(Object[] params) {
        com.example.grzegorz.androidterminalemulator.dns.Nslookup nslookup = new com.example.grzegorz.androidterminalemulator.dns.Nslookup();
        String args[] = cmd.split(" ");
        if(args.length == 3) {
            String domainName = args[1];
            DnsPayload.RecordType recordType;
            try {
                 recordType = DnsPayload.RecordType.valueOf(args[2]);
            } catch (Exception e) {
                publishProgress("Invalid record type\n");
                return null;
            }
            String response;
            try {
                response = nslookup.run(domainName, recordType);
            } catch (Exception e) {
                publishProgress("Nslookup error\n");
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                publishProgress(response);
            }
        }
        else {
            publishProgress("Usage: nslookup domainName recordType\n");
        }

        return null;
    }

}
