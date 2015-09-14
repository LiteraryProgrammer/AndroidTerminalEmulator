package com.example.grzegorz.androidterminalemulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grzegorz on 09.05.15.
 */
public class CommandsManager_dep {


    private List<Command_dep> commands = new ArrayList<Command_dep>();

    public CommandsManager_dep() {
    }

    public void registerCommand(Command_dep command){
        commands.add(command);
    }

    public String getAvailableCommands() {
        return commands.toString();
    }

    public Command_dep getCommand(String name) {
        for (Command_dep command : commands) {
            if (command.name.equals(name)) {
                return command;
            }
        }
        return null; //no command found
    }


}
