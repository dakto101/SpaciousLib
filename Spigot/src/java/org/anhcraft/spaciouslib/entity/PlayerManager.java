package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.NamedEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PlayerInfo;
import org.anhcraft.spaciouslib.utils.GameVersion;
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
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, getPlayer());
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            return (int) ReflectionUtils.getField("ping", nmsEntityPlayerClass, nmsEntity);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
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
        String v = GameVersion.getVersion().toString();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, getPlayer()).sendWorld(w);
        EntityDestroy.create(getPlayer().getEntityId()).sendPlayers(players);
        new GameProfileManager(getPlayer()).setSkin(skin).apply(getPlayer());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, getPlayer()).sendWorld(w);
        NamedEntitySpawn.create(getPlayer()).sendPlayers(players);

        // requests the player client to reload the player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftServer");
            Class<?> nmsPlayerListClass = Class.forName("net.minecraft.server." + v + ".PlayerList");
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
            Class<?> nmsWorldServerClass = Class.forName("net.minecraft.server." + v + ".WorldServer");
            Object craftWorld = ReflectionUtils.cast(craftWorldClass, getPlayer().getWorld());
            Object worldServer = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
            int dimension = (int) ReflectionUtils.getField("dimension", nmsWorldServerClass, worldServer);
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, getPlayer());
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            Object playerList = ReflectionUtils.getMethod("getHandle", craftServerClass, craftServer);
            ReflectionUtils.getMethod("moveToWorld", nmsPlayerListClass, playerList, new Group<>(
                    new Class<?>[]{nmsEntityPlayerClass, int.class, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, getPlayer().getLocation(), true}
            ));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        String v = GameVersion.getVersion().toString();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, getPlayer()).sendWorld(w);
        EntityDestroy.create(getPlayer().getEntityId()).sendPlayers(viewers);
        new GameProfileManager(getPlayer()).setSkin(skin).apply(getPlayer());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, getPlayer()).sendWorld(w);
        NamedEntitySpawn.create(getPlayer()).sendPlayers(viewers);

        // requests the player client to reload the player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftServer");
            Class<?> nmsPlayerListClass = Class.forName("net.minecraft.server." + v + ".PlayerList");
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
            Class<?> nmsWorldServerClass = Class.forName("net.minecraft.server." + v + ".WorldServer");
            Object craftWorld = ReflectionUtils.cast(craftWorldClass, getPlayer().getWorld());
            Object worldServer = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
            int dimension = (int) ReflectionUtils.getField("dimension", nmsWorldServerClass, worldServer);
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, getPlayer());
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            Object playerList = ReflectionUtils.getMethod("getHandle", craftServerClass, craftServer);
            ReflectionUtils.getMethod("moveToWorld", nmsPlayerListClass, playerList, new Group<>(
                    new Class<?>[]{nmsEntityPlayerClass, int.class, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, getPlayer().getLocation(), true}
            ));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void respawn(){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> playerConnClass = Class.forName("net.minecraft.server." + v + ".PlayerConnection");
            Class<?> packetClass = Class.forName("net.minecraft.server." + v + ".PacketPlayInClientCommand");
            Class<?> enumClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "PacketPlayInClientCommand$") + "EnumClientCommand");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, getPlayer());
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object enum_ = ReflectionUtils.getEnum("PERFORM_RESPAWN", enumClass);
            Object packet = ReflectionUtils.getConstructor(packetClass, new Group<>(
                    new Class<?>[]{enumClass},
                    new Object[]{enum_}
            ));
            Object playerConn = ReflectionUtils.getField("playerConnection", nmsEntityPlayerClass, nmsEntityPlayer);
            ReflectionUtils.getMethod("a", playerConnClass, playerConn, new Group<>(
                    new Class<?>[]{packetClass},
                    new Object[]{packet}
            ));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
