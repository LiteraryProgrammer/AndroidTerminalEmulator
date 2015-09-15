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
//        this.registerCommand(Traceroute.class);
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
        final TextView tv2 = (TextView) ma.findViewById(R.id.textView);
        //todo: przerobic wszystkie extracommands na async task?
//        TestingWriterAsyncTask testingWriterAsyncTask = new TestingWriterAsyncTask();
//        testingWriterAsyncTask.execute(tv2);

        NativeCommand nativeCommand = new NativeCommand("ping -c 3 8.8.8.8");
        nativeCommand.onPreExecute(tv2);
        nativeCommand.execute();




        if(a==5) {
            return;
        }


    }


}
