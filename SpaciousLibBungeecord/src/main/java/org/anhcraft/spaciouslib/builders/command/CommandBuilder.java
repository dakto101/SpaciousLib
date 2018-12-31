package org.anhcraft.spaciouslib.builders.command;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.anhcraft.spaciouslib.builders.ArrayBuilder;
import org.anhcraft.spaciouslib.builders.ChatComponentBuilder;
import org.anhcraft.spaciouslib.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandBuilder {
    private String name;
    private List<String> aliases = new ArrayList<>();
    private String[] explanationData = new String[0];
    private Table<Argument> arguments = new Table<>(0, 0);
    private ErrorCallback errorCallback = new ErrorCallback() {
        @Override
        public void invalid(CommandBuilder builder, CommandSender sender, String[] args, int command, int arg) {
            Argument argument = getArgument(command, arg);
            System.out.println("Arg="+arg);
            System.out.println("Args="+String.join(" ", args));
            sender.sendMessage(Chat.color("&e(!) Invalid value &f'"+StringUtils.escape(args[arg])+"' &eat argument &f'"+argument.getName()+"' &e(index="+arg+"): "+argument.getType().getErrorMessage()));
            sender.sendMessage(Chat.color("&a(>) Please check again the command:"));
            sender.sendMessage(toTextComponent(command, true, true));
        }

        @Override
        public void notFound(CommandBuilder builder, CommandSender sender, String[] args) {
            sender.sendMessage(Chat.color("&c(!) Command not found!"));
        }
    };

    private SuggestionCallback suggestionCallback = new SuggestionCallback() {
        @Override
        public void run(CommandBuilder builder, CommandSender sender, String[] args, int suggestionCommand) {
            sender.sendMessage(Chat.color("&a(>) Perhaps you want this command?"));
            sender.sendMessage(toTextComponent(suggestionCommand, true, true));
        }
    };
    private PluginCommand command;

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param callback command callback
     */
    public CommandBuilder(String name, CommandCallback callback){
        this.name = name;
        addChild("", new Argument("", callback, "", null));
    }

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param callback command callback
     * @param explanation command explanation
     */
    public CommandBuilder(String name, CommandCallback callback, String explanation){
        this.name = name;
        addChild(explanation, new Argument("", callback, "", null));
    }

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param args arguments of root command
     */
    public CommandBuilder(String name, Argument[] args){
        ExceptionThrower.ifTrue(args.length == 0, new Exception("Command must have at least one argument"));
        this.name = name;
        addChild("", args);
    }

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param args arguments of root command
     * @param explanation command explanation
     */
    public CommandBuilder(String name, Argument[] args, String explanation){
        ExceptionThrower.ifTrue(args.length == 0, new Exception("Command must have at least one argument"));
        this.name = name;
        addChild(explanation, args);
    }

    /**
     * Add a child command.<br>
     * Warning: please put arguments in order: root arguments then path arguments then variable arguments
     * @param args arguments of child command
     * @return this object
     */
    public CommandBuilder addChild(Argument... args){
        return addChild("", args);
    }

    /**
     * Add a child command.<br>
     * Warning: please put arguments in order: root arguments then path arguments then variable arguments
     * @param args arguments of child command
     * @param explanation child command explanation
     * @return this object
     */
    public CommandBuilder addChild(String explanation, Argument... args){
        if(args.length > 0) {
            /*
            Check whether the order of arguments is correct
            - First range: one root argument
            - Second range: path arguments
            - Third range: variable arguments
            - Residual range: null arguments
             */
            int mode = 0;
            boolean oob = false;
            try {
                for(Argument arg : args) {
                    // when an argument is null, determine that the checking passed the third range
                    if(arg == null){
                        oob = true;
                    }
                    else {
                        if(oob){
                            // when an argument is not null, but the current range is the residual one, then this is an invalid situation
                            throw new Exception("Invalid ordinal: "+String.join(" ", Repeater.whileTrue(0, 1, new Repeater<String>() {
                                @Override
                                public String run(int current) {
                                    return args[current] == null ? "[NULL]" : args[current].getName();
                                }

                                @Override
                                public boolean check(int current) {
                                    return current < args.length;
                                }
                            })));
                        } else {
                            // ROOT AND PATH ARGUMENTS
                            if(arg.isPathArgument()) {
                                // ROOT
                                if(arg.getName().isEmpty()) {
                                    // we only allow one root argument
                                    if(mode == 0) {
                                        mode++;
                                    }
                                    // this is an invalid situation whenever the checking is not in the first range
                                    else {
                                        throw new Exception("Invalid ordinal: "+String.join(" ", Repeater.whileTrue(0, 1, new Repeater<String>() {
                                            @Override
                                            public String run(int current) {
                                                return args[current] == null ? "[NULL]" : args[current].getName();
                                            }

                                            @Override
                                            public boolean check(int current) {
                                                return current < args.length;
                                            }
                                        })));
                                    }
                                }
                                // PATH
                                else {
                                    // set the current range to the second
                                    if(mode == 0) {
                                        mode++;
                                    }
                                    // this is an invalid situation whenever the checking is in the third range
                                    else if(mode == 2) {
                                        throw new Exception("Invalid ordinal: "+String.join(" ", Repeater.whileTrue(0, 1, new Repeater<String>() {
                                            @Override
                                            public String run(int current) {
                                                return args[current] == null ? "[NULL]" : args[current].getName();
                                            }

                                            @Override
                                            public boolean check(int current) {
                                                return current < args.length;
                                            }
                                        })));
                                    }
                                }
                            }
                            // VARIABLE ARGUMENTS
                            // if the argument is a variable one, determine that the checking is in the third range
                            else if(mode < 2) {
                                mode = 2;
                            }
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                explanationData = (String[]) new ArrayBuilder(explanationData).append(explanation).build();
                // expand the table if needed
                if(arguments.columns() < args.length) {
                    int amount = args.length-arguments.columns();
                    int i = 0;
                    while(i < amount) {
                        arguments.addLastColumn();
                        i++;
                    }
                }
                arguments.addLastRow();
                arguments.set(arguments.columns(), arguments.rows() - 1, args);
            }
        }
        return this;
    }

    /**
     * Set error callback
     * @param errorCallback error callback
     * @return this object
     */
    public CommandBuilder setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }

    /**
     * Set suggestion callback
     * @param suggestionCallback error callback
     * @return this object
     */
    public CommandBuilder setSuggestionCallback(SuggestionCallback suggestionCallback) {
        this.suggestionCallback = suggestionCallback;
        return this;
    }

    /**
     * Format a command into string
     * @param commandIndex the index of command
     * @param color color the string or not
     * @return string
     */
    public String toString(int commandIndex, boolean color){
        return toString(commandIndex, color, false);
    }

    /**
     * Format a command into string
     * @param commandIndex the index of command
     * @param color color the string or not
     * @param showType show the type of arguments or not
     * @return string
     */
    public String toString(int commandIndex, boolean color, boolean showType){
        List<Argument> args = getCommandArgs(commandIndex);
        StringBuilder chat = new StringBuilder((color ? "&b/" : "/")+name);
        if(color) {
            for(Argument arg : args) {
                if(arg.getName().length() == 0){
                    continue;
                }
                chat.append(" ").append(arg.isPathArgument() ? "&e" : "&7<&a").append(arg.getName()).append(showType && arg.getType() != null ? "&f:" + arg.getType().toString() : "").append(arg.isPathArgument() ? "" : "&7>");
            }
            if(explanationData[commandIndex].length() > 0) {
                chat.append("&f: ").append(explanationData[commandIndex]);
            }
        } else {
            for(Argument arg : args) {
                if(arg.getName().length() == 0){
                    continue;
                }
                chat.append(" <").append(arg.getName()).append(showType && arg.getType() != null ? ":"+arg.getType().toString() : "").append(">");
            }
            if(explanationData[commandIndex].length() > 0) {
                chat.append(": ").append(explanationData[commandIndex]);
            }
        }
        return Chat.color(chat.toString());
    }

    /**
     * Format a command into component
     * @param commandIndex the index of command
     * @param color color the component or not
     * @return component
     */
    public TextComponent toTextComponent(int commandIndex, boolean color){
        return toTextComponent(commandIndex, color, false);
    }

    /**
     * Format a command into component
     * @param commandIndex the index of command
     * @param color color the component or not
     * @param showType show the type of arguments or not
     * @return component
     */
    public TextComponent toTextComponent(int commandIndex, boolean color, boolean showType){
        List<Argument> args = getCommandArgs(commandIndex);
        ChatComponentBuilder chat = new ChatComponentBuilder((color ? "&b/" : "/")+name);
        if(color) {
            for(Argument arg : args) {
                if(arg.getName().length() == 0){
                    continue;
                }
                chat.text(" " + (arg.isPathArgument() ? "&e" : "&7<&a") + arg.getName() + (showType && arg.getType() != null ? "&f:" + arg.getType().toString() : "") + (arg.isPathArgument() ? "" : "&7>"), new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new ChatComponentBuilder(arg.getExplanation()).build()}));
            }
            if(explanationData[commandIndex].length() > 0) {
                chat.text("&f: " + explanationData[commandIndex]);
            }
        } else {
            for(Argument arg : args) {
                if(arg.getName().length() == 0){
                    continue;
                }
                chat.text(" <" + arg.getName() + (showType && arg.getType() != null ? ":" + arg.getType().toString() : "") + ">", new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new ChatComponentBuilder(arg.getExplanation()).build()}));
            }
            if(explanationData[commandIndex].length() > 0) {
                chat.text(": " + explanationData[commandIndex]);
            }
        }
        return (TextComponent) chat.build();
    }

    /**
     * Get the number of commands
     * @return number of commands
     */
    public int getCommands(){
        return explanationData.length;
    }

    /**
     * Get all arguments of a command
     * @param commandIndex the index of command
     * @return list of arguments
     */
    public List<Argument> getCommandArgs(int commandIndex){
        return arguments.toListOfColumns(commandIndex).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Get the command explanation
     * @param commandIndex the index of command
     * @return command explanation
     */
    public String getCommandExplanation(int commandIndex){
        return explanationData[commandIndex];
    }

    /**
     * Get the argument of a command
     * @param command the index of command
     * @param arg the index of the argument
     * @return argument
     */
    public Argument getArgument(int command, int arg) {
        return arguments.get(arg, command);
    }

    /**
     * Send help messages to the target
     * @param sender the receiver
     * @param color color the messages or not
     * @param showType show the type of arguments or not
     * @return this object
     */
    public CommandBuilder sendHelpMessages(CommandSender sender, boolean color, boolean showType) {
        for(int i = 0; i < explanationData.length; i++) {
            sender.sendMessage(toTextComponent(i, color, showType));
        }
        return this;
    }

    /**
     * Build the executor and register the command for the given plugin
     * @param plugin plugin
     * @return this object
     */
    public CommandBuilder build(Plugin plugin){
        if(command != null){
            BungeeCord.getInstance().getPluginManager().unregisterCommand(command);
        }
        command = new PluginCommand(this.name) {
            @Override
            public void execute(CommandSender commandSender, String[] strings) {
                executor(commandSender, strings);
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
                return tabCompleter(strings);
            }
        };
        ReflectionUtils.setField("aliases", Command.class, command, CommonUtils.toArray(aliases, String.class));
        BungeeCord.getInstance().getPluginManager().registerCommand(plugin, command);
        return this;
    }

    /**
     * Clones this object
     * @param name new command name
     * @return new object
     */
    public CommandBuilder clone(String name){
        CommandBuilder cb = new CommandBuilder(name, (Argument[]) arguments.toArrayOfColumns(Argument.class, 0), explanationData[0]);
        cb.errorCallback = errorCallback;
        cb.suggestionCallback = suggestionCallback;
        cb.aliases = aliases;
        if(1 < arguments.rows()) {
            for(int i = 1; i < arguments.rows(); i++) {
                cb.addChild(explanationData[i], (Argument[]) arguments.toArrayOfColumns(Argument.class, i));
            }
        }
        return cb;
    }

    private List<String> tabCompleter(String[] args) {
        /*
        HOW DOES THE TAB COMPLETER WORK?
        - Loop though all path arguments
        - Checks all typed arguments except the last one, they must match with registered arguments
        - Add the last argument to the suggestion list
         */
        Argument arg; // store the current typed argument
        List<String> str = new ArrayList<>(); // list of suggestions
        for(int i = 0; i < arguments.rows(); i++){
            for(int j = 0; j < arguments.columns(); j++){
                arg = arguments.get(j, i);
                if(arg == null || !arg.isPathArgument() || arg.getName().length() == 0){
                    break;
                }
                // if there isn't any typed arguments
                if(args.length == 0) {
                    str.add(arg.getName());
                    break;
                }
                // check previous typed arguments
                if(j < args.length-1) {
                    // they must match with registered arguments
                    if(!arg.getName().equals(args[j])){
                        break;
                    }
                    continue;
                }
                // now this is the last argument, we will ensure it represent for an registered argument and then add the suggestion to the list
                else if(arg.getName().startsWith(args[j])) {
                    str.add(arg.getName());
                }
                break; // ignores remaining registered arguments
            }
        }
        return str;
    }

    private void executor(CommandSender sender, String[] args) {
        /*
        NOTE:
        - Calls all registered arguments (which is being in the table now) are X
        - Calls all typed arguments are Y
        - The index of X and Y are not always the same
         */

        // the command index in the table
        int command = -1;
        // the number of X
        // this is the actual number which doesn't include null values
        int regArgCount = 0;
        // the index of the last Y
        int typedArgIndex = -1;
        boolean lastArgValid = false;

        main:
        for(int i = 0; i < arguments.rows(); i++){
            //System.out.println("[>>] Command "+i);

            // ------temporary variables------
            int tempRegArgCount = 0;
            int tempTypedArgIndex = -1;
            boolean tempLastArgValid = false;
            // --------------------------------

            for(int j = 0; j < arguments.columns(); j++){
                Argument arg = arguments.get(j, i);
                //System.out.println("- Arg "+j);
                // stop the checking when there is a null argument
                if(arg == null){
                    //System.out.println("-- NULL => BREAK");
                    break;
                } else {
                    // PATH & ROOT ARGS
                    if(arg.isPathArgument()){
                        // ROOT ARGS
                        if(arg.getName().isEmpty()){
                            // approves it
                            tempRegArgCount++;
                            tempLastArgValid = true;
                            //System.out.println("-- ROOT (tempRegArgCount="+tempRegArgCount+")");
                        }
                        // PATH ARGS
                        else {
                            tempTypedArgIndex++;
                            if(tempTypedArgIndex < args.length &&
                                    (tempLastArgValid = arg.check(args[tempTypedArgIndex]))){
                                tempRegArgCount++;
                                //System.out.println("-- PATH: VALID (tempTypedArgIndex="+tempTypedArgIndex+",tempRegArgCount="+tempRegArgCount+")");
                            }
                            // if this path arg is invalid, we ignore this command
                            else {
                                //System.out.println("-- PATH: INVALID (tempTypedArgIndex="+tempTypedArgIndex+") => BREAK");
                                continue main;
                            }
                        }
                    }
                    // VAR ARGS
                    else {
                        tempTypedArgIndex++;
                        if(tempTypedArgIndex < args.length &&
                                (tempLastArgValid = arg.check(args[tempTypedArgIndex]))){
                            tempRegArgCount++;
                            //System.out.println("-- VAR: VALID (tempTypedArgIndex="+tempTypedArgIndex+",tempRegArgCount="+tempRegArgCount+")");
                        }
                    }
                }
            }
            // switch to the new command if possible
            if(regArgCount <= tempRegArgCount){
                command = i;
                regArgCount = tempRegArgCount;
                typedArgIndex = tempTypedArgIndex;
                lastArgValid = tempLastArgValid;
                //System.out.println("[CHANGE] Selected command "+command+" (regArgCount="+regArgCount+",typedArgIndex="+typedArgIndex+",lastArgValid="+lastArgValid+")");
            }
        }
        if(command == -1 || regArgCount == 0){
            errorCallback.notFound(this, sender, args);
            suggestionCallback.run(this, sender, args, 0);
        } else {
            if(!lastArgValid) {
                errorCallback.invalid(this, sender, args, command, typedArgIndex);
            } else {
                // the index of X
                int regArgIndex = regArgCount-1;
                if(typedArgIndex == -1) {
                    arguments.get(regArgIndex, command).getCallback().run(this, sender, command, args, typedArgIndex, null);
                } else{
                    int valueLength = args.length - typedArgIndex;
                    String[] arr = new String[valueLength];
                    System.arraycopy(args, typedArgIndex, arr, 0, valueLength);
                    arguments.get(regArgIndex, command).getCallback().run(this, sender, command, args, typedArgIndex, String.join(" ", arr));
                }
            }
        }
    }

    /**
     * Gets all aliases
     * @return list of aliases
     */
    public List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    /**
     * Add an alias
     * @param alias alias
     * @return this object
     */
    public CommandBuilder addAlias(String alias) {
        aliases.add(alias);
        return this;
    }
}
