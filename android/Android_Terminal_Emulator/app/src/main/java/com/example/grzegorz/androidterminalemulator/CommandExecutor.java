package com.example.grzegorz.androidterminalemulator;

import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import junit.framework.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grzegorz on 11.05.15.
 */
public class CommandExecutor {

    List<Class<? extends ExtraCommand>> extracommands = new ArrayList<>();

    public void registerCommand(Class<? extends ExtraCommand> extracommand) {
        extracommands.add(extracommand);
    }

    private MainActivity ma = null;

    public CommandExecutor(MainActivity ma) {
        this.registerCommand(Traceroute.class);
//        this.registerCommand(Nslookup.class);
//        this.registerCommand(Whois.class);
//        this.registerCommand(TestingWriter.class);
        this.ma = ma;
    }

    private Command command;

    public void cancelCommand() throws InterruptedException {
        command.cancel();
    }

    public void executeCommand(final String cmd, final MainActivity ma) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, InterruptedException {

        int a = 5;
        final TextView tv = (TextView) ma.findViewById(R.id.textView);

        String[] cmd_parts = cmd.split(" ");

        for (Class<? extends ExtraCommand> extracommand: extracommands) {
            String className = extracommand.getName().toLowerCase();
            if (className.endsWith(cmd_parts[0])) { // if equals zero argument in cmd (name of command)
                Constructor ctor = extracommand.getConstructor(String.class);
                ExtraCommand ec = (ExtraCommand) ctor.newInstance(cmd);
                //todo: necessary?
                command = ec;
                ec.onPreExecute(tv);
                ec.execute();
                //todo: implement extra command
                return;
            }
        }

        //if not extra command found use native

        NativeCommand nativeCommand = new NativeCommand(cmd);
        nativeCommand.onPreExecute(tv);
        nativeCommand.execute();

    }
}
