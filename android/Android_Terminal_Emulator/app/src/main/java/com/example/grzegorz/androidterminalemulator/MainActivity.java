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
}
