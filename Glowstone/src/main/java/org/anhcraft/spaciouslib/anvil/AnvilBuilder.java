package org.anhcraft.spaciouslib.anvil;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowAnvilInventory;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class AnvilBuilder {
    public static LinkedHashMap<Player, Group<Inventory, AnvilHandler>> data = new LinkedHashMap<>();

    private GlowAnvilInventory anvil;
    private AnvilHandler handler;
    private GlowPlayer player;

    public AnvilBuilder(Player player, AnvilHandler handler){
        this.player = (GlowPlayer) player;
        this.anvil = new GlowAnvilInventory(null);
    }

    public AnvilBuilder clone(Player player){
        return new AnvilBuilder(player, this.handler);
    }

    public AnvilBuilder open() {
        this.player.openInventory(this.anvil);
        data.put(this.player, new Group<>(this.anvil, this.handler));
        return this;
    }

    public AnvilBuilder setItem(AnvilSlot slot, ItemStack item) {
        this.anvil.setItem(slot.getID(), item);
        return this;
    }
}