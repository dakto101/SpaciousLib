package org.anhcraft.spaciouslib.command;

/**
 * Represents a command argument implementation.
 */
public class CommandArgument {
    public enum Type{
        /**
         * A player can type anything they want
         */
        CUSTOM,
        /**
         * A player only can type a valid URL
         */
        URL,
        /**
         * A player only can type a valid email
         */
        EMAIL,
        /**
         * A player only can type a name of an online player
         */
        ONLINE_PLAYER,
        /**
         * A player only can type an integer number
         */
        INTEGER_NUMBER,
        /**
         * A player only can type a real number (and also an integer number)
         */
        REAL_NUMBER,
        /**
         * A player only can type a boolean (true, false)
         */
        BOOLEAN,
        /**
         * A player only can type a loaded world
         */
        WORLD,
        /**
         * A player only can type a valid UUID
         */
        UUID
    }

    private String name;
    private CommandRunnable runnable;
    private boolean optional;

    /**
     * Creates a new command argument instance for SubCommandBuilder
     * @param name the name of this argument
     * @param runnable a runnable
     * @param optional if yes, players can skip this argument
     * @throws Exception
     */
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
