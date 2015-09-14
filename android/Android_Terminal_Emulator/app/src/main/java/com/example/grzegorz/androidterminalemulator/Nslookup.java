package com.example.grzegorz.androidterminalemulator;

import java.io.IOException;

/**
 * Created by grzegorz on 12.05.15.
 */
public class Nslookup extends ExtraCommand {
    public Nslookup(String cmd) {
        super(cmd);
    }

    @Override
    public Boolean allFinished() throws IOException {
        return null;
    }



    @Override
    public void run() {

        String[] cmd_parts = cmd.split(" ");

        System.out.println("Do sth " + cmd);

    }
}
