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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grzegorz on 11.05.15.
 */

//todo: live output
public class Traceroute extends ExtraCommand {

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

    //todo: osobny watek ktory wykonuje pingi i pisze do jednego duzego streama, i jego analizujemy

    @Override
    protected Object doInBackground(Object[] params) {

        final PipedInputStream totalIs = new PipedInputStream();
        String dstIP = null;
        String[] args = cmd.split(" ");
        if(args.length == 2) {
            dstIP = args[1];
        } else {
            publishProgress("usage: traceroute ip");
            return null;
        }
        final String finalDstIP = dstIP;
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
                        //todo: rest of streams?

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
                            out.write("invalid response\n".getBytes());
                            continue;
                        }

                        out.write((ip != null ? ip + "\n" : "no response\n").getBytes());

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

    protected Object depdoInBackground(Object[] params) {


        InputStreamTerminalWriter istr = new InputStreamTerminalWriter();

        PipedInputStream totalIs = new PipedInputStream();
        PipedOutputStream out = null;

        try {
            out = new PipedOutputStream(totalIs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        istr.onPreExecute(tv, sv, totalIs);
        istr.execute();


        //todo: global notification - running
        //todo: support fqdns
        //todo: validate ip
        //todo: traceroute options
        //todo: convert ip to fqdn
        //todo: use udp instead of ping?
        //todo: timeout
        //todo: ip validation
        //todo: refactor to seperate methods

        String dstIP;
        try {
            //todo: refactor args
            dstIP = cmd.split(" ")[1];
        } catch (Exception e) {
            publishProgress("No arguments specified\n");
            return null;
        }


        for (int j = 1; j < 1; j++) { //todo: temporrary disabled
//        for(int j = 1; j < PING_MAX_TTL; j++) {
//            runtime = Runtime.getRuntime();
            try {
//                process = runtime.exec(("ping -c 3 " + "-t " + j + " " + dstIP).split(" "));
//                process = runtime.exec(("ping -c 1 " + "-t " + j + " " + dstIP).split(" "));
                //todo: support all streams
                //todo: shorter timeout ?
                //todo: extra ? arguments for traceroute
                //todo: add argument that convert to fqdn
                //todo: use process builder
                //todo: redirect error to stdout
                //.redirectErrorStream(true)
                //todo: live output
//                is = process.getInputStream();
//                es = process.getErrorStream();
//                os = process.getOutputStream();

//                process.waitFor();
//                String response = IOUtils.toString(is);

                String response = "asdf";
                out.write(response.getBytes());

                if (5 < 6)
                    continue;

                //todo: temporary, create general inputtrac stream to stream all responses converted to traceroute response
//                InputStreamTerminalWriter istr = new InputStreamTerminalWriter();
//                istr.onPreExecute(tv, sv, is);
//                istr.execute();

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
                    publishProgress("invalid responsetra\n");
                    continue;
//                    Log.d("ERROR", "INVALID RESPONSE");
//                    return null;
                }

                publishProgress(ip != null ?
                        j + ": " + ip + "\n" :
                        j + ": " + "no response\n");

                if (ip != null && ip.equals(dstIP)) break;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
