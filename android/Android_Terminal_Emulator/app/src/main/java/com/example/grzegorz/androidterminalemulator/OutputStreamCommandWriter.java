package com.example.grzegorz.androidterminalemulator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by gpietrus on 21.10.15.
 */
public class OutputStreamCommandWriter {

    private OutputStream os;
    private OutputStreamWriter osw;

    public OutputStreamCommandWriter(OutputStream os) {
        this.os = os;
        this.osw = new OutputStreamWriter(os);
    }

    public void write(String text) {
        try {
            osw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
