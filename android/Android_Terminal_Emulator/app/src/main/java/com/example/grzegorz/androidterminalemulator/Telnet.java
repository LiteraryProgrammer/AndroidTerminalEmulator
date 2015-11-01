package com.example.grzegorz.androidterminalemulator;

import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
            os.close();
            es.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //rainmaker.wunderground.com
        telnet = new TelnetClient();

        String args[] = cmd.split(" ");

        int port = 23;

        if(args.length == 3) {
            port = Integer.parseInt(args[2]);
        }
        else if (args.length != 2) {
            publishProgress("usage: telnet ip [port]\n");
            return null;
        }

        String address = args[1];
        try {
            telnet.setConnectTimeout(10);
            telnet.connect(address, port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        is = telnet.getInputStream();
        os = telnet.getOutputStream();


        try {
            InputStreamReader isr = new InputStreamReader(is);
            OutputStreamWriter osw = new OutputStreamWriter(os);
        }
        catch(Exception e) {
            publishProgress("Error: Connection refused.");
            try {
                telnet.disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
        istw.onPreExecute(tv, sv , is);
        istw.execute();

        return os;
    }
}
