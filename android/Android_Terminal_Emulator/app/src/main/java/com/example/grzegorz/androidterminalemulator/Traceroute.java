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
    private Runtime runtime;
    private Process process;

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

        //todo: obsluzyc co gdy nie dziala ping lub zly ip
        //todo: obsluzyc gdy dany router nie odpowiada dlugo
        //todo: implelemtn printing as FQDN!!!!!!!!!!!!!!!!!!!!!! - za pomoca rev dns
        //todo: TRACEROUTE UZYWA UDP A NIE PINGA - DO PRZEMYSLENIA
        //todo: uzywa dnsa do zrobienia requestu o nazwe domeny
        //todo: czy nslookup nie powinien zwracac klasy zamiast stringa?
        //todo: printowanie na biezaco nie dziala!!!!!!!!
        //todo: ip z args a nie na stale
        //todo: zrobic na asynctaskach
        //todo: zaimplelmtnwoac opcje tracerouta jakies?
        //todo: add arguments support and help info
        //todo: support fqdns
        //todo: globalny notyfikator ze komenda trwa

        publishProgress(cmd + "\n");

        String dstIP;
        //todo: refactor to seperate methods!!!! IMPORTANT
        try {
            dstIP = cmd.split(" ")[1]; //todo: temporary solution
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
