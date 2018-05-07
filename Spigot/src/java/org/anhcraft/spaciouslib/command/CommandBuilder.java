package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommandUtils;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * A command builder helps you to create a new command and register it in runtime
 */
public class CommandBuilder extends CommandString {
    private Command command;
    private String name;
    private SubCommandBuilder rootCmd;
    private List<SubCommandBuilder> subcmds = new ArrayList<>();

    /**
     * Creates a new CommandBuilder instance
     * @param name the name of that command (e.g: test is the name of the command /test a b c)
     * @param rootRunnable a runnable which triggers if a player run that command with no arguments
     * @param rootDescription the description for that command
     */
    public CommandBuilder(String name, CommandRunnable rootRunnable, String rootDescription) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommandBuilder("", rootDescription, rootRunnable);
        addSubCommand(this.rootCmd);
    }

    /**
     * Creates a new CommandBuilder instance<br>
     * The default description: &cShows all commands
     * @param name the name of this command
     * @param rootRunnable a runnable which triggers if a player run that command with no arguments
     */
    public CommandBuilder(String name, CommandRunnable rootRunnable) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = new SubCommandBuilder("", "&cShows all commands", rootRunnable);
        addSubCommand(this.rootCmd);
    }

    /**
     * Creates a new CommandBuilder instance<br>
     * The sub command must have <b>a blank name</b>
     * @param name the name of the command
     * @param rootCmd SubCommandBuilder object
     */
    public CommandBuilder(String name, SubCommandBuilder rootCmd) throws Exception {
        this.name = name.trim().toLowerCase();
        if(0 < this.name.length() && this.name.split(" ").length != 1){
            throw new Exception("Invalid command name!");
        }
        this.rootCmd = rootCmd;
        if(0 < this.rootCmd.getName().length()){
            throw new Exception("Subcommand must have a blank name!");
        }
        addSubCommand(this.rootCmd);
    }

    /**
     * Adds a sub command
     * @param subCommand SubCommandBuilder object
     * @return this object
     */
    public CommandBuilder addSubCommand(SubCommandBuilder subCommand){
        this.subcmds.add(subCommand);
        return this;
    }

    /**
     * Gets all sub commands
     * @return list of sub commands
     */
    public List<SubCommandBuilder> getSubCommands(){
        return this.subcmds;
    }

    /**
     * Get the Bukkit command
     * @return Bukkit command
     */
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
        for(SubCommandBuilder sc : getSubCommands()){
            a.add(Chat.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + (0 < sc.getName().length() ? " " : "") + sc.getCommandString(color)));
        }
        return a;
    }

    /**
     * Gets the string format of a specific sub command
     * @param color true if you want to "color" that string
     * @return the command in string format
     */
    public String getCommandAsString(SubCommandBuilder subCommand, boolean color){
        return Chat.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + (0 < subCommand.getName().length() ? " " : "") + subCommand.getCommandString(color));
    }

    /**
     * Builds a new executor for this command<br>
     * Also registers this command with "CommandManager" if the command hasn't registered yet<br>
     * @param plugin the plugin
     * @return this object
     */
    public CommandBuilder buildExecutor(JavaPlugin plugin) throws Exception {
        if(getCommand() == null){
            PluginCommand c = (PluginCommand) ReflectionUtils.getConstructor(PluginCommand.class, new Group<>(
                    new Class<?>[]{String.class, Plugin.class},
                    new Object[]{this.name, plugin}
            ));
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
            CommandUtils.register(plugin, c);
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
        // [vi] CO CHE HOAT DONG
        // [vi] tab complete la hinh thuc dua ra mot danh sach cac argument con thieu va chen truc tiep vao lenh ma nguoi choi da nhap bang cach an TAB
        // [vi] no dong nghia voi viec ta khong the dua tat ca cac lenh, ma chi co the dua cac tham so con thieu duoi dang mot lenh
        // [vi] vd /test a b c d la lenh chi dinh
        // [vi] nguoi choi nhap /test a vay con thieu b c d
        // [vi] tuy nhien co nhung truong hop con co cac tham so dong
        // [vi] vd /test a b c e
        // [vi] va /test a b c f deu duoc
        // [vi] thi ta chi kiem tra phan /test a b c

        // if only contains the root command
        if(getSubCommands().size() == 1){
            return new ArrayList<>();
        }

        // [vi] mot tree map se xep lenh co do dai tu cao -> thap (vi da lat lai)
        TreeMap<Integer, String> s = new TreeMap<>(Collections.reverseOrder());

        // [vi] day la lenh ma nguoi choi da nhap, khong co root command
        StringBuilder cmdb = new StringBuilder();
        for(String t : a){
            // [vi] bo cac tham so khong co gia tri
            if(t.replace(" ", "").length() == 0){
                continue;
            }
            cmdb.append(" ").append(t);
        }
        // [vi] chinh lai lenh moi voi dung format la: giua cac tham so co khoang cach
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();

        for(SubCommandBuilder sc : getSubCommands()){
            // [vi] kiem tra xem lenh do co bat dau boi mot lenh chi dinh (sub command) nao khong
            if(sc.getName().startsWith(cmd)) {
                // [vi] tach ten sub command
                String[] m = sc.getName().split(" ");
                // [vi] tach lenh da nhap
                String[] j = cmd.split(" ");
                // [vi] d9a6 la danh sach cac phan con thieu
                String[] n = m;
                // [vi] kiem tra lenh da nhap phai dai hon 0, hai bien m va n phai co kich co lon hon 0
                // [vi] phan cuoi cung cua sub command phai bat dau bang phan cuoi cung cua lenh cuoi cung da nhap
                // [vi] lenh chi dinh phai co nhieu phan hon hoac bang phan lenh chi dinh lenh da nhap
                // [vi] vd: /test a b c la lenh da nhap con /test a b c la lenh da dat
                // [vi] vd: /test a b test la lenh da nhap con /test a b t la lenh da dat
                if(0 < cmd.length() && 0 < j.length && 0 < m.length
                        && j.length <= m.length && m[m.length - 1].startsWith(j[j.length - 1])){
                    // [vi] neu ca hai bien m va n bang nhau, nghia la trong lenh da nhap khong he co tham so nao khac
                    if(j.length == m.length){
                        n = new String[]{m[m.length - 1]};
                    }
                    // [vi] con khong la chi lay phan ma lenh da nhap con thieu (vi dang tab complete)
                    else {
                        n = Arrays.copyOfRange(m, j.length, m.length);
                    }
                }
                // [vi] chuyen cac phan con thieu tu bien n sang mot lenh co format hoan chinh
                StringBuilder x = new StringBuilder();
                for(String t : n){
                    x.append(" ").append(t);
                }
                // [vi] dat vao tree map
                String v = x.toString().trim();
                s.put(v.length(), v);
            }
        }
        return new ArrayList<>(s.values());
    }

    private void execute(CommandSender s, String[] a) {
        // if only contains the root command
        if(getSubCommands().size() == 1){
            try {
                getSubCommands().get(0).execute(this, s, a);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return;
        }

        StringBuilder cmdb = new StringBuilder();
        for(String t : a) {
            cmdb.append(" ").append(t);
        }        // [vi] chuyen tham so thanh lenh
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();

        SubCommandBuilder found = null;
        boolean xt = false;
        for(SubCommandBuilder sc : this.subcmds){
            // [vi] neu lenh da nhap khong co tham so => dang nhap root command
            // [vi] vd /test
            if(cmd.length() == 0 && sc.getName().length() == 0){
                xt = true;
                try {
                    sc.execute(this, s, new String[]{});
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            // [vi] neu co tham so thi ten sub command phai co do dai lon hon 0
            else if(0 < sc.getName().length() && validateSubCommandBuilder(sc.getName(), cmd)){
                // [vi] neu sub command chua tim thay thi cho phep them thang, con khong phai thong qua kiem tra
                // [vi] ten sub command da tim phai ngan hon ten sub command hien tai
                if(found == null || found.getName().length() < sc.getName().length()){
                    found = sc;
                }
            }
        }
        if(found != null) {
            int x = found.getName().split(" ").length;
            // [vi] tach sub command de con lai phan tham so dong cho plugin xu ly
            try {
                found.execute(this, s, Arrays.copyOfRange(a, x, a.length));
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            // [vi] neu khong thay thi goi y lenh
            if(!xt) {
                s.sendMessage(Chat.color(rootCmd.canNotFindCmdErrorMessage));
                for(SubCommandBuilder sc : getSubCommands()){
                    if(sc.getName().startsWith(cmd)){
                        s.sendMessage(Chat.color(rootCmd.suggestionMessage));
                        s.sendMessage(getCommandAsString(sc, true));
                        break;
                    }
                }
            }
        }
    }

    private boolean validateSubCommandBuilder(String name, String cmd) {
        // [vi] CO CHE HOAT DONG
        // [vi] cho lenh chi dinh /test a b
        // [vi] lenh da nhap /test a b e
        // [vi] nhu vay chi can a & b o hai lenh trung nhau la duoc,, con e se duoc tinh nhu la mot tham so dong

        String[] a = name.split(" ");
        String[] b = cmd.split(" ");
        int i = 0;
        // [vi] lap tung phan ten cua sub command
        for(String c : a){
            // [vi] neu lenh da nhap khong co phan sub command nay thi tra ve sai
            if(b.length <= i){
                return false;
            }
            // [vi] ten phan cua sub command hien tai phai trung voi phan hien tai trong lenh da nhap
            // [vi] (khong can trung in hoa - thuong)
            if(!c.equalsIgnoreCase(b[i])){
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Sets new sub commands
     * @param subCommands lsit of sub commands
     * @return this object
     */
    public CommandBuilder setSubCommands(List<SubCommandBuilder> subCommands) {
        this.subcmds = subCommands;
        return this;
    }

    /**
     * Clones this object
     * @param name new command name
     * @return new object
     */
    public CommandBuilder clone(String name) throws Exception {
        return new CommandBuilder(name, rootCmd).setSubCommands(subcmds);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            CommandBuilder c = (CommandBuilder) o;
            return new EqualsBuilder()
                    .append(c.command, this.command)
                    .append(c.name, this.name)
                    .append(c.rootCmd, this.rootCmd)
                    .append(c.subcmds, this.subcmds)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(19, 33)
                .append(this.command)
                .append(this.name)
                .append(this.rootCmd)
                .append(this.subcmds).toHashCode();
    }
}
