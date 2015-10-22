package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by grzegorz on 11.05.15.
 */
public class NativeCommand extends Command {

    public NativeCommand(String cmd) {
        super(cmd);
    }

    public Boolean finished() {
        return !isRunning();
    }

    private Runtime runtime = null;
    private TextView tv = null;
    private ScrollView sv = null;
    private String currentWorkingDirectory;
    private Process process = null;

    @Override
    public void cancel() {
        process.destroy();
    }

    //todo: refactor arguments, move to upper class
    public boolean isRunning() {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected void onPreExecute(TextView view, ScrollView sv, String currentWorkingDirectory) {
        super.onPreExecute();
        this.tv = view;
        this.sv = sv;
        this.currentWorkingDirectory = currentWorkingDirectory;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        runtime = Runtime.getRuntime();
        try {
            String[] envp = null;

            process = runtime.exec(cmd, envp, new File(currentWorkingDirectory));
            is = process.getInputStream();
            es = process.getErrorStream();
            os = process.getOutputStream();

            if(tv != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                //todo: add error stream?
                while (inputStreamReader.ready() || isRunning()) {
                    char c = (char) inputStreamReader.read();
                    publishProgress(String.valueOf(c));
                }
            }

            process.waitFor(); //todo: check if necessary?
        } catch (IOException e) {
            es = new ByteArrayInputStream("No such command".getBytes());
            is = new ByteArrayInputStream("".getBytes());
            notify();
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    //todo: refactor args types
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if (tv != null) {
            tv.append((String) values[0]);
            sv.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
