package com.example.grzegorz.androidterminalemulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by grzegorz on 09.05.15.
 */
public class NativeCommand_dep extends Command_dep {

    public NativeCommand_dep(String name, String path) {
        super(name,path);
    }

    public void execute(List<String> args) {
        Runtime runtime = Runtime.getRuntime();
        try {
            //Process process = runtime.exec("sh -c " + this.path);
//todo: create own args because can be null
            args.add(0,this.path); //add command name/path in front

            String[] str_args = new String[args.size()];
            args.toArray(str_args);

            Process process = runtime.exec(str_args);
            this.retvalue = process.waitFor();

            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader bufferedreadererror = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder stringbuilder = new StringBuilder();

            String line = null;
            stringbuilder.append("STDO\n");

            while((line = bufferedreader.readLine()) != null) {
                stringbuilder.append(line).append("\n");
            }
            stringbuilder.append("STDE\n");
            while((line = bufferedreadererror.readLine()) != null) {
                stringbuilder.append(line).append("\n");
            }

            this.output = stringbuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
