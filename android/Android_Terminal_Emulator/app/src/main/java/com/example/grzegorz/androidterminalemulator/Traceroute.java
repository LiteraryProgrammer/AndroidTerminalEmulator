package com.example.grzegorz.androidterminalemulator;

import com.google.common.base.Joiner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grzegorz on 11.05.15.
 */

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
            if (pingRTTMatcher.matches()) {
                float time1 = Float.parseFloat(pingRTTMatcher.group(1));
                float time2 = Float.parseFloat(pingRTTMatcher.group(2));
                float time3 = Float.parseFloat(pingRTTMatcher.group(3));
                return new float[]{time1, time2, time3};
            } else {
                return new float[]{};
            }
        }

    }

    private class Hostname {

        private String PTRResponseRegexp = "^(.*\\n)+(Payload:\\tDomain:\\t(.*)\\n)(.*\\n)+$";
        Pattern PTRResponsePattern = Pattern.compile(PTRResponseRegexp);
        String matchedHostname = null;

        String address;

        //note: parsing nslookup response may not work when nslookup response format got changed
        public Hostname(String ip) throws Exception {
            String splittedAddress[] = ip.split("\\.");
            if (splittedAddress.length != 4) {
                throw new Exception("invalid address");
            }

            Collections.reverse(Arrays.asList(splittedAddress));
            address = Joiner.on(".").join(splittedAddress).concat(".in-addr.arpa");
            Nslookup nslookup = new Nslookup("nslookup " + address + " PTR");

            PipedInputStream in = new PipedInputStream();
            final PipedOutputStream out = new PipedOutputStream(in);
            StringBuilder stringBuilder = new StringBuilder();
            nslookup.onPreExecute(stringBuilder);
            nslookup.doInBackground(null); //note: why execute() doesn't work


            String responseString = stringBuilder.toString();
            Matcher matcher = PTRResponsePattern.matcher(responseString);

            if (responseString.contains("Domain") && matcher.matches()) {
                matchedHostname = matcher.group(3);
            }
        }

        public String getMatchedHostname() {
            return matchedHostname;
        }

    }

    private Boolean finishedFlag = false;
    private int PING_MAX_TTL = 64;
    Process process;
    private String usage = "usage: traceroute ip [-t] [-f]\n-t - prints rtts\n-f - prints in fqdn format if possible\n";


    public Boolean finished() {
        return true;
    }

    @Override
    public void cancel() {

    }

    private String pingTtlExceededResponseRegexp = "PING .*\\nFrom .*(\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b).*: .* Time to live exceeded(.*\\n)+";

    //    private String pingTtlExceededResponseRegexp = "PING .*\\nFrom .*: .* Time to live exceeded(.*\\n)+";
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


    //todo: traceroute nie zawsze podjae domene hostname
    @Override
    protected Object doInBackground(Object[] params) {

        final PipedInputStream totalIs = new PipedInputStream();
        Boolean measureRTT = false;
        Boolean findHostname = false;
        String dstIP = null;
        String[] args = cmd.split(" ");

        DefaultParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("t", false, "print rtts");
        options.addOption("f", false, "convert to fqdns");

        if (args.length < 2) {
            publishProgress(usage);
            return null;
        }
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        if (commandLine.hasOption("t")) {
            measureRTT = true;
        }

        if (commandLine.hasOption("f")) {
            findHostname = true;
        }


        if (args.length > 1) {
            try {
                dstIP = InetAddress.getByName(args[1]).getHostAddress(); //convert to ip if got fqdn
            } catch (UnknownHostException e) {
                publishProgress(usage);
                e.printStackTrace();
                return null;
            }
        }


        final String finalDstIP = dstIP;
        final Boolean finalMeasureRTT = measureRTT;
        final Boolean finalFindHostname = findHostname;
        new Thread(new Runnable() {
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
                        String responseAddr;

                        Matcher ttlExceededMatcher = ttlExceededPattern.matcher(response);
                        Matcher noResponseMatcher = noResponsePattern.matcher(response);
                        Matcher finalResponseMatcher = finalResponsePattern.matcher(response);


                        if (ttlExceededMatcher.matches()) {
                            responseAddr = ttlExceededMatcher.group(1); // first group = ip
                        } else if (noResponseMatcher.matches()) {
                            responseAddr = null;
                        } else if (finalResponseMatcher.matches()) {
                            responseAddr = finalResponseMatcher.group(1);
                        } else {
                            out.write("invalid response\n".getBytes());
                            continue;
                        }

                        if (responseAddr == null) {
                            out.write("no response\n".getBytes());
                            continue;
                        }
                        String hostname = null;
                        if (finalFindHostname) {
                            hostname = new Hostname(responseAddr).getMatchedHostname();
                        }

                        StringBuilder rrtsStrBuilder = new StringBuilder();
                        if (finalMeasureRTT) {
                            float rtts[] = new RTT(responseAddr).getRTT();
                            for (float rtt : rtts) {
                                rrtsStrBuilder.append(rtt + " ms\t");
                            }
                        }

                        String entryName;
                        if (finalFindHostname && hostname != null) {
                            entryName = hostname;
                        } else {
                            entryName = responseAddr;
                        }

                        String rrtsStr = finalMeasureRTT ? rrtsStrBuilder.toString() : "";
                        out.write((entryName + "\t" + (rrtsStr.length() > 0 ? rrtsStr : "") + "\n").getBytes());

                        if (responseAddr.equals(finalDstIP)) {
                            break;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
        istw.onPreExecute(tv, sv, totalIs);
        istw.execute();

        return null;

    }
}
