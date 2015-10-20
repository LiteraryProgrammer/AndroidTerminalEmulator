package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by gpietrus on 20.10.15.
 */
public class Telnet extends ExtraCommand {

    private TextView tv = null; //todo: move to upper class

    public Telnet(String cmd) {
        super(cmd);
    }

    @Override
    protected void onPreExecute(TextView view) {
        tv = view;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        publishProgress(cmd + "\n");

        TelnetClient telnet;

        telnet = new TelnetClient();

        try {
            telnet.connect("rainmaker.wunderground.com", 23);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Utils.readWrite(telnet.getInputStream(), telnet.getOutputStream(),
//        System.in, System.out);

        InputStream is = telnet.getInputStream();
        OutputStream os = telnet.getOutputStream();

        InputStreamReader isr = new InputStreamReader(is);
        OutputStreamWriter osw = new OutputStreamWriter(os);


        InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
        istw.onPreExecute(tv, is);
        istw.execute();


        try {
            osw.write("\r\n");
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tu skonczylem, rozkminic to na ssh telnet i spr czy dziala!

//        try
//        {
//            telnet.disconnect();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }


        //
        return null;
    }
}
