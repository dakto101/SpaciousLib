package org.anhcraft.spaciouslib.command;

import org.bukkit.command.CommandSender;

public abstract class CommandRunnable {
    /**
     * This method will be called if a player execute a command or a sub command (with or without arguments)
     * @param cmd CommandBuilder object
     * @param subcmd SubCommandBuilder object
     * @param sender the sender who executed this command (the console or a player)
     * @param args all arguments
     * @param value current argument's value (may be null if there isn't any arguments)
     */
    public abstract void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value);
}
