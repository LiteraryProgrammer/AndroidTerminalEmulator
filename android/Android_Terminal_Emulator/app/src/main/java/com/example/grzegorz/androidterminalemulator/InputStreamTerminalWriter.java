package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gpietrus on 20.10.15.
 */
public class InputStreamTerminalWriter extends AsyncTask {

    //todo: rename to full names
    private TextView tv = null;
    private InputStream is = null;
    private InputStream es = null;
    private ScrollView sv = null;
    private Boolean finished = false;

    protected void onPreExecute(TextView view, ScrollView sv, InputStream is, InputStream es) {
        this.tv = view;
        this.is = is;
        this.sv = sv;
        this.es = es;
    }

    public void kill() {
        finished = true;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (tv != null && sv != null) {
            tv.append((String) values[0]);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private Boolean allStreamsEmpty(List<InputStreamReader> streamReaders) throws IOException {
        Boolean empty = true;
        for (InputStreamReader streamReader : streamReaders) {
            if (streamReader.ready()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    private void delayedScroll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 250);
            }
        }).start();
    }

    private String readUntilEnd(InputStreamReader streamReader) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        while(streamReader.ready()) {
            stringBuilder.append((char) streamReader.read());
        }
        return stringBuilder.toString();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        List<InputStreamReader> streamReaders = new ArrayList<>();
        if (is != null) {
            streamReaders.add(new InputStreamReader(is));
        }
        if (es != null) {
            streamReaders.add(new InputStreamReader(es));
        }

        try {
            while (!finished || !allStreamsEmpty(streamReaders)) {
                for (InputStreamReader streamReader : streamReaders) {
                    String text = readUntilEnd(streamReader);
                    publishProgress(text);
                    if(text.endsWith(String.valueOf('\uFFFF'))) {
                        finished = true;
                    }
                }
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
            delayedScroll();
        } catch (IOException e) {
            publishProgress(e.getMessage());
        }

        return null;
    }
}
