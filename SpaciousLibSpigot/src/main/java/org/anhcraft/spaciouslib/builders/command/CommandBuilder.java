package org.anhcraft.spaciouslib.builders.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.anhcraft.spaciouslib.builders.ArrayBuilder;
import org.anhcraft.spaciouslib.builders.ChatComponentBuilder;
import org.anhcraft.spaciouslib.utils.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandBuilder {
    private String name;
    private String[] explanationData;
    private Table<Argument> arguments;
    private ErrorCallback errorCallback = new ErrorCallback() {
        @Override
        public void invalid(CommandBuilder builder, CommandSender sender, String[] args, int command, int arg) {
            sender.sendMessage(Chat.color("&e(!) Invalid value &f'"+StringUtils.escape(args[arg])+"' &eat argument "+arg+": "+getArgument(command, arg).getType().getErrorMessage()));
            sender.sendMessage(Chat.color("&a(>) Please check again the command:"));
            sender.spigot().sendMessage(toTextComponent(command, true, true));
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
            sender.spigot().sendMessage(toTextComponent(suggestionCommand, true, true));
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
        this.arguments = new Table<>(0, 0);
        this.explanationData = new String[0];
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
        this.arguments = new Table<>(0, 0);
        this.explanationData = new String[0];
        addChild(explanation, new Argument("", callback, "", null));
    }

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param args arguments of root command
     */
    public CommandBuilder(String name, Argument[] args){
        this.name = name;
        this.arguments = new Table<>(0, 0);
        this.explanationData = new String[0];
        addChild("", args);
    }

    /**
     * Creates a new CommandBuilder instance
     * @param name name of command
     * @param args arguments of root command
     * @param explanation command explanation
     */
    public CommandBuilder(String name, Argument[] args, String explanation){
        this.name = name;
        this.arguments = new Table<>(0, 0);
        this.explanationData = new String[0];
        addChild(explanation, args);
    }

    /**
     * Add a child command
     * @param args arguments of child command
     * @return this object
     */
    public CommandBuilder addChild(Argument... args){
        return addChild("", args);
    }

    /**
     * Add a child command
     * @param args arguments of child command
     * @param explanation child command explanation
     * @return this object
     */
    public CommandBuilder addChild(String explanation, Argument... args){
        if(args.length > 0) {
            explanationData = (String[]) new ArrayBuilder(explanationData).append(explanation).build();
            if(arguments.columns() < args.length) {
                int i = 0;
                while(i < args.length) {
                    arguments.addLastColumn();
                    i++;
                }
            }
            arguments.addLastRow();
            arguments.set(arguments.columns(), arguments.rows() - 1, args);
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
            sender.spigot().sendMessage(toTextComponent(i, color, showType));
        }
        return this;
    }

    /**
     * Build the executor and register the command for the given plugin
     * @param plugin plugin
     * @return this object
     */
    public CommandBuilder build(JavaPlugin plugin){
        if(command == null){
            PluginCommand c = (PluginCommand) ReflectionUtils.getConstructor(PluginCommand.class, new Group<>(
                    new Class<?>[]{String.class, Plugin.class},
                    new Object[]{this.name, plugin}
            ));
            c.setTabCompleter((sender, command, alias, args) -> tabCompleter(args));
            c.setExecutor((s, command, l, a) -> {
                executor(s, a);
                return false;
            });
            CommandUtils.register(plugin, c);
            this.command = c;
        } else {
            command.setTabCompleter((sender, command, alias, args) -> tabCompleter(args));
            command.setExecutor((s, command, l, a) -> {
                executor(s, a);
                return false;
            });
        }
        return this;
    }

    public CommandBuilder clone(String name){
        CommandBuilder cb = new CommandBuilder(name, CommonUtils.toArray(arguments.toList(), Argument.class),
                explanationData[0]);
        cb.errorCallback = errorCallback;
        cb.suggestionCallback = suggestionCallback;
        return cb;
    }

    private List<String> tabCompleter(String[] args) {
        /*
        HOW DOES THE TAB COMPLETER WORK?
        - Loop though all path arguments
        - Checks all typing arguments except the last one, they must match with registered arguments
        - Add the last argument to the suggestion list
         */

        Argument arg; // store the current typing argument
        List<String> str = new ArrayList<>(); // list of suggestions
        for(int i = 0; i < arguments.rows(); i++){
            for(int j = 0; j < arguments.columns(); j++){
                arg = arguments.get(j, i);
                if(arg == null || !arg.isPathArgument() || arg.getName().length() == 0){
                    break;
                }
                // if there isn't any typing arguments
                if(args.length == 0) {
                    str.add(arg.getName());
                    break;
                }
                // previous typing arguments must match with registered arguments
                if(j < args.length-1) {
                    if(!arg.getName().equals(args[j])){
                        break;
                    }
                }
                // if this is the last argument, we will add it to the suggestion list
                else {
                    str.add(arg.getName());
                    break;
                }
            }
        }
        return str;
    }

    private void executor(CommandSender sender, String[] args) {
        if(args.length == 0 && getCommandArgs(0).size() == 1){
            arguments.get(0, 0).getCallback().run(this, sender, 0, args, 0, null);
            return;
        }
        int command = -1;
        int mostValidArgs = 0;

        boolean validArg[] = new boolean[explanationData.length];
        for(int i = 0; i < arguments.rows(); i++){
            int validArgs = 0;
            for(int j = 0; j < arguments.columns(); j++){
                Argument arg = arguments.get(j, i);
                if(arg == null || arg.getName().length() == 0){
                    break;
                }
                validArg[i] = arg.check(args[j]);
                if(validArg[i]) {
                    validArgs++;
                } else {
                    break;
                }
                if(j == arguments.columns() - 1 || j == args.length -1){
                    break;
                }
            }
            if(mostValidArgs < validArgs){
                command = i;
                mostValidArgs = validArgs;
            }
        }

        if(command == -1){
            errorCallback.notFound(this, sender, args);
            suggestionCallback.run(this, sender, args, 0);
        } else {
            int lastArg = mostValidArgs-1;
            if(!validArg[command]) {
                errorCallback.invalid(this, sender, args, command, mostValidArgs);
            } else {
                String[] arr = new String[args.length-lastArg];
                System.arraycopy(args, lastArg, arr, 0, args.length-lastArg);
                arguments.get(lastArg, command).getCallback().run(this, sender, command, args, lastArg, String.join(" ", arr));
            }
        }
    }
}
