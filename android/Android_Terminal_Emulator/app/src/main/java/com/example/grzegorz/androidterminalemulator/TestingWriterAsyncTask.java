package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static java.lang.Thread.sleep;

/**
 * Created by grzegorz on 11.05.15.
 */

public class TestingWriterAsyncTask extends AsyncTask<TextView,String,String> {

    private int a = 0;
    private TextView tv = null;;

    @Override
    protected String doInBackground(TextView ... views) {
        tv = views[0];
        String dstIP = "46.4.242.141";
        for(int j = 1; j < 64; j++) { //todo: specify 64 by parameter
            NativeCommand ping = new NativeCommand("ping -c 1 " + "-t " + j + " " + dstIP);
            ping.start();

            synchronized (ping) {
                try {
                    ping.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            final InputStream pingIs = ping.getInputStream();

            String output = "";
            try { //todo: zrobic inaczej? bo malo optymalne
                while(!ping.allFinished()) {
                    while(pingIs.available() != 0)  {
                        output += (char) pingIs.read();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            publishProgress(output);

        }




        return null;
    }

    @Override
    protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
        Log.d("PROGRESSUPDATE", String.valueOf(values[0]));
        tv.append(values[0] + "\n");

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("PROGRESSUPDATE","FINISH");
    }
}