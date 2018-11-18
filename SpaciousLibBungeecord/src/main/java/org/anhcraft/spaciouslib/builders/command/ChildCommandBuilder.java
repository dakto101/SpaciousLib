package org.anhcraft.spaciouslib.builders.command;

import org.anhcraft.spaciouslib.builders.ArrayBuilder;

public class ChildCommandBuilder {
    private ArrayBuilder array = new ArrayBuilder(Argument.class);

    /**
     * Append a path argument
     * @param name argument name
     * @param callback argument callback
     * @return this object
     */
    public ChildCommandBuilder path(String name, CommandCallback callback){
        array.append(new Argument(name, callback, "", null));
        return this;
    }

    /**
     * Append a path argument
     * @param name argument name
     * @param callback argument callback
     * @param explanation argument explanation
     * @return this object
     */
    public ChildCommandBuilder path(String name, CommandCallback callback, String explanation){
        array.append(new Argument(name, callback, explanation, null));
        return this;
    }

    /**
     * Append a variable argument
     * @param name argument name
     * @param callback argument callback
     * @param type type of argument
     * @return this object
     */
    public ChildCommandBuilder var(String name, CommandCallback callback, ArgumentType type){
        array.append(new Argument(name, callback, "", type));
        return this;
    }

    /**
     * Append a variable argument
     * @param name argument name
     * @param callback argument callback
     * @param type type of argument
     * @param explanation argument explanation
     * @return this object
     */
    public ChildCommandBuilder var(String name, CommandCallback callback, ArgumentType type, String explanation){
        array.append(new Argument(name, callback, explanation, type));
        return this;
    }

    public Argument[] build(){
        return (Argument[]) array.build();
    }
}
