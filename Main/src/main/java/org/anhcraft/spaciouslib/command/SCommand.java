package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCommand extends CommandString {
    private Command command;
    private String name;
    private SubCommand rootCmd;
    private List<SubCommand> subcmds = new ArrayList<>();

    public SCommand(String name, CommandRunnable rootRunnable, String rootDescription) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommand("", rootDescription, rootRunnable);
        addSubCommand(this.rootCmd);
    }

    public SCommand(String name, CommandRunnable rootRunnable) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommand("", "&cShows all commands", rootRunnable);
        addSubCommand(this.rootCmd);
    }

    public SCommand(String name, SubCommand rootCmd) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = rootCmd;
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

    public List<String> getCommandsAsString(boolean color){
        List<String> a = new ArrayList<>();
        for(SubCommand sc : getSubCommands()){
            a.add(Strings.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + (0 < sc.getName().length() ? " " : "") + sc.getCommandString(color)));
        }
        return a;
    }

    public String getCommandAsString(SubCommand subCommand, boolean color){
        return Strings.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + " " + subCommand.getCommandString(color));
    }

    public SCommand buildExecutor(JavaPlugin plugin) throws Exception {
        if(getCommand() == null){
            Constructor pluginCommandCons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
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
            CommandManager.register(plugin, c);
            this.command = c;
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
        return this;
    }

    private void execute(CommandSender s, String[] a) {
        StringBuilder cmdb = new StringBuilder();
        for(String t : a){
            cmdb.append(" ").append(t);
        }
        String cmd = cmdb.toString().replaceFirst(" ", "").trim();
        SubCommand found = null;
        boolean xt = false;
        for(SubCommand sc : this.subcmds){
            if(cmd.length() == 0 && sc.getName().length() == 0){
                xt = true;
                sc.execute(this, s, new String[]{});
                break;
            }
            else if(0 < sc.getName().length() && validateSubCommand(sc.getName(), cmd)){
                if(found == null || found.getName().length() < sc.getName().length()){
                    found = sc;
                }
            }
        }
        if(found != null) {
            int x = found.getName().split(" ").length;
            found.execute(this, s, Arrays.copyOfRange(a, x, a.length));
        } else {
            if(!xt) {
                s.sendMessage(Strings.color(rootCmd.canNotFindCmdErrorMessage));
            }
        }
    }

    private boolean validateSubCommand(String name, String cmd) {
        String[] a = name.split(" ");
        String[] b = cmd.split(" ");
        int i = 0;
        for(String c : a){
            if(b.length <= i){
                return false;
            }
            if(!c.equals(b[i].toLowerCase())){
                return false;
            }
            i++;
        }
        return true;
    }
}
