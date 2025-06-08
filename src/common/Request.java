package common;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String commandStrArg;
    private final Serializable commandObjArg;
    private final String login;
    private final String pass;


    public Request(String login, String pass,String commandName, String commandStrArg, Serializable commandObjArg) {
        this.login = login;
        this.pass = pass;
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
        this.commandObjArg = commandObjArg;
    }

    public Request(String login, String pass,String commandName, String commandStrArg) {
        this.login = login;
        this.pass = pass;
        this.commandName = commandName;
        this.commandStrArg = commandStrArg;
        this.commandObjArg = null;
    }




    public String getCommandName() {
        return commandName;
    }

    public String getCommandStrArg() {
        return commandStrArg;
    }

    public Serializable getCommandObjArg() {
        return commandObjArg;
    }
    public String getLogin() {
        return login;
    }
    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", commandStrArg='" + commandStrArg + '\'' +
                ", commandObjArg=" + commandObjArg +
                '}';
    }
}