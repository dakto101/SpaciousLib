package org.anhcraft.spaciouslib.builders.command;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class PluginCommand extends Command implements TabExecutor {
    public PluginCommand(String name) {
        super(name);
    }
}
