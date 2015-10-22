package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.dns.DnsPayload;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by grzegorz on 12.05.15.
 */

public class Nslookup extends ExtraCommand {

    private TextView tv = null;

    public Nslookup(String cmd) {
        super(cmd);
    }

    public Boolean finished() {
        return true; //todo
    }

    @Override
    protected void onPreExecute(TextView view, ArrayBlockingQueue queue) {
        tv = view;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        com.example.grzegorz.androidterminalemulator.dns.Nslookup nslookup = new com.example.grzegorz.androidterminalemulator.dns.Nslookup();
        //todo: refactor arguments
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
            String response = null;
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
            publishProgress("Invalid arguments\n");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }
}
