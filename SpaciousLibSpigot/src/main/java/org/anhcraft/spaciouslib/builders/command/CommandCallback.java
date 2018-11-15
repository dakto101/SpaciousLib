package org.anhcraft.spaciouslib.builders.command;

import org.bukkit.command.CommandSender;

public abstract class CommandCallback {
    /**
     * This method is called after a player had entered a command which pointed to this argument
     * @param builder the command builder
     * @param sender the executor
     * @param command the command index
     * @param args typed arguments
     * @param arg the argument index
     * @param value the argument value
     */
    public abstract void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value);
}
