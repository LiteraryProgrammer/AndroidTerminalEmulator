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
        this.registerCommand(Nslookup.class);
        this.registerCommand(Whois.class);
        this.registerCommand(TestingWriter.class);
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
        TestingWriterAsyncTask testingWriterAsyncTask = new TestingWriterAsyncTask();
        testingWriterAsyncTask.execute(tv2);



        if(a==5) {
            return;
        }


//first look in list if commands exists

        final Handler handler = new Handler();
        final TextView tv = (TextView) ma.findViewById(R.id.textView);
        final ScrollView sv = (ScrollView) ma.findViewById(R.id.scrollView);

       // ma.handler.sendEmptyMessage(0);
        String[] cmd_parts = cmd.split(" ");

        for (Class<? extends ExtraCommand> extracommand: extracommands) {
            String className = extracommand.getName().toLowerCase();
            Log.d("RESPONSE",className + " " + cmd_parts[0]);
            if (className.endsWith(cmd_parts[0])) { // if equals zero argument in cmd (name of command)
                Constructor ctor = extracommand.getConstructor(String.class);
                final ExtraCommand ec = (ExtraCommand) ctor.newInstance(cmd);
                command = ec; //todo: refactor?
                ec.start();

                Log.d("ASDF", "EC START");

                synchronized (ec )  {
                    ec.wait();
                    Log.d("ASDF", "SYNCHRONIZED");
                }

                final InputStream is = ec.getInputStream();

                final InputStreamReader isr = new InputStreamReader(is);


//                while(!ec.allFinished()) {
                /*
                for(int i = 0; i < 100; i++) {
                    tv.append( "allFinished:" + ec.allFinished().toString() + "\n");

                    tv.append("available:" + String.valueOf(is.available()) + "\n");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((char) isr.read());
                    tv.append("read:" + stringBuilder.toString() + "\n");
                }
                */

                //todo: merge with down
                //TODO: WAZNE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                final Runnable r = new Runnable() {

                    @Override
                    public void run() {
                                /*synchronized (ec) {
                                    try {
                                        ec.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("ASDF", "SYNCHRONIZED2");

                                }*/
                        /*try {
                            tv.append(String.valueOf((char) isr.read()) + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            Log.d("TESTINGWRITERD","WHILE");
                            while(isr.ready()) {
                                tv.append(String.valueOf((char) isr.read()));
                            }
                            Log.d("TESTINGWRITERD","WHILE end");
                            //todo: nie wiem czemu blokuje wyswietlanie?
                            //todo: Zobac zyc czy z watku jak bede co 500 msw wppisal to czy bedzie sie prawidlowo wyswietlac czy moze caly watek bokuje
                            //todo: while is never ending
                            //TU SKONCZYLEM
                            Log.d("ASDF READY","FALSE!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //todo: jakies zakonczenie
                        try {
                            if(!ec.allFinished() || isr.ready()) {
                                handler.postDelayed(this, 10);
                            }
                            else {
                                tv.append("END\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                tv.append("\n" + cmd + "\n");
                handler.postDelayed(r,0);





/*                for(int i = 0; i < 200; i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((char) isr.read());
                    tv.append(stringBuilder.toString());
                }*/



                return;
            }
        }


        Log.d("RESPONSE","native");

        //if not execute native command
        final NativeCommand nc = new NativeCommand(cmd); //todo :Refactor?
        command = nc;


        nc.start();

        Log.d("NATIVE COMAND", "WAITING SYNC");

        synchronized (nc) {
            nc.wait();
        }
        //TU PROBLEM, notify wychodzi dobrze ale notify received dopiero tuz przed skonczeniem procesu
        Log.d("NATIVE COMMAND", "NOTIFY RECEIVED");


        final InputStream is = nc.getInputStream();
        final InputStream es = nc.getErrorStream();
        final OutputStream os = nc.getOutputStream();

        Log.d("NATIVE COMMAND STREAMS", is.toString() + es.toString() + os.toString());



        final Runnable r = new Runnable() {

            public void run() {
                //todo: WAZNE, USE READER http://stackoverflow.com/questions/13722945/append-inputstream-object-to-string-object
                Log.d("NATIVE COMMAND RUN", "START");

                try {
                    while(is.available() != 0) {
                        char c = (char) is.read();
                        tv.append(String.valueOf(c));

                    }
                    while(es.available() != 0) {
                        char c = (char) es.read();
                        tv.append(String.valueOf(c));

                    }
                    sv.fullScroll(sv.FOCUS_DOWN); //todo: repair scrolling
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("NATIVE COMMAND FINISHED", "CALL");
                try {
                    Log.d("NATIVE COMMAND FINISHED", nc.allFinished().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(!nc.allFinished()) { //if not finished wait for next text
                        handler.postDelayed(this, 10);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        tv.append("\n" + cmd + "\n");
        handler.postDelayed(r,0);


        return;
/*
        while(!nc.allFinished()) {
            while(is.available() != 0) {
                char c = (char) is.read();
                System.out.print(c);
                Log.d("OUTPUT", String.valueOf(c));
                ma.handler.sendEmptyMessage(0);


            }

            while(es.available() != 0) {
                char c = (char) es.read();
                System.out.print(c);
                ma.handler.sendEmptyMessage(0);



            }
            Thread.sleep(10000); //todo : better
        }*/


    }


}
