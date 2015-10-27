package com.example.grzegorz.androidterminalemulator;

import android.widget.ScrollView;
import android.widget.TextView;

import com.example.grzegorz.androidterminalemulator.netstat.ConnectionType;
import com.example.grzegorz.androidterminalemulator.netstat.NetstatUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Created by grzegorz on 18.06.15.
 */

public class Netstat extends ExtraCommand {
    public Netstat(String cmd) {
        super(cmd);
    }

    //todo: --help
    //todo: addreses

    @Override
    public Boolean finished() {
        return true;
    } //todo: refactor

    @Override
    public void cancel() {

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

        char ipVersion;
        if(commandLine.hasOption("6")) {
            ipVersion = 6;
        }
        else {
            ipVersion = 4;
        }

        if(commandLine.hasOption("t")) {
            publishProgress(NetstatUtils.getConnections(ConnectionType.TCP, ipVersion));
        }
        if(commandLine.hasOption("u")) {
            publishProgress(NetstatUtils.getConnections(ConnectionType.UDP, ipVersion));
        }


        //todo: routing v6? !!!
        if(commandLine.hasOption("r")) {
            publishProgress(NetstatUtils.getRouting(ipVersion));
        }

       //todo: setting finished flag
        return null;
    }


}
