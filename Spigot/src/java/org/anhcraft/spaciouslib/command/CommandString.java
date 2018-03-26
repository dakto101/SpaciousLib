package org.anhcraft.spaciouslib.command;

import java.util.LinkedHashMap;

/**
 * This class uses to define the color codes for command string
 */
public abstract class CommandString {
    public enum Type {
        BEGIN_COMMAND,
        BEGIN_SUB_COMMAND,
        BEGIN_ARGUMENT,
        BEGIN_ARGUMENT_OPTIONAL,
        ARGUMENT_NAME,
        ARGUMENT_NAME_OPTIONAL,
        ARGUMENT_TYPE,
        ARGUMENT_TYPE_OPTIONAL,
        END_ARGUMENT,
        END_ARGUMENT_OPTIONAL,
        DESCRIPTION
    }

    private LinkedHashMap<Type, String> cmdstr = new LinkedHashMap<>();

    public void scs(Type type, String str){
        cmdstr.put(type, str);
    }
    
    public String gcs(Type type){
        return cmdstr.get(type);
    }

    public CommandString(){
        scs(Type.BEGIN_COMMAND, "&f/");
        scs(Type.BEGIN_SUB_COMMAND, "&f");
        scs(Type.BEGIN_ARGUMENT, "&a<");
        scs(Type.BEGIN_ARGUMENT_OPTIONAL, "&f[");
        scs(Type.ARGUMENT_NAME, "&a");
        scs(Type.ARGUMENT_NAME_OPTIONAL, "&a");
        scs(Type.ARGUMENT_TYPE, "&6:");
        scs(Type.ARGUMENT_TYPE_OPTIONAL, "&6:");
        scs(Type.END_ARGUMENT, "&a>");
        scs(Type.END_ARGUMENT_OPTIONAL, "&f]");
        scs(Type.DESCRIPTION, "&f:&b");
    }
}
