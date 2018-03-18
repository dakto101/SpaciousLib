package org.anhcraft.spaciouslib.anvil;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class Anvil implements AnvilWrapper{
    public static HashMap<UUID, AnvilWrapper> anvils = new HashMap<>();
    private Player player;
    private String id;

    /**
     * Creates a new anvil
     * Please use AnvilForm instead
     * @param player a player which you want to show to
     * @param id anvil's id
     */
    public Anvil(Player player, String id){
        this.player = player;
        this.id = id;
    }

    /**
     * Opens the anvil
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
     * Sets item on a specific slot for the anvil
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
     * Gets the anvil's id
     * @return anvil's id
     */
    @Override
    public String getAnvilID() {
        return id;
    }
}
