package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by gpietrus on 20.10.15.
 */
public class OutputStreamTerminalReader_depracted extends AsyncTask {

    //todo: use this in all extra commands

    private OutputStream os;

    protected void onPreExecute(OutputStream os) {
        this.os = os;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        /*
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
*/
        return null;
    }
}
