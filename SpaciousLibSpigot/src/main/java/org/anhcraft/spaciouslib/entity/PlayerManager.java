package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.NamedEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PlayerInfo;
import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage players
 */
public class PlayerManager extends EntityManager {
    /**
     * Creates a new PlayerManager instance
     *
     * @param player the player
     */
    public PlayerManager(Player player) {
        super(player);
    }

    private Player getPlayer(){
        return (Player) this.entity;
    }

    /**
     * Gets the ping number of that player
     * @return the player ping
     */
    public int getPing(){
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, getPlayer());
        Object nmsEntity = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        return (int) ReflectionUtils.getField("ping", ClassFinder.NMS.EntityPlayer, nmsEntity);
    }

    /**
     * Changes the skin of that player.<br>
     * Warning: this is for server-side, not proxy-side.<br>
     * If you use Bungeecord, please use the method "requestChangeSkin" of BungeeAPI instead.
     * @param skin the skin
     */
    public void changeSkin(Skin skin){
        List<Player> players = new ArrayList<>(getPlayer().getWorld().getPlayers());
        players.remove(getPlayer());
        World w = getPlayer().getWorld();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, getPlayer()).sendWorld(w);
        EntityDestroy.create(getPlayer().getEntityId()).sendPlayers(players);
        new GameProfileManager(getPlayer()).setSkin(skin).apply(getPlayer());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, getPlayer()).sendWorld(w);
        NamedEntitySpawn.create(getPlayer()).sendPlayers(players);

        // requests the player client to reload the player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        Object craftWorld = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, getPlayer().getWorld());
        Object worldServer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorld);
        int dimension = (int) ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, getPlayer());
        Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object playerList = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftServer, craftServer);
        ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityPlayer, int.class, boolean.class, Location.class, boolean.class},
                new Object[]{nmsEntityPlayer, dimension, true, getPlayer().getLocation(), true}
        ));
    }

    /**
     * Changes the skin of that player.<br>
     * Warning: this is for server-side, not proxy-side.<br>
     * If you use Bungeecord, please use the method "requestChangeSkin" of BungeeAPI instead.
     * @param skin the skin
     * @param viewers the array of viewers
     */
    public void changeSkin(Skin skin, Player... viewers){
        World w = getPlayer().getWorld();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, getPlayer()).sendWorld(w);
        EntityDestroy.create(getPlayer().getEntityId()).sendPlayers(viewers);
        new GameProfileManager(getPlayer()).setSkin(skin).apply(getPlayer());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, getPlayer()).sendWorld(w);
        NamedEntitySpawn.create(getPlayer()).sendPlayers(viewers);

        // requests the player client to reload the player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        Object craftWorld = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, getPlayer().getWorld());
        Object worldServer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorld);
        int dimension = (int) ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, getPlayer());
        Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object playerList = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftServer, craftServer);
        ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityPlayer, int.class, boolean.class, Location.class, boolean.class},
                new Object[]{nmsEntityPlayer, dimension, true, getPlayer().getLocation(), true}
        ));
    }
}
