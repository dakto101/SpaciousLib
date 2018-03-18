package org.anhcraft.spaciouslib.command;

public class CommandArgument {
    public enum Type{
        CUSTOM,
        URL,
        EMAIL,
        ONLINE_PLAYER,
        INTEGER_NUMBER,
        REAL_NUMBER,
        BOOLEAN,
        WORLD
    }

    private String name;
    private CommandRunnable runnable;
    private boolean optional;

    public CommandArgument(String name, CommandRunnable runnable, boolean optional) throws Exception {
        name = name.trim();
        if(name.split(" ").length != 1){
            throw new Exception("Invalid argument name!");
        }
        this.name = name.toLowerCase();
        this.runnable = runnable;
        this.optional = optional;
    }

    public String getName(){
        return this.name;
    }

    public CommandRunnable getRunnable(){
        return this.runnable;
    }

    public boolean isOptional(){
        return this.optional;
    }
}
