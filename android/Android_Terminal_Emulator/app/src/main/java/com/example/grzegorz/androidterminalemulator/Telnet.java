package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by gpietrus on 20.10.15.
 */
public class Telnet extends ExtraCommand {

    private TextView tv = null; //todo: move to upper class
    private ArrayBlockingQueue queue; //todo: if necessary?
    private TelnetClient telnet;

    public Telnet(String cmd) {
        super(cmd);
    }

    @Override
    protected void onPreExecute(TextView view, ArrayBlockingQueue queue) {
        this.tv = view;
        this.queue = queue;
    }

//    @Override
//    protected OutputStream onPostExecute() {
//        return os;
//    }


    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }

    public Boolean finished() {
        return !telnet.isConnected() && !telnet.isAvailable();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        publishProgress(cmd + "\n");

        telnet = new TelnetClient();

        try {
            telnet.connect("192.168.1.15", 23);
//            telnet.connect("rainmaker.wunderground.com", 23);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo: zrobic sink ktory bedzie pisal stad do istneiejaacego sink nizej
        //todo: czyli bufor w command executorze oprozniany przez proces tu gdy bedzie gotowy

//        Utils.readWrite(telnet.getInputStream(), telnet.getOutputStream(),
//        System.in, System.out);

        is = telnet.getInputStream();
        os = telnet.getOutputStream();

        InputStreamReader isr = new InputStreamReader(is);
        OutputStreamWriter osw = new OutputStreamWriter(os);


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
            } catch (IOException e) {
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

        //todo: disconnecting
        //
        return os; //todo: return both os and is and process then inside command executor
    }
}
