package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.netstat.ConnectionType;
import com.example.grzegorz.androidterminalemulator.netstat.NetstatUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by grzegorz on 18.06.15.
 */

public class Netstat extends ExtraCommand {
    public Netstat(String cmd) {
        super(cmd);
    }

    private TextView tv = null; //todo: move to upper class

    @Override
    protected void onPreExecute(TextView view, ArrayBlockingQueue queue) {
        tv = view;
    }

    @Override
    public Boolean finished() {
        return true;
    } //todo: refactor

    @Override
    protected void onProgressUpdate(Object[] values) { //todo: move to upper class
        super.onProgressUpdate(values);
        tv.append((String) values[0]);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        //todo: add --help
        DefaultParser parser = new DefaultParser(); //todo: move to upper class or seperate function
        Options options = new Options();
        options.addOption("t",false,"tcp");
        options.addOption("u",false,"udp");
        options.addOption("r", false, "route");
        options.addOption("6",false,"ipv6");

        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, cmd.split(" "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //todo: both tcp6 and tcp

        if(commandLine.hasOption("t")) {
            publishProgress(NetstatUtils.getConnections(ConnectionType.TCP));
        }
        else if(commandLine.hasOption("u")) {
            publishProgress(NetstatUtils.getConnections(ConnectionType.UDP));
        }

        else if(commandLine.hasOption("r")) {
            publishProgress(NetstatUtils.getRouting());
        }

        else if(commandLine.hasOption("6")) {
            //todo: to be implemented
        }

        //todo: routing6

       //todo: setting finished flag
        return null;
    }


}
