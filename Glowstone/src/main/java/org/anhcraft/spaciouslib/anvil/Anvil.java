package org.anhcraft.spaciouslib.anvil;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents an anvil implementation.
 */
public class Anvil implements AnvilWrapper{
    public static HashMap<UUID, AnvilWrapper> anvils = new HashMap<>();
    private Player player;
    private String id;

    /**
     * Creates a new anvil instance<br><br>
     * <b>(Please use AnvilForm instead)</b>
     * @param player a player which you want to open for
     * @param id anvil id
     */
    public Anvil(Player player, String id){
        this.player = player;
        this.id = id;
    }

    /**
     * Opens that anvil
     */
    @Override
    public void open() {
        try {
            Class e = Class.forName("org.anhcraft.spaciouslib.anvil.Anvil_"+
                    GameVersion.getVersion().toString().replace("v",""));
            Constructor c = e.getConstructor(Player.class, String.class);
            AnvilWrapper a;
            if(anvils.containsKey(player.getUniqueId())){
                a = anvils.get(player.getUniqueId());
            } else {
                a = (AnvilWrapper) c.newInstance(player, id);
            }            a.open();
            anvils.put(player.getUniqueId(), a);
        } catch(ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException x) {
            x.printStackTrace();
        }
    }

    /**
     * Sets the given item on a specific slot for that anvil
     */
    @Override
    public void setSlot(AnvilSlot slot, ItemStack item) {
        try {
            Class e = Class.forName("org.anhcraft.spaciouslib.anvil.Anvil_"+
                    GameVersion.getVersion().toString().replace("v",""));
            Constructor c = e.getConstructor(Player.class, String.class);
            AnvilWrapper a;
            if(anvils.containsKey(player.getUniqueId())){
                a = anvils.get(player.getUniqueId());
            } else {
                a = (AnvilWrapper) c.newInstance(player, id);
            }
            a.setSlot(slot, item);
            anvils.put(player.getUniqueId(), a);
        } catch(ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException x) {
            x.printStackTrace();
        }
    }

    /**
     * Gets the anvil id
     * @return anvil id
     */
    @Override
    public String getAnvilID() {
        return id;
    }
}
