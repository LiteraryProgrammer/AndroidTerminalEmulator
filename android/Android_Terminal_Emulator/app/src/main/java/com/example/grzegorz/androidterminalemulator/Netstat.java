package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.netstat.NetstatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by grzegorz on 18.06.15.
 */

public class Netstat extends ExtraCommand {
    public Netstat(String cmd) {
        super(cmd);
    }

    private TextView tv = null; //todo: move to upper class

    @Override
    protected void onPreExecute(TextView view, ArrayBlockingQueue queue) {
        tv = view;
    }

    @Override
    public Boolean finished() {
        return true;
    } //todo: refactor

    @Override
    protected void onProgressUpdate(Object[] values) { //todo: move to upper class
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            publishProgress(NetstatUtils.getRouting());
            //todo: set finished flag
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
