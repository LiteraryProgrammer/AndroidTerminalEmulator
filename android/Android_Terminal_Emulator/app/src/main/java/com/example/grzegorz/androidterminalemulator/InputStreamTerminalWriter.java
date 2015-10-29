package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by gpietrus on 20.10.15.
 */
public class InputStreamTerminalWriter extends AsyncTask {

    private TextView tv = null;
    private InputStream is = null;
    private ScrollView sv = null;

    protected void onPreExecute(TextView view, ScrollView sv, InputStream is) {
        this.tv = view;
        this.is = is;
        this.sv = sv;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (tv != null && sv != null) {
            tv.append((String) values[0]);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        InputStreamReader isr = new InputStreamReader(is);
        while(true) {
            char c = 0;
            try {
                c = (char) isr.read();
                //todo: system err?
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(c == '\uFFFF') break;
            String str = String.valueOf(c);
            publishProgress(str);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
            }

        return null;
    }
}
