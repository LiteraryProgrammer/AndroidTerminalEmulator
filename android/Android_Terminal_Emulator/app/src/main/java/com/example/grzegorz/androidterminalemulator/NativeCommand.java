package com.example.grzegorz.androidterminalemulator;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by grzegorz on 11.05.15.
 */
public class NativeCommand extends Command {

    public NativeCommand(String cmd) {
        super(cmd);

    }

    @Override
    public Boolean allFinished() throws IOException {
        if(this.getState() == State.TERMINATED && is.available() == 0 && es.available() == 0) {
            return true;
        }
        return false;
    }


    private Runtime runtime = null;
    private Process process = null;

    @Override
    public void cancel() {
        process.destroy();
    }


    @Override
    public void run() {

            runtime = Runtime.getRuntime();

            try {
                process = runtime.exec(cmd.split(" "));
                is = process.getInputStream();
                es = process.getErrorStream();
                os = process.getOutputStream();
                Log.d("BLABLA", cmd);
                Log.d("NATIVE COMMAND ", "NOTIFY");
                Log.d("NATIVE COMMAND", is.toString() + es.toString() + os.toString());
                synchronized (this) {
                    notify();
                }

                process.waitFor();


                //System.out.println("koniec procesu");


            } catch (IOException e) {
                Log.d("RUNTIME", "ERROR IN RUNTIME EXEC");
                es = new ByteArrayInputStream("No such command".getBytes());
                is = new ByteArrayInputStream("".getBytes());

                notify();
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }





    }
}
