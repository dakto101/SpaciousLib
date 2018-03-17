package org.anhcraft.spaciouslib.command;

import org.bukkit.command.CommandSender;

public abstract class CommandRunnable {
    public abstract void run(SubCommand cmd, CommandSender sender, String[] args, String value);
}
