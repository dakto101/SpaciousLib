package org.anhcraft.spaciouslib.placeholder;

import org.bukkit.entity.Player;

public abstract class Placeholder {
    public abstract String getPlaceholder();
    public abstract String getValue(Player player);
}