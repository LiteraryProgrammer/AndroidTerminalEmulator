package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * Created by grzegorz on 11.05.15.
 */

//todo: live output
public class Traceroute extends ExtraCommand {

    private Boolean finishedFlag = false;
    private int PING_MAX_TTL = 64; //todo: verify
    private TextView tv = null;
    private ScrollView sv = null;
    private Runtime runtime;
    private Process process;

    public Boolean finished() {
        return true;
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

    @Override
    protected void onPreExecute(TextView view, ScrollView sv) {
        this.tv = view;
        this.sv = sv;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if(tv != null) {
            tv.append((String) values[0]);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    public boolean isRunning() {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        //todo: global notification - running
        //todo: support fqdns
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
        }
            catch (Exception e) {
                publishProgress("No arguments specified\n");
                return null;
            }

        for(int j = 1; j < PING_MAX_TTL; j++) {
            runtime = Runtime.getRuntime();
            try {
                process = runtime.exec(("ping -c 1 " + "-t " + j + " " + dstIP).split(" "));
                //todo: support all streams
                is = process.getInputStream();
                es = process.getErrorStream();
                os = process.getOutputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(is);
                StringBuilder stringBuilder = new StringBuilder();
                while (inputStreamReader.ready() || isRunning()) {
                    char c = (char) inputStreamReader.read();
                    stringBuilder.append(c);
                }

                String response = stringBuilder.toString();
                String ip;

                Matcher ttlExceededMatcher = ttlExceededPattern.matcher(response);
                Matcher noResponseMatcher = noResponsePattern.matcher(response);
                Matcher finalResponseMatcher = finalResponsePattern.matcher(response);

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
                    return null;
                }

                publishProgress(ip != null ?
                        j + ": " + ip + "\n" :
                        j + ": " + "no response\n");

                if (ip != null && ip.equals(dstIP)) break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
