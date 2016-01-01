package com.example.grzegorz.androidterminalemulator;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;

/**
 * Created by gpietrus on 20.10.15.
 */

//parameters:
//-t tcp
//-u udp
//-r route
//-6 ipv6

public class Telnet extends ExtraCommand {

    private TelnetClient telnet;
    private final int connectTimeout = 10000;

    public Telnet(String cmd) {
        super(cmd);
    }

    public Boolean finished() {
        return !telnet.isConnected() && !telnet.isAvailable();
    }

    @Override
    public void cancel() {
        publishProgress("\nTERMINATED\n");
        try {
            telnet.disconnect();
            inputStream.close();
            outputStream.close();
            errorStream.close();
        } catch (IOException e) {
            publishProgress(e.getMessage());
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        telnet = new TelnetClient();

        String args[] = cmd.split(" ");

        int port = 23;

        if (args.length == 3) {
            port = Integer.parseInt(args[2]);
        } else if (args.length != 2) {
            publishProgress("usage: telnet ip [port]\n");
            return null;
        }

        String address = args[1];
        try {
            telnet.setConnectTimeout(connectTimeout);
            telnet.connect(address, port);
        } catch (Exception e) {
            publishProgress(e.getMessage());
        }

        if (!telnet.isConnected()) {
            publishProgress("\nError: Connection refused.");
            return null;
        }

        inputStream = telnet.getInputStream();
        outputStream = telnet.getOutputStream();

        InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
        istw.onPreExecute(textView, scrollView, inputStream);
        istw.execute();

        return outputStream;
    }
}
