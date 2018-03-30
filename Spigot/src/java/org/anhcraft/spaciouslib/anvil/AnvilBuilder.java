package org.anhcraft.spaciouslib.anvil;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public class AnvilBuilder {
    public static LinkedHashMap<Player, Group<Inventory, AnvilHandler>> data = new LinkedHashMap<>();

    private AnvilWrapper wrapper;
    private AnvilHandler handler;
    private Player player;

    public AnvilBuilder(Player player, AnvilHandler handler){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.anvil.Anvil_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor(Player.class);
            this.wrapper = ((AnvilWrapper) c.newInstance(player));
            this.player = player;
            this.handler = handler;
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    public AnvilBuilder clone(Player player){
        return new AnvilBuilder(player, this.handler);
    }

    public AnvilBuilder open() {
        this.wrapper.open();
        data.put(this.player, new Group<>(this.wrapper.inv, this.handler));
        return this;
    }

    public AnvilBuilder setItem(AnvilSlot slot, ItemStack item) {
        this.wrapper.setItem(slot, item);
        return this;
    }
}