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

    protected TextView textView;
    protected ScrollView scrollView;
    protected StringBuilder outputStringBuilder;
    protected InputStream inputStream = null;
    protected OutputStream outputStream = null;
    protected InputStream errorStream = null;

    @Override
    protected void onProgressUpdate(Object[] values) {
        for (Object value : values) {
            if (textView != null && scrollView != null) {
                textView.append((String) value);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
            if (outputStringBuilder != null) {
                outputStringBuilder.append((String) value);
            }
        }
    }

    protected void onPreExecute(TextView view, ScrollView scrollView) {
        textView = view;
        this.scrollView = scrollView;
    }

    protected void onPreExecute(StringBuilder outputStringBuilder) {
        this.outputStringBuilder = outputStringBuilder;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public abstract Boolean finished();

    public abstract void cancel();
}
