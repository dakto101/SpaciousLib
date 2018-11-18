package org.anhcraft.spaciouslib.builders.command;

import org.anhcraft.spaciouslib.utils.ExceptionThrower;

public class Argument {
    private String name;
    private CommandCallback callback;
    private String explanation;
    private ArgumentType type;

    /**
     * Create a new Argument instance
     * @param name argument name
     * @param callback argument callback
     * @param explanation argument explanation
     * @param type type of argument
     */
    public Argument(String name, CommandCallback callback, String explanation, ArgumentType type) {
        ExceptionThrower.ifTrue(name == null, new Exception("Argument name must not be null"));
        ExceptionThrower.ifTrue(callback == null, new Exception("Argument callback must not be null"));
        this.name = name;
        this.callback = callback;
        this.explanation = explanation == null ? "" : explanation;
        this.type = type;
    }

    public ArgumentType getType() {
        return type;
    }

    public String getExplanation() {
        return explanation;
    }

    public CommandCallback getCallback() {
        return callback;
    }

    public String getName() {
        return name;
    }

    /**
     * Check whether this is a path argument or not
     * @return true if yes
     */
    public boolean isPathArgument() {
        return type == null;
    }

    /**
     * Check whether the given string is valid
     * @param str string
     * @return true if it is valid
     */
    public boolean check(String str) {
        if(isPathArgument() && name.length() > 0){
            return str.equalsIgnoreCase(name);
        } else {
            return type == null || type.check(str.trim());
        }
    }
}
