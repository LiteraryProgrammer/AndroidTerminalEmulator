package com.example.grzegorz.androidterminalemulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {


    Button executeButton;
    Button cancelButton;
    EditText et;
    TextView tv;
    ScrollView sv;
    ChangeDirectory cd;

    //todo: indicator that command inputStream running
    //todo: autoscroll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity ma = this;

        executeButton = (Button) findViewById(R.id.executebutton);
        cancelButton = (Button) findViewById(R.id.cancelbutton);
        et = (EditText) findViewById(R.id.editText);
        tv = (TextView) findViewById(R.id.textView);
        sv = (ScrollView) findViewById(R.id.scrollView);
        cd = new ChangeDirectory(tv, sv);

        final CommandExecutor ce = new CommandExecutor(ma);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    ce.cancelCommand();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = et.getText().toString();

                if (command.length() != 0) {

                    et.setText(""); //clean input


                    if (command.startsWith("cd") && !ce.getIsRunning()) {
                        cd.changeWorkingDirectory(command);
                    }
                    else if(command.equals("clear")) {
                        tv.setText(""); //clear terminal
                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    else {
                        if (ce.getIsRunning()) {
                            ce.write(command + "\r\n");
                        } else {
                            try {
                                tv.append("\n" + command + "\n");
                                sv.fullScroll(ScrollView.FOCUS_DOWN);
                                ce.executeCommand(command, ma, cd.getCurrentWorkingDirectory());
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ExecutionException | IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
