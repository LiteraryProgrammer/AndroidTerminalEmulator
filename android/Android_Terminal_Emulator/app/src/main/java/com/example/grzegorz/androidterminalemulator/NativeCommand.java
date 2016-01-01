package com.example.grzegorz.androidterminalemulator;

import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;

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
    private String currentWorkingDirectory;
    private Process process = null;


    @Override
    public void cancel() {
        process.destroy();
    }

    //todo: refactor arguments, move to upper class
    public boolean isRunning() {
        if(process == null) {
            return false;
        }
        try {
            int i = process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected void onPreExecute(TextView view, ScrollView sv, String currentWorkingDirectory) {
        super.onPreExecute();
        this.textView = view;
        this.scrollView = sv;
        this.currentWorkingDirectory = currentWorkingDirectory;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        runtime = Runtime.getRuntime();
        try {
            String[] envp = null;

            process = runtime.exec(cmd, envp, new File(currentWorkingDirectory));
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            outputStream = process.getOutputStream();

            if(textView != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //todo: add error stream?

                //todo: for error stream: refactor?
                InputStreamTerminalWriter estw = new InputStreamTerminalWriter();
                estw.onPreExecute(textView, scrollView, errorStream);
                estw.execute();

                InputStreamTerminalWriter istw = new InputStreamTerminalWriter();
                istw.onPreExecute(textView, scrollView, inputStream);
                istw.execute();

            }

//            process.waitFor(); //todo: check if necessary?
        } catch (Exception e) {
//            errorStream = new ByteArrayInputStream("No such command".getBytes());
//            inputStream = new ByteArrayInputStream("".getBytes());
//            notify(); //todo?
            publishProgress("No such command.");
            e.printStackTrace();
        }
        return outputStream;
    }


    //todo: error stream that gives manual!! very importatnt

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
