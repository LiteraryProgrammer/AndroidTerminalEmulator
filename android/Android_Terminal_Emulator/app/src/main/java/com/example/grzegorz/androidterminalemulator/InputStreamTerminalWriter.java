package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by gpietrus on 20.10.15.
 */
public class InputStreamTerminalWriter extends AsyncTask {

    //todo: use this in all extra commands
    private TextView tv = null; //todo: move to upper class
    private InputStream is = null;

    protected void onPreExecute(TextView view, InputStream is) {
        this.tv = view;
        this.is = is;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        tv.append((CharSequence) values[0]);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        InputStreamReader isr = new InputStreamReader(is);
        while(true) {
            char c = 0;
            try {
                c = (char) isr.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = String.valueOf(c);
            publishProgress(str);
            }

//        return null;
    }
}
