package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommandUtils;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
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
            throw new Exception("Subcommand must have a black name!");
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
        return Chat.color((color ? gcs(Type.BEGIN_COMMAND) : "") + this.name + " " + subCommand.getCommandString(color));
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
        // [vi] CƠ CHẾ HOẠT ĐỘNG
        // [vi] tab complete là hình thức đưa ra một danh sách các argument còn thiếu và chèn trực tiếp vào lệnh mà người chơi đã nhập bằng cách ấn TAB
        // [vi] nó đồng nghĩa với việc ta không thể đưa tất cả các lệnh, mà chỉ có thể đưa các tham số còn thiếu dưới dạng một lệnh
        // [vi] vd /test a b c d là lệnh chỉ định
        // [vi] người chơi nhập /test a vậy còn thiếu b c d
        // [vi] tuy nhiên có những trường hợp còn có các tham số động
        // [vi] vd /test a b c e
        // [vi] và /test a b c f đều được
        // [vi] thì ta chỉ kiểm tra phần /test a b c

        // [vi] một tree map sẽ xếp lệnh có độ dài từ cao -> thấp (vì đã lật lại)
        TreeMap<Integer, String> s = new TreeMap<>(Collections.reverseOrder());

        // [vi] đây là lệnh mà người chơi đã nhập, không có root command
        StringBuilder cmdb = new StringBuilder();
        for(String t : a){
            // [vi] bỏ các tham số không có giá trị
            if(t.replace(" ", "").length() == 0){
                continue;
            }
            cmdb.append(" ").append(t);
        }
        // [vi] chỉnh lại lệnh mới với đúng format là: giữa các tham số có khoảng cách
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();

        for(SubCommandBuilder sc : getSubCommands()){
            // [vi] kiểm tra xem lệnh đó có bắt đầu bởi một lệnh chỉ định (sub command) nào không
            if(sc.getName().startsWith(cmd)) {
                // [vi] tách tên sub command
                String[] m = sc.getName().split(" ");
                // [vi] tách lệnh đã nhập
                String[] j = cmd.split(" ");
                // [vi] d9a6 là danh sách các phần còn thiếu
                String[] n = m;
                // [vi] kiểm tra lệnh đã nhập phải dài hơn 0, hai biến m và n phải có kích cỡ lớn hơn 0
                // [vi] phần cuối cùng của sub command phải bắt đầu bằng phần cuối cùng của lệnh cuối cùng đã nhập
                // [vi] lệnh chỉ định phải có nhiều phần hơn hoặc bằng phần lệnh chỉ định lệnh đã nhập
                // [vi] vd: /test a b c là lệnh đã nhập còn /test a b c là lệnh đã đặt
                // [vi] vd: /test a b test là lệnh đã nhập còn /test a b t là lệnh đã đặt
                if(0 < cmd.length() && 0 < j.length && 0 < m.length
                        && j.length <= m.length && m[m.length - 1].startsWith(j[j.length - 1])){
                    // [vi] nếu cả hai biến m và n bằng nhau, nghĩa là trong lệnh đã nhập không hề có tham số nào khác
                    if(j.length == m.length){
                        n = new String[]{m[m.length - 1]};
                    }
                    // [vi] còn không là chỉ lấy phần mà lệnh đã nhập còn thiếu (vì đang tab complete)
                    else {
                        n = Arrays.copyOfRange(m, j.length, m.length);
                    }
                }
                // [vi] chuyển các phần còn thiếu từ biến n sang một lệnh có format hoàn chỉnh
                StringBuilder x = new StringBuilder();
                for(String t : n){
                    x.append(" ").append(t);
                }
                // [vi] đặt vào tree map
                String v = x.toString().trim();
                s.put(v.length(), v);
            }
        }
        return new ArrayList<>(s.values());
    }

    private void execute(CommandSender s, String[] a) {
        StringBuilder cmdb = new StringBuilder();
        for(String t : a) {
            cmdb.append(" ").append(t);
        }
        // [vi] chuyển tham số thành lệnh
        String cmd = cmdb.toString().replaceFirst(" ", "").trim().toLowerCase();

        SubCommandBuilder found = null;
        boolean xt = false;
        for(SubCommandBuilder sc : this.subcmds){
            // [vi] nếu lệnh đã nhập không có tham số => đang nhập root command
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
            // [vi] nếu có tham số thì tên sub command phải có độ dài lớn hơn 0
            else if(0 < sc.getName().length() && validateSubCommandBuilder(sc.getName(), cmd)){
                // [vi] nếu sub command chưa tìm thấy thì cho phép thêm thẳng, còn không phải thông qua kiểm tra
                // [vi] tên sub command đã tìm phải ngắn hơn tên sub command hiện tại
                if(found == null || found.getName().length() < sc.getName().length()){
                    found = sc;
                }
            }
        }
        if(found != null) {
            int x = found.getName().split(" ").length;
            // [vi] tách sub command để còn lại phần tham số động cho plugin xử lý
            try {
                found.execute(this, s, Arrays.copyOfRange(a, x, a.length));
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            // [vi] nếu không thấy thì gợi ý lệnh
            if(!xt) {
                s.sendMessage(Chat.color(rootCmd.canNotFindCmdErrorMessage));
                for(SubCommandBuilder sc : getSubCommands()){
                    if(sc.getName().startsWith(cmd)){
                        s.sendMessage(Chat.color(rootCmd.suggestMessage));
                        s.sendMessage(getCommandAsString(sc, true));
                        break;
                    }
                }
            }
        }
    }

    private boolean validateSubCommandBuilder(String name, String cmd) {
        // [vi] CƠ CHẾ HOẠT ĐỘNG
        // [vi] cho lệnh chỉ định /test a b
        // [vi] lệnh đã nhập /test a b e
        // [vi] như vậy chỉ cần a & b ở hai lệnh trùng nhau là được,, còn e sẽ được tính như là một tham số động

        String[] a = name.split(" ");
        String[] b = cmd.split(" ");
        int i = 0;
        // [vi] lặp từng phần tên của sub command
        for(String c : a){
            // [vi] nếu lệnh đã nhập không có phần sub command này thì trả về sai
            if(b.length <= i){
                return false;
            }
            // [vi] tên phần của sub command hiện tại phải trùng với phần hiện tại trong lệnh đã nhập
            // [vi] (không cần trùng in hoa - thường)
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
}
