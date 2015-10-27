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

    private TextView tv = null; //todo: move to upper class
    private ScrollView sv = null; //todo: move to upper class
    private ArrayBlockingQueue queue; //todo: necessary?
    private TelnetClient telnet;

    public Telnet(String cmd) {
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
        tv.append((String) values[0]);
        sv.fullScroll(ScrollView.FOCUS_DOWN);
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
        istw.onPreExecute(tv, is);
        istw.execute();

        /*
        while(a == 5) {
            try {
                String text = (String) queue.take();
                osw.write(text);
                osw.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException pine) {
                e.printStackTrace();
            }
        }*/

//        try {
//            osw.write("\r\n");
//            osw.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        tu skonczylem, rozkminic to na ssh telnet i spr czy dziala!

//        try
//        {
//            telnet.disconnect();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

        //todo:

//        w czesci teoretyczne
//                uzyte narzedzia
//                        opisac android
//                                i polecenia ktore sa dostepna ktore nie
//                i busybox
//                        inne aplikacje
//                                tekst na poczatek grudnia


        //todo: disconnecting
        //
        return os; //todo: return both os and is and process then inside command executor
    }
}
