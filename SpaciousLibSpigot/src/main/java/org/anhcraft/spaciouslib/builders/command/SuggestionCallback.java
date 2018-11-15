package org.anhcraft.spaciouslib.builders.command;

import org.bukkit.command.CommandSender;

public abstract class SuggestionCallback {
    /**
     * This method is called when a player had entered an unknown command and the system made a suggestion
     * @param builder the command builder
     * @param sender the executor
     * @param args typed arguments
     * @param suggestionCommand the index of suggestion command
     */
    public abstract void run(CommandBuilder builder, CommandSender sender, String[] args, int suggestionCommand);
}
