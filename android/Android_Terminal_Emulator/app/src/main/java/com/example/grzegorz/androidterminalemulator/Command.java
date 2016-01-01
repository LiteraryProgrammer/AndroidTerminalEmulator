package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;

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
    protected StringBuilder outputStringBuilder;

    @Override
    protected void onProgressUpdate(Object[] values) {
        for (Object value : values) {
            if (tv != null && sv != null) {
                tv.append((String) value);
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
            if (outputStringBuilder != null) {
                outputStringBuilder.append((String) value);
            }
        }
    }

    protected void onPreExecute(TextView view, ScrollView scrollView) {
        tv = view;
        sv = scrollView;
    }

    protected void onPreExecute(StringBuilder outputStringBuilder) {
        this.outputStringBuilder = outputStringBuilder;
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
