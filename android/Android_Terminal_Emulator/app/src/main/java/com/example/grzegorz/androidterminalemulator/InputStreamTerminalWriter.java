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
                //todo: streams are not closing?
                //todo: use callback on command finished?
                for (InputStreamReader streamReader : streamReaders) {
                    if (streamReader.ready()) {
                        Character inputChar = (char) streamReader.read();
                        if (inputChar == '\uFFFF') finished = true;
                        if (inputChar != -1) {
                            publishProgress(String.valueOf(inputChar));
                        }
                    }
                }
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        } catch (IOException e) {
            publishProgress(e.getMessage());
        }

        return null;
    }
}
