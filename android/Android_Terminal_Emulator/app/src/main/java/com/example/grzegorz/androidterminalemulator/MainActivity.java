package com.example.grzegorz.androidterminalemulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.net.whois.WhoisClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final MainActivity ma = this;


        Button bt = (Button) findViewById(R.id.executebutton);
        Button bt2 = (Button) findViewById(R.id.cancelbutton);
        final EditText et = (EditText) findViewById(R.id.editText);

        final CommandExecutor[] ce = new CommandExecutor[1];

        bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    ce[0].cancelCommand(); //refactor?
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ce[0] = new CommandExecutor(ma);
                try {

                    ce[0].executeCommand(et.getText().toString(), ma);


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });




        /*ArrayList<String> commandLine = new ArrayList<String>();
        commandLine.add("ping 8.8.8.8");

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commandLine.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert process != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));


        StringBuilder builder = new StringBuilder();
        String aux = "";

        try {
            while ((aux = bufferedReader.readLine()) != null) {
                builder.append(aux);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = builder.toString();
        String line = text;

        Log.d("LOG LINE", line);

        TextView textview = (TextView) findViewById(R.id.hellotext);
        textview.setText(line);

*/

        /*
        Process sh = null;
        try {
            sh = Runtime.getRuntime().exec("/system/bin/screencap", null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sh.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        InputStream is = sh.getInputStream();

        InputStreamReader isr = new InputStreamReader(is);
        char buf[] = new char[999999];
        try {
            isr.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String a = new String(buf);
        Log.d("LOG LINE",a);
*/

/*
        //in mac oxs
        String command = "topad";
        //in windows
        //String command = "ping -n 3 " + domainName;

        String output = executeCommand(command);
*/



        /*
        working

        //todo: live output
        //todo: native commands without adding
        //todo: do commands as async tasks

        NativeCommand_dep ping = new NativeCommand_dep("ping","ping");
        NativeCommand_dep df = new NativeCommand_dep("df","df");
        NativeCommand_dep ls = new NativeCommand_dep("ls","ls");
        NativeCommand_dep traceroute = new NativeCommand_dep("traceroute","traceroute");
        NativeCommand_dep nslookup = new NativeCommand_dep("netstat","netstat");

        CommandsManager_dep cm = new CommandsManager_dep();
        cm.registerCommand(ping);
        cm.registerCommand(df);
        cm.registerCommand(ls);
        cm.registerCommand(traceroute);
        cm.registerCommand(nslookup);

        Log.d("TESTING REGISTER", cm.getAvailableCommands());

        Command_dep my = cm.getCommand("ping");

        List<String> args = new ArrayList<String>();
        args.add("-c");
        args.add("3");
        args.add("8.8.8.8");

        my.execute(args);

        Log.d("TESTING RET VALUE", String.valueOf(my.getRetValue()));
        Log.d("TESTING OUTPUT", my.getOutput());

        Command_dep my2 = cm.getCommand("netstat");
        List<String> args2 = new ArrayList<>();
        args2.add("46.4.242.141");
        my2.execute(args2);

        Log.d("TESTING RET VALUE", String.valueOf(my2.getRetValue()));
        Log.d("TESTING OUTPUT", my2.getOutput());


*/

       // executeCammand(); //todo: dobre
/*
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("sh -c ping");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        /*
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/





        /*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));


        StringBuilder builder = new StringBuilder();
        String aux = "";

        try {
            while ((aux = bufferedReader.readLine()) != null) {
                builder.append(aux);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = builder.toString();
        String line = text;

        Log.d("LOG LINE", line);

        TextView textview = (TextView) findViewById(R.id.hellotext);
        textview.setText(line);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/*
    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    private boolean executeCammand(){
        Runtime runtime = Runtime.getRuntime();
        try
        {
            //Process  mIpAddrProcess = runtime.exec("sh -c /system/bin/ping2");
            Process  mIpAddrProcess = runtime.exec("su -c /system/bin/ping");
            int mExitValue = mIpAddrProcess.waitFor();
            InputStream is = mIpAddrProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String out = br.readLine();
            Log.d("LOG LINE"," mExitValue "+mExitValue + " " + out);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            Log.d("LOG LINE"," Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("LOG LINE"," Exception:"+e);
        }
        return false;
    }

    */
}
