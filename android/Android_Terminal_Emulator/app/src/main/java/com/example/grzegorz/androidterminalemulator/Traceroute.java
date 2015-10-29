package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grzegorz on 11.05.15.
 */

//todo: live output
public class Traceroute extends ExtraCommand {

    private class RTT {
        String ip;
        String pingRTTRegexp = "^PING .*\\n64 bytes from .* time=(.*) ms\\n64 bytes from .* time=(.*) ms\\n64 bytes from .* time=(.*) ms\\n\\n.*\\n.*\\n.*\\n$";
        Pattern pingRTTPattern = Pattern.compile(pingRTTRegexp);
        public RTT(String ip) {
            this.ip = ip;
        }
        public float[] getRTT() throws IOException, InterruptedException {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ping -c 3 " + this.ip);
            process.waitFor();
            String response = IOUtils.toString(process.getInputStream());
            Matcher pingRTTMatcher = pingRTTPattern.matcher(response);
            if(pingRTTMatcher.matches()){
                float time1 = Float.parseFloat(pingRTTMatcher.group(1));
                float time2 = Float.parseFloat(pingRTTMatcher.group(2));
                float time3 = Float.parseFloat(pingRTTMatcher.group(3));
                return new float[] {time1, time2, time3};
            } else {
                return new float[] {};
            }
        }

    }

    //todo: allow fqdn as input ip

    private Boolean finishedFlag = false;
    private int PING_MAX_TTL = 64;
    Process process; //todo: refactor


    public Boolean finished() {
        return true;
    }

    @Override
    public void cancel() {

    }

    //todo: add to pingTtlExceededResponseRegexp  - time to live exceeded
    //todo: add timeout
    //todo: extract timing
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

    public boolean isRunning() {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }


    //notes:
    @Override
    protected Object doInBackground(Object[] params) {

        final PipedInputStream totalIs = new PipedInputStream();
        Boolean measureRTT = false;
        String dstIP = null;
        String[] args = cmd.split(" ");

        if(args.length < 2) {
            publishProgress("usage: traceroute ip [-t]\n-t - prints rtts");
            return null;
        }

        if(args.length < 3) {
            dstIP = args[1];
        }
        else if(args.length == 3 && args[2].equals("-t")) {
            measureRTT = true;
        }


        //todo: add mesasure rtt arg

        final String finalDstIP = dstIP;
        final Boolean finalMeasureRTT = measureRTT;
        new Thread(new Runnable() { //todo: move to sepeerate class?
            @Override
            public void run() {
                try {

                    Runtime runtime = Runtime.getRuntime();
                    Process process;
                    PipedOutputStream out = new PipedOutputStream(totalIs);

                    for (int j = 1; j < PING_MAX_TTL; j++) {

                        out.write((j + ": ").getBytes());

                        process = runtime.exec(("ping -c 1 " + "-t " + j + " " + finalDstIP).split(" "));
                        process.waitFor();

                        is = process.getInputStream();

                        String response = IOUtils.toString(is);
                        String ip;

                        Matcher ttlExceededMatcher = ttlExceededPattern.matcher(response);
                        Matcher noResponseMatcher = noResponsePattern.matcher(response);
                        Matcher finalResponseMatcher = finalResponsePattern.matcher(response);


                        if (ttlExceededMatcher.matches()) {
                            ip = ttlExceededMatcher.group(1); // first group = ip
                        } else if (noResponseMatcher.matches()) {
                            ip = null;
                        } else if (finalResponseMatcher.matches()) {
                            ip = finalResponseMatcher.group(1);
                        } else {
                            out.write("invalid response\n".getBytes()); //todo? when?
                            continue;
                        }

                        if(ip == null) {
                            out.write("no response\n".getBytes());
                            continue;
                        }

                        StringBuilder rrtsStrBuilder = new StringBuilder();
                        if(finalMeasureRTT) {
                            if (ip != null) {
                                float rtts[] = new RTT(ip).getRTT();
                                for (int i = 0; i < rtts.length; i++) {
                                    rrtsStrBuilder.append(rtts[i] + " ms\t");
                                }
                            }
                        }
                        String rrtsStr = finalMeasureRTT ? rrtsStrBuilder.toString() : "";
                        out.write((ip + "\t" + (rrtsStr.length() > 0 ? rrtsStr : "") + "\n").getBytes());

                        if (ip != null && ip.equals(finalDstIP)) {
                            break;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
        istw.onPreExecute(tv, sv, totalIs);
        istw.execute();

        return null;

    }
        //todo: time parsing
        //todo: support fqdns
        //todo: validate ip
        //todo: traceroute options
        //todo: convert ip to fqdn
        //todo: use udp instead of ping?
        //todo: timeout
        //todo: ip validation
        //todo: refactor to seperate methods
        //todo: crash after traceroute

}
