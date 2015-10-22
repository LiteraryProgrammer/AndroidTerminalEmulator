package com.example.grzegorz.androidterminalemulator;

import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import junit.framework.Test;

import org.apache.commons.net.telnet.TelnetCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

/**
 * Created by grzegorz on 11.05.15.
 */
public class CommandExecutor {

    List<Class<? extends ExtraCommand>> extracommands = new ArrayList<>();

    public void registerCommand(Class<? extends ExtraCommand> extracommand) {
        extracommands.add(extracommand);
    }

    private MainActivity ma = null;
    public ArrayBlockingQueue<String> queue; //todo necessary?
    public Boolean isRunning = false;

    public CommandExecutor(MainActivity ma) {
        this.registerCommand(Traceroute.class);
        this.registerCommand(Nslookup.class);
        this.registerCommand(Whois.class);
        this.registerCommand(Telnet.class);
        this.registerCommand(Netstat.class);
        this.ma = ma;
        this.queue = new ArrayBlockingQueue<String>(16); //todo: 16
    }

    public Command command;
    private OutputStreamWriter osw;

    public void cancelCommand() throws InterruptedException {
        command.cancel();
    }

    public Boolean getIsRunning() {
        return command != null && !command.finished();
    }

    public void write(String text) {
        try {
            if(osw != null) {
                osw.write(text + "\r\n");
                osw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(final String cmd, final MainActivity ma) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, InterruptedException, ExecutionException {

        final TextView tv = (TextView) ma.findViewById(R.id.textView);

        //todo: move out of here, maybe to
        if(command != null && command.finished()) {
            int a = 5;
        };
        //todo


        String[] cmd_parts = cmd.split(" ");

        for (Class<? extends ExtraCommand> extracommand: extracommands) {
            String className = extracommand.getName().toLowerCase();
            if (className.endsWith(cmd_parts[0])) { // if equals zero argument in cmd (name of command)
                Constructor ctor = extracommand.getConstructor(String.class);
                ExtraCommand ec = (ExtraCommand) ctor.newInstance(cmd);
                command = ec;
                ec.onPreExecute(tv, queue);
                ec.execute();
                isRunning = true; //todo: remove
                try {
                    OutputStream outputStream = (OutputStream) ec.get();
                    if(outputStream != null) {
                        osw = new OutputStreamWriter(outputStream);
                        tv.append(osw.toString());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return;
            }
        }

        //if not extra command found use native

        NativeCommand nativeCommand = new NativeCommand(cmd);
        command = nativeCommand;
        isRunning = true;
        nativeCommand.onPreExecute(tv);
        nativeCommand.execute();
        nativeCommand.get();
        isRunning = false;
    }
}
