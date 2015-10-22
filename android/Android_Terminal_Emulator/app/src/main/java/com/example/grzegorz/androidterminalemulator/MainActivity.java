package com.example.grzegorz.androidterminalemulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {


    private String currentWorkingDirectory = "/";

    private void changeWorkingDirectory(String newWorkingDirectory) {
        //todo: check if new path is valid
        newWorkingDirectory = newWorkingDirectory.split("cd ")[1];
        if (newWorkingDirectory.startsWith("/")) {
            this.currentWorkingDirectory = newWorkingDirectory;
        }
        else if(newWorkingDirectory.equals("../") || newWorkingDirectory.equals("..")) {
            String[] splittedCWD = this.currentWorkingDirectory.split("/");
            String tmp= Joiner.on("/").join(Arrays.copyOf(splittedCWD, splittedCWD.length - 1)) + "/";
            this.currentWorkingDirectory = tmp;
        }
        else {
            this.currentWorkingDirectory = this.currentWorkingDirectory + newWorkingDirectory + "/";
        }
    }

    //todo: display prompt with current with CWD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity ma = this;

        Button executeButton = (Button) findViewById(R.id.executebutton);
        Button cancelButton = (Button) findViewById(R.id.cancelbutton);
        final EditText et = (EditText) findViewById(R.id.editText);
        final TextView tv = (TextView) findViewById(R.id.textView);
        final ScrollView sv = (ScrollView) findViewById(R.id.scrollView);

        final CommandExecutor[] ce = new CommandExecutor[1];

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    ce[0].cancelCommand(); //refactor?
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = et.getText().toString();

                if (command.length() != 0) {

                    et.setText(""); //clean input
                    tv.append("\n" + command + "\n");
                    sv.fullScroll(ScrollView.FOCUS_DOWN);

                    //todo: autoscorll
                    //todo: telnet, extract parameter

                    if (command.startsWith("cd")) {
                        changeWorkingDirectory(command);
                    } else {
                        if (ce[0] == null) {
                            ce[0] = new CommandExecutor(ma);
                        }

                        if (ce[0].getIsRunning()) {
                            ce[0].write(command + "\r\n");
                            //todo: add writing to terminal view to!!
                        } else {
                            try {
                                ce[0].executeCommand(command, ma, currentWorkingDirectory);
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
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    //todo: autoscrolling

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
