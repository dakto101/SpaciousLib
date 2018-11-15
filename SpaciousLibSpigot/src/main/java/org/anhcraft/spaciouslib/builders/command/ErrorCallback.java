package org.anhcraft.spaciouslib.builders.command;

import org.bukkit.command.CommandSender;

public abstract class ErrorCallback {
    /**
     * This method is called when a player had entered a command which encountered an invalid argument.<br>
     * This error only happens with variable arguments
     * @param builder the command builder
     * @param sender the executor
     * @param args typed arguments
     * @param command the command index
     * @param arg the argument index
     */
    public abstract void invalid(CommandBuilder builder, CommandSender sender, String[] args, int command, int arg);

    /**
     * This method is called when a player entered an unknown command
     * @param builder the command builder
     * @param sender the executor
     * @param args typed arguments
     */
    public abstract void notFound(CommandBuilder builder, CommandSender sender, String[] args);
}
