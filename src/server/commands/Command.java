package server.commands;

import common.Response;

import java.io.Serializable;


public abstract class Command {


    public abstract Response execute(String strArg, Serializable objArg, Integer userId);

    public abstract String getDescription();
}
