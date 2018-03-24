package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * A command builder helps you create a new command and register it in runtime
 */
public class CommandBuilder extends CommandString {
    private Command command;
    private String name;
    private SubCommandBuilder rootCmd;
    private List<SubCommandBuilder> subcmds = new ArrayList<>();

    /**
     * Create a new CommandBuilder instance
     * @param name the name of that command (e.g: test is the name of the command /test a b c)
     * @param rootRunnable a runnable which was triggered if a player run that command with no arguments
     * @param rootDescription the description for this command if a player run that command with no arguments
     * @throws Exception
     */
    public CommandBuilder(String name, CommandRunnable rootRunnable, String rootDescription) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommandBuilder("", rootDescription, rootRunnable);
        addSubCommandBuilder(this.rootCmd);
    }

    /**
     * Creates a new CommandBuilder instance (which doesn't need a description for the command)<br>
     * The description: &cShows all commands
     * @param name the name of this command
     * @param rootRunnable a runnable which was triggered if a player run that command with no arguments
     * @throws Exception
     */
    public CommandBuilder(String name, CommandRunnable rootRunnable) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommandBuilder("", "&cShows all commands", rootRunnable);
        addSubCommandBuilder(this.rootCmd);
    }

    /**
     * Creates a new CommandBuilder instance<br>
     * This subcommand must have a blank name
     * @param name the name of this command
     * @param rootCmd the subcommand instance
     * @throws Exception
     */
    public CommandBuilder(String name, SubCommandBuilder rootCmd) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = rootCmd;
        if(0 < this.rootCmd.getName().length()){
            throw new Exception("Subcommand must have a black name!");
        }
    }

    public CommandBuilder addSubCommandBuilder(SubCommandBuilder subCommand){
        this.subcmds.add(subCommand);
        return this;
    }

    public List<SubCommandBuilder> getSubCommandBuilders(){
        return this.subcmds;
    }

    public Command getCommand(){
        return this.command;
    }

    /**
     * Gets the string format of this command
     * @param color true if you want to "color" that string
     * @return the command in string format
     */
    public List<String> getCommandsAsString(boolean color){
        List<String> a = new ArrayList<>();
        for(SubCommandBuilder sc : getSubCommandBuilders()){
            a.add(Strings.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + (0 < sc.getName().length() ? " " : "") + sc.getCommandString(color)));
        }
        return a;
    }

    /**
     * Gets the string format of a specific subcommand
     * @param color true if you want to "color" that string
     * @return the command in string format
     */
    public String getCommandAsString(SubCommandBuilder subCommand, boolean color){
        return Strings.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + " " + subCommand.getCommandString(color));
    }

    /**
     * Builds a new executor for this command<br>
     * Also registers this command with "CommandManager" if the command hasn't registered yet<br>
     * @param plugin
     * @return
     * @throws Exception
     */
    public CommandBuilder buildExecutor(JavaPlugin plugin) throws Exception {
        if(getCommand() == null){
            Constructor pluginCommandCons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            pluginCommandCons.setAccessible(true);
            Object pluginCommand = pluginCommandCons.newInstance(this.name, plugin);
            PluginCommand c = (PluginCommand) pluginCommand;
            c.setTabCompleter(new TabCompleter() {
                @Override
                public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                    return tabcomplete(args);
                }
            });
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
            ((PluginCommand) getCommand()).setTabCompleter(new TabCompleter() {
                @Override
                public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                    return tabcomplete(args);
                }
            });
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

    private List<String> tabcomplete(String[] a) {
        TreeMap<Integer, String> s = new TreeMap<>(Collections.reverseOrder());
        StringBuilder cmdb = new StringBuilder();
        for(String t : a){
            if(t.replace(" ", "").length() == 0){
                continue;
            }
            cmdb.append(" ").append(t);
        }
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();
        for(SubCommandBuilder sc : getSubCommandBuilders()){
            if(sc.getName().startsWith(cmd)) {
                String[] m = sc.getName().split(" ");
                String[] j = cmd.split(" ");
                String[] n = m;
                if(0 < cmd.length() && 0 < j.length && 0 < m.length && m[m.length - 1].startsWith(j[j.length - 1])){
                    if(j.length == m.length){
                        n = new String[]{m[m.length - 1]};
                    } else {
                        n = Arrays.copyOfRange(m, j.length, m.length);
                    }
                }
                StringBuilder x = new StringBuilder();
                for(String t : n){
                    x.append(" ").append(t);
                }
                String v = x.toString().trim();
                s.put(v.length(), v);
            }
        }
        return new ArrayList<>(s.values());
    }

    private void execute(CommandSender s, String[] a) {
        StringBuilder cmdb = new StringBuilder();
        for(String t : a){
            cmdb.append(" ").append(t);
        }
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();
        SubCommandBuilder found = null;
        boolean xt = false;
        for(SubCommandBuilder sc : this.subcmds){
            if(cmd.length() == 0 && sc.getName().length() == 0){
                xt = true;
                try {
                    sc.execute(this, s, new String[]{});
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            else if(0 < sc.getName().length() && validateSubCommandBuilder(sc.getName(), cmd)){
                if(found == null || found.getName().length() < sc.getName().length()){
                    found = sc;
                }
            }
        }
        if(found != null) {
            int x = found.getName().split(" ").length;
            try {
                found.execute(this, s, Arrays.copyOfRange(a, x, a.length));
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            if(!xt) {
                s.sendMessage(Strings.color(rootCmd.canNotFindCmdErrorMessage));
                for(SubCommandBuilder sc : getSubCommandBuilders()){
                    if(sc.getName().startsWith(cmd)){
                        s.sendMessage(Strings.color(rootCmd.suggestMessage));
                        s.sendMessage(getCommandAsString(sc, true));
                        break;
                    }
                }
            }
        }
    }

    private boolean validateSubCommandBuilder(String name, String cmd) {
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

    public CommandBuilder setSubCommandBuilders(List<SubCommandBuilder> subCommands) {
        this.subcmds = subCommands;
        return this;
    }

    public CommandBuilder newAlias(String name) throws Exception {
        return new CommandBuilder(name, rootCmd).setSubCommandBuilders(subcmds);
    }
}
