package com.example.grzegorz.androidterminalemulator;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by grzegorz on 11.05.15.
 */
public abstract class Command extends AsyncTask {

    //tutaj umiescic wszystkie inne metody ktore byly tam

    String cmd;
    public Command(String cmd) {
        this.cmd = cmd;
    }

    protected InputStream is = null;
    protected OutputStream os = null;
    protected InputStream es = null;


    public InputStream getErrorStream() {
        return es;
    }
    public OutputStream getOutputStream() {
        return os;
    }

    //public abstract Boolean allFinished() throws IOException;

    public void cancel() {};


}
