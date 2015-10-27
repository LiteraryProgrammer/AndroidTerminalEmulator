package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by grzegorz on 18.06.15.
 */

public class Whois extends ExtraCommand {
    public Whois(String cmd) {
        super(cmd);
    }

    @Override
    protected void onPreExecute(TextView view, ScrollView queue) {
        tv = view;
    }

    @Override
    public Boolean finished() {
        return true;
    }

    @Override
    public void cancel() {

    }

    @Override
    protected void onProgressUpdate(Object[] values) { //todo: move to upper class
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String domainName;
        try {
            //todo: refactor args
            domainName = cmd.split(" ")[1];
        }
        catch (Exception e) {
            publishProgress("No arguments specified\n");
            return null;
        }

        try {
            InetAddress serverAddr = InetAddress.getByName("193.59.201.49"); //todo: make whois server as parameter
            Socket socket = new Socket(serverAddr, 43);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(is);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(domainName + "\r\n");
            osw.flush();
            char[] buf = new char[4096]; //todo: refactor
            isr.read(buf);
            isr.close();
            osw.close();
            Log.d("RESPONSE", String.valueOf(buf));
            publishProgress(String.valueOf(buf));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        publishProgress("\n");
        return null;
    }
}
