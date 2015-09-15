package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * Created by grzegorz on 11.05.15.
 */

public class Traceroute extends ExtraCommand {

    private Boolean finishedFlag = false;
    private int PING_MAX_TTL = 64; //todo
    private TextView tv = null;

    //todo: add to pingTtlExceededResponseRegexp  - time to live exceeded
    //todo: obsluzyc gdy nie odpowiada bardzo dlugo - max limit
    //todo: dodac obsluge czasow!!!!!!!!!!!!!!!!!!!!!!!!! pomiary
    private String pingTtlExceededResponseRegexp =
            "PING .*\\n" +
            "From ([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}).*\n" +
            "\n" +
            ".*\n" +
            ".*\n" +
            ".*\n";

    private String pingFinalResponseRegexp = "PING .* \\(([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\) .*\\n.*\\n.*\\n.*\\n.*1 received, 0% packet loss.*\\n.*\\n";
    private String pingNoResponseRegexp = "PING .* \\(([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\) .*\\n.*\\n.*\\n.*100% packet loss.*\\n\\n";

    Pattern ttlExceededPattern = Pattern.compile(pingTtlExceededResponseRegexp);
    Pattern finalResponsePattern = Pattern.compile(pingFinalResponseRegexp);
    Pattern noResponsePattern = Pattern.compile(pingNoResponseRegexp);

    public Traceroute(String cmd) {
        super(cmd);
    }

    @Override
    protected void onPreExecute(TextView view) {
        tv = view;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if(tv != null) {
            tv.append((String) values[0]);
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        String dstIP = "46.4.242.141";
        //temporary //todo

        for(int j = 1; j < PING_MAX_TTL; j++) {
//            NativeCommand ping = new NativeCommand("ping -c 1 " + "-t " + j + " " + dstIP);
            NativeCommand ping = new NativeCommand("ls");
            ping.execute(); //todo: implement as async task in native command class


            final InputStream pingIs = ping.getInputStream();
            final InputStream pingEs = ping.getErrorStream();
            final OutputStream pingOs = ping.getOutputStream();

            //todo: support all streams
            InputStreamReader inputStreamReader = new InputStreamReader(pingIs);

            try {
                while(inputStreamReader.ready() || ping.isRunning()) {
                    char c = (char) inputStreamReader.read();
                    publishProgress(String.valueOf(c));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
/*
    @Override
    public void run() {

        String[] cmd_parts = cmd.split(" ");
        String dstIP = "46.4.242.141";

        for(int j = 1; j < 64; j++) { //todo: specify 64 by parameter

             //todo: implement to only one stream!!!!!!!!! wazne nie tylko w tym tylko wszedzie
            final InputStream pingIs = ping.getInputStream();
            final InputStream pingEs = ping.getErrorStream();
            final OutputStream pingOs = ping.getOutputStream();

            String output = "";
            try { //todo: zrobic inaczej? bo malo optymalne
                while(!ping.allFinished()) {
                    while(pingIs.available() != 0)  {
                        output += (char) pingIs.read();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //todo: obsluzyc co gdy nie dziala ping lub zly ip
            //todo: obsluzyc gdy dany router nie odpowiada dlugo
            //todo: implelemtn printing as FQDN!!!!!!!!!!!!!!!!!!!!!! - za pomoca rev dns
            //todo: TRACEROUTE UZYWA UDP A NIE PINGA - DO PRZEMYSLENIA
            //todo: uzywa dnsa do zrobienia requestu o nazwe domeny
            //todo: czy nslookup nie powinien zwracac klasy zamiast stringa?
            //todo: printowanie na biezaco nie dziala!!!!!!!!
            //todo: ip z args a nie na stale
            //todo: zrobic na asynctaskach

            String ip;
            Matcher ttlExceededMatcher = ttlExceededPattern.matcher(output);
            Matcher noResponseMatcher = noResponsePattern.matcher(output);
            Matcher finalResponseMatcher = finalResponsePattern.matcher(output);

            if(ttlExceededMatcher.matches()) {
                ip = ttlExceededMatcher.group(1); // first group = ip
            }
            else if(noResponseMatcher.matches()) {
                ip = null;
            }
            else if (finalResponseMatcher.matches()) {
                ip = finalResponseMatcher.group(1);
            }
            else {
                Log.d("ERROR", "INVALID RESPONSE");
                return;
            }

            try {
                osw.write(
                        ip != null ?
                        j + ": " + ip + "\n" :
                        j + ": " + "no response\n"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(ip != null && ip.equals(dstIP)) {
                finishedFlag = true;
                break; //if equals dotracn't ping more
            }

            synchronized (this) {
                notify();
            }
        }

        try {
            osw.close();
            pos.close();
            //todo: refactor, merge with previous synchornized
            synchronized (this) {
                notify();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Boolean allFinished() throws IOException {
        return finishedFlag;
    }
    */
}
