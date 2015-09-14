package com.example.grzegorz.androidterminalemulator;

import java.util.List;

/**
 * Created by grzegorz on 09.05.15.
 */
abstract public class Command_dep {


    protected String name;
    protected String path;
    protected int retvalue;
    protected String output;

    public Command_dep(String name, String path) {
        this.name = name;
        this.path = path;
    }

    //todo: abstract method execute?

    public int getRetValue() {
        return retvalue;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public abstract void execute(List<String> args);
}
