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

    //todo: refactor arguments, move to upper class
    public boolean isRunning() {
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
        Log.d("PROCESS","DO IN BACKGROUND");
        runtime = Runtime.getRuntime();
        try {
            process = runtime.exec(cmd.split(" "));
            is = process.getInputStream();
            es = process.getErrorStream();
            os = process.getOutputStream();

            if(tv != null) { //todo: correct?
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                //todo: add error stream?
                while (inputStreamReader.ready() || isRunning()) {
                    char c = (char) inputStreamReader.read();
                    publishProgress(String.valueOf(c));
                }
            }

//            tu skonczylem refaktoryzacje na async task!!
            process.waitFor(); //todo: necessary?
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
            Log.d("LETTER", (String) values[0]);
            tv.append((String) values[0]);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
