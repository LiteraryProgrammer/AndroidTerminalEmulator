package com.example.grzegorz.androidterminalemulator;

import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
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
/*
    @Override
    public Boolean allFinished() throws IOException {
        if(this.getState() == State.TERMINATED && is.available() == 0 && es.available() == 0) {
            return true;
        }
        return false;
    }
*/
    private Runtime runtime = null;
    private TextView tv = null;
    private Process process = null;

    @Override
    public void cancel() {
        process.destroy();
    }

    //todo: refactor arguments
    boolean isRunning(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected void onPreExecute(TextView view) {
        super.onPreExecute();
        tv = view;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //todo: IMPLEMENT AS ASYNC TASK!!!!
        runtime = Runtime.getRuntime();
        try {
            process = runtime.exec(cmd.split(" "));
            is = process.getInputStream();
            es = process.getErrorStream();
            os = process.getOutputStream();

            //todo: necessary?
            synchronized (this) {
                notify();
            }

            int a= 5;

            InputStreamReader inputStreamReader = new InputStreamReader(is);
//            while(is.available() != 0) {
//            while(is.available() != 0 || isRunning(process)) {
            while(inputStreamReader.ready() || isRunning(process)) {
                char c = (char) inputStreamReader.read();
                publishProgress(String.valueOf(c));
            }
            publishProgress("END OF COMMAND");

//            tu skonczylem refaktoryzacje na async task!!
            process.waitFor();
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
        Log.d("LETTER", (String) values[0]);
        tv.append((String) values[0]);
    }
}
