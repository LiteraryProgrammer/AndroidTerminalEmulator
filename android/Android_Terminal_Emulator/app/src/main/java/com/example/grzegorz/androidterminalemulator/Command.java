package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by grzegorz on 11.05.15.
 */
public abstract class Command extends AsyncTask {

    String cmd;
    public Command(String cmd) {
        this.cmd = cmd;
    }

    protected TextView tv;
    protected ScrollView sv;

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (tv != null && sv != null) {
            tv.append((String) values[0]);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    protected void onPreExecute(TextView view, ScrollView scrollView) {
        tv = view;
        sv = scrollView;
    }

    protected InputStream is = null;
    protected OutputStream os = null;
    protected InputStream es = null;

    public OutputStream getOutputStream() {
        return os;
    }

    public abstract Boolean finished();

    public abstract void cancel();
}
