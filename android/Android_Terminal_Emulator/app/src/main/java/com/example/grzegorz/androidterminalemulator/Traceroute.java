package com.example.grzegorz.androidterminalemulator;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grzegorz on 11.05.15.
 */
public class Traceroute extends ExtraCommand {

    private Boolean finishedFlag = false;

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

    public Traceroute(String cmd) {
        super(cmd);
    }

    @Override
    public Boolean allFinished() throws IOException {
        return finishedFlag;
    }
/*
    private String extractIP(String pingResult) {
        int fromPosition = pingResult.indexOf("From");
        int icmpPosition = pingResult.indexOf("icmp_seq");
        int bytesPosition = pingResult.indexOf("64 bytes"); //only in last ping
        String ip;
        if (bytesPosition == -1) {
            ip = pingResult.substring(fromPosition + 5, icmpPosition - 1);
        }
        else { //if last
            ip = pingResult.substring(bytesPosition+14, icmpPosition - 2);
        }
        Log.d("TRACEROUTE IP",ip);
        return ip;
    }
*/
    @Override
    public void run() {

        String[] cmd_parts = cmd.split(" ");
        String dstIP = "46.4.242.141";
        Pattern ttlExceededPattern = Pattern.compile(pingTtlExceededResponseRegexp);
        Pattern finalResponsePattern = Pattern.compile(pingFinalResponseRegexp);
        Pattern noResponsePattern = Pattern.compile(pingNoResponseRegexp);

        //todo :Refactor
        is = new PipedInputStream();
        es = new PipedInputStream(); //todo: czy potrzebne
        PipedOutputStream pos = null;
        OutputStreamWriter osw = null;
        try {
            pos = new PipedOutputStream((PipedInputStream) is);
            osw = new OutputStreamWriter(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            notify();
        }

        for(int j = 1; j < 64; j++) { //todo: specify 64 by parameter

            NativeCommand ping = new NativeCommand("ping -c 1 " + "-t " + j + " " + dstIP);
            ping.start();

            synchronized (ping) {
                try {
                    ping.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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
}
