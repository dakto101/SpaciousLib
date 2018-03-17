package org.anhcraft.spaciouslib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCommand {
    public enum ArgumentType{
        CUSTOM,
        URL,
        EMAIL,
        ONLINE_PLAYER,
        INTEGER_NUMBER,
        REAL_NUMBER,
        BOOLEAN,
        WORLD
    }

    private Command command;
    private String name;
    private List<SubCommand> subcmds = new ArrayList<>();

    @Deprecated
    public SCommand(Command command){
        this.command = command;
        this.name = command.getName();
    }

    public SCommand(PluginCommand command){
        this.command = command;
        this.name = command.getName();
    }

    public SCommand(String name) throws Exception {
        if(name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.name = name.trim().toLowerCase();
    }

    public SCommand addSubCommand(SubCommand subCommand){
        this.subcmds.add(subCommand);
        return this;
    }

    public List<SubCommand> getSubCommands(){
        return this.subcmds;
    }

    public Command getCommand(){
        return this.command;
    }

    public SCommand buildExecutor(JavaPlugin plugin) throws Exception {
        SCommand sc = this;
        if(getCommand() == null){
            Class pluginCommandClass = PluginCommand.class;
            Constructor pluginCommandCons = pluginCommandClass.getDeclaredConstructor(String.class, Plugin.class);
            pluginCommandCons.setAccessible(true);
            Object pluginCommand = pluginCommandCons.newInstance(this.name, plugin);
            PluginCommand c = (PluginCommand) pluginCommand;
            c.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender s, Command command,
                                         String l, String[] a) {
                    execute(s, a);
                    return false;
                }
            });
            CommandManager.register(c);
        } else if(getCommand() instanceof PluginCommand){
            ((PluginCommand) getCommand()).setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender s, Command command,
                                         String l, String[] a) {
                    execute(s, a);
                    return false;
                }
            });
        }
        return sc;
    }

    private void execute(CommandSender s, String[] a) {
        StringBuilder cmd = new StringBuilder();
        for(String t : a){
            cmd.append(" ").append(t);
        }
        cmd = new StringBuilder(cmd.toString().replaceFirst(" ", "").trim());
        for(SubCommand sc : this.subcmds){
            if((cmd.length() == 0 && sc.getName().length() == 0) || (0 < sc.getName().length() && cmd
                    .toString().startsWith(sc.getName()))){
                int x = sc.getName().split(" ").length;
                sc.execute(s, Arrays.copyOfRange(a, x, a.length));
            }
        }
    }
}
