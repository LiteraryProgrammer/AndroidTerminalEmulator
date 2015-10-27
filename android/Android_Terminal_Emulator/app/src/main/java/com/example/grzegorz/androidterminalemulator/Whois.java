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
import java.net.InetSocketAddress;
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
    public Boolean finished() {
        return true;
    }

    @Override
    public void cancel() {

    }

    @Override
    protected Object doInBackground(Object[] params) {
        String domainName = "";
        String whoisServer = "193.59.201.49"; //default value
        String[] args = cmd.split(" ");

        if (args.length == 2) {
            domainName = args[1];
        } else if (args.length == 3) {
            domainName = args[1];
            whoisServer = args[2];
        }

        if (args.length == 1 || args.length > 3) {
            publishProgress("usage: whois domainname [whois server]");
            return null;
        }

        try {
            InetAddress serverAddr = InetAddress.getByName(whoisServer);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddr, 43), 5000);

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
            publishProgress(String.valueOf(buf));

        } catch (Exception e) {
            publishProgress(e.toString());
            e.printStackTrace();
        }
        publishProgress("\n");
        return null;
    }
}
