package com.example.grzegorz.androidterminalemulator;

import com.google.common.base.Joiner;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
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
        //todo: parsing nslookup response may not work when nslookup response format got changed
        //todo: don't nslookup private ips
        public Hostname(String ip) throws Exception {
            String splittedAddress[] = ip.split("\\.");
            if(splittedAddress.length != 4) {
                throw new Exception("invalid address");
            }

            Collections.reverse(Arrays.asList(splittedAddress));
            address = Joiner.on(".").join(splittedAddress).concat(".in-addr.arpa");
            Nslookup nslookup = new Nslookup("nslookup " + address + " PTR");

            PipedInputStream in = new PipedInputStream();
            final PipedOutputStream out = new PipedOutputStream(in);
            StringBuilder stringBuilder = new StringBuilder();
            nslookup.onPreExecute(stringBuilder);
            nslookup.doInBackground(null); //todo: why execute() doesn't work, todo: null?


            String responseString = stringBuilder.toString();
            Matcher matcher = PTRResponsePattern.matcher(responseString);

            if(responseString.contains("Domain")) { //todo: temporary, merge

                if (matcher.matches()) {
                    matchedHostname = matcher.group(3);
                }
            }
        }

        public String getMatchedHostname() {
            return matchedHostname;
        }

        //todo: validate if good results
    }


    //todo: allow fqdn as input ip®

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


    //todo: what time does traceroute show? just 3 attemps of ping or any logic?
    //todo: failing after traceroute end
    //todo: fqdns as seperate argument?
    @Override
    protected Object doInBackground(Object[] params) {

        final PipedInputStream totalIs = new PipedInputStream();
        Boolean measureRTT = false;
        String dstIP = null;
        String[] args = cmd.split(" ");

        if (args.length < 2) {
            publishProgress("usage: traceroute ip [-t]\n-t - prints rtts\n");
            return null;
        }

        if (args.length > 1) {
            try {
                dstIP = InetAddress.getByName(args[1]).getHostAddress(); //convert to ip if got fqdn
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        if (args.length == 3 && args[2].equals("-t")) {
            measureRTT = true;
        }


        final String finalDstIP = dstIP;
        final Boolean finalMeasureRTT = measureRTT;
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
                            out.write("invalid response\n".getBytes()); //todo? when?
                            continue;
                        }

                        if (responseAddr == null) {
                            out.write("no response\n".getBytes());
                            continue;
                        }
                        Boolean findHostname = true; //todo as argument
                        //todo: check if already is not hostname
                        String hostname = new Hostname(responseAddr).getMatchedHostname();
                        //todo: add hostname as parameter / fqdn

                        StringBuilder rrtsStrBuilder = new StringBuilder();
                        if (finalMeasureRTT) {
                            float rtts[] = new RTT(responseAddr).getRTT();
                            for (float rtt : rtts) {
                                rrtsStrBuilder.append(rtt + " ms\t");
                            }
                        }

                        String entryName;
                        if(findHostname && hostname != null) {
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
