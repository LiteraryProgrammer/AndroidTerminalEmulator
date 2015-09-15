package com.example.grzegorz.androidterminalemulator;

import android.util.Log;

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
/*
public class Whois extends ExtraCommand {
    public Whois(String cmd) {
        super(cmd);
    }

    @Override
    public Boolean allFinished() throws IOException {
        return null;
    }

    @Override
    public void run() {
        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName("193.59.201.49");
                Socket socket = new Socket(serverAddr, 43);

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                InputStreamReader isr = new InputStreamReader(is);
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write("onet.pl\r\n");
                osw.flush();
                char[] buf = new char[512];
                isr.read(buf);
                isr.close();
                osw.close();
                Log.d("RESPONSE", String.valueOf(buf));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
*/