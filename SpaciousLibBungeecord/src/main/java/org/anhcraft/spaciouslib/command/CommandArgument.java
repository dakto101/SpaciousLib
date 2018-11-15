package org.anhcraft.spaciouslib.command;

/**
 * Represents a command argument implementation.
 */
@Deprecated
public class CommandArgument {
    public enum Type{
        /**
         * The players can type anything they want
         */
        CUSTOM,
        /**
         * The players can only type a valid URL
         */
        URL,
        /**
         * The players can only type a valid email
         */
        EMAIL,
        /**
         * The players can only type a name of an online player
         */
        ONLINE_PLAYER,
        /**
         * The players can only type an integer
         */
        INTEGER,
        /**
         * The players can only type a real number (and also an integer number)
         */
        REAL_NUMBER,
        /**
         * The players can only type a boolean (true, false)
         */
        BOOLEAN,
        /**
         * The players can only type a valid UUID
         */
        UUID,
        /**
         * The players can only type a positive real number
         */
        POSITIVE_REAL_NUMBER,
        /**
         * The players can only type a negative real number
         */
        NEGATIVE_REAL_NUMBER,
        /**
         * The players can only type a positive integer
         */
        POSITIVE_INTEGER,
        /**
         * The players can only type a negative integer
         */
        NEGATIVE_INTEGER,
        /**
         * The players can only type a valid IP v4
         */
        IP_V4,
        /**
         * The players can only type a valid server name
         */
        SERVER,
    }

    private String name;
    private CommandRunnable runnable;
    private boolean optional;

    /**
     * Creates a new command argument instance for SubCommandBuilder
     * @param name the name of this argument
     * @param runnable a runnable
     * @param optional if yes, players can skip this argument
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
