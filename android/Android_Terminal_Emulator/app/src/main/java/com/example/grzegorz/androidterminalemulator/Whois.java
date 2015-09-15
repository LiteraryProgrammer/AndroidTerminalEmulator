package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
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

    private TextView tv = null; //todo: move to upper class

    @Override
    protected void onPreExecute(TextView view) {
        tv = view;
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
            domainName = cmd.split(" ")[1]; //todo: temporary solution
        }
        catch (Exception e) {
            publishProgress("No arguments specified\n");
            return null;
        }

        try {
            InetAddress serverAddr = InetAddress.getByName("193.59.201.49"); //todo: parametrize whois server
            Socket socket = new Socket(serverAddr, 43);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(is);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(domainName + "\r\n");
            osw.flush();
            char[] buf = new char[512];
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
        return null;
    }

}
