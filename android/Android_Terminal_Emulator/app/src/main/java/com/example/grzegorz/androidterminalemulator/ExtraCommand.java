package com.example.grzegorz.androidterminalemulator;

import android.widget.TextView;

import java.io.IOException;

/**
 * Created by grzegorz on 11.05.15.
 */
public abstract class ExtraCommand extends Command {
    public ExtraCommand(String cmd) {
        super(cmd);
    }

    //todo: refactor, move to upper class
    protected abstract void onPreExecute(TextView view);

    @Override
    protected abstract Object doInBackground(Object[] params);

}
