package com.example.grzegorz.androidterminalemulator;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by grzegorz on 11.05.15.
 */
public class TestingWriter extends ExtraCommand {

    public TestingWriter(String cmd) {
        super(cmd);
    }

    private Boolean finishedFlag = false;

    @Override
    public Boolean allFinished() throws IOException {
        return finishedFlag;
    }

    @Override
    public void run() {

        is = new PipedInputStream();
        PipedOutputStream pos = null;
        OutputStreamWriter osw = null;
        try {
            pos = new PipedOutputStream((PipedInputStream) is);
            osw = new OutputStreamWriter(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for(int i = 0 ; i < 10; i++) {
                osw.write("asdf");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finishedFlag = true;

        synchronized (this) {
            notify();
        }
        synchronized (this) {
            notify();
        }
        synchronized (this) {
            notify();
        }


    }
}
