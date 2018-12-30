package org.anhcraft.spaciouslib.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.anhcraft.spaciouslib.builders.ProfileBuilder;
import org.anhcraft.spaciouslib.listeners.PlayerListener;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.NamedEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerUtils {
    /**
     * Get the ping number of the given player at the moment
     * @param player player
     * @return number of ping
     */
    public static int getPing(Player player){
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, player);
        Object nmsEntity = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        return (int) ReflectionUtils.getField("ping", ClassFinder.NMS.EntityPlayer, nmsEntity);
    }

    /**
     * Get the offline id of the given player
     * @param player player
     * @return offline id
     */
    public static UUID getOfflineId(String player){
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Get the profile of the given player
     * @param player player
     * @return game profile
     */
    public static GameProfile getProfile(Player player){
        Object craftHumanEntity = ReflectionUtils.cast(ClassFinder.CB.CraftHumanEntity, player);
        Object entityHuman = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftHumanEntity, craftHumanEntity);
        return (GameProfile) ReflectionUtils.getMethod("getProfile", ClassFinder.NMS.EntityHuman, entityHuman);
    }

    /**
     * Overrides the profile of the given player
     * @param player player
     * @param profile new profile
     */
    public static void setProfile(Player player, GameProfile profile){
        String[] fields = new String[]{"bF","bH","bH","bR","bS","bT","bS","g","h","h"};
        Object craftHumanEntity = ReflectionUtils.cast(ClassFinder.CB.CraftHumanEntity, player);
        Object entityHuman = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftHumanEntity, craftHumanEntity);
        ReflectionUtils.setField(fields[GameVersion.getVersion().getId()], ClassFinder.NMS.EntityHuman, entityHuman, profile);
    }

    /**
     * Get the current skin of the given player
     * @param player player
     * @return the skin
     */
    public static Skin getSkin(Player player){
        GameProfile gp = getProfile(player);
        if(gp.getProperties().containsKey("textures")){
            Collection<Property> v = gp.getProperties().get("textures");
            if(v.size() > 0){
                Property s = v.iterator().next();
                return new Skin(s.getValue(), s.getSignature());
            }
        }
        return null;
    }

    /**
     * Change the given player's skin.<br>
     * Only affecting any viewers within the defined view distance.<br>
     * Using this for a server which is under a proxy like Bungeecord is impossible. Use the alternative method {@link org.anhcraft.spaciouslib.bungee.BungeeAPI#requestChangeSkin(String, Skin)} instead.
     * @param player the player
     * @param skin new skin
     */
    public static void changeSkin(Player player, Skin skin){
        int d = Bukkit.getViewDistance();
        List<Player> players = player.getWorld().getNearbyEntities(player.getLocation(), d, d, d).stream().filter(entity -> entity instanceof Player && ((Player) entity).canSee(player)).map(entity -> (Player) entity).collect(Collectors.toList());
        World w = player.getWorld();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, player).sendWorld(w);
        EntityDestroy.create(player.getEntityId()).sendPlayers(players);
        setProfile(player, new ProfileBuilder(getProfile(player)).setSkin(skin).build());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, player).sendWorld(w);
        NamedEntitySpawn.create(player).sendPlayers(players);

        // changing own player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, player);
        Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object playerList = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftServer, craftServer);
        Object craftWorld = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, player.getWorld());
        Object worldServer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorld);
        if(GameVersion.v1_13_R2.getId() <= GameVersion.getVersion().getId()){
            Object dimension = ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
            ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.EntityPlayer, ClassFinder.NMS.DimensionManager, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, player.getLocation(), true}
            ));
        } else {
            int dimension = (int) ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
            ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.EntityPlayer, int.class, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, player.getLocation(), true}
            ));
        }
    }

    /**
     * Change the given player's skin.<br>
     * Only affecting any viewers which is defined from the parameter "viewers".<br>
     * Using this for a server which is under a proxy like Bungeecord is impossible. Use the alternative method {@link org.anhcraft.spaciouslib.bungee.BungeeAPI#requestChangeSkin(String, Skin)} instead.
     * @param player the player
     * @param skin new skin
     */
    public static void changeSkin(Player player, Skin skin, Player... viewers){
        World w = player.getWorld();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, player).sendWorld(w);
        EntityDestroy.create(player.getEntityId()).sendPlayers(viewers);
        setProfile(player, new ProfileBuilder(getProfile(player)).setSkin(skin).build());
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, player).sendWorld(w);
        NamedEntitySpawn.create(player).sendPlayers(viewers);

        // changing own player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, player);
        Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object playerList = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftServer, craftServer);
        Object craftWorld = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, player.getWorld());
        Object worldServer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorld);
        if(GameVersion.v1_13_R2.getId() <= GameVersion.getVersion().getId()){
            Object dimension = ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
            ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.EntityPlayer, ClassFinder.NMS.DimensionManager, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, player.getLocation(), true}
            ));
        } else {
            int dimension = (int) ReflectionUtils.getField("dimension", ClassFinder.NMS.WorldServer, worldServer);
            ReflectionUtils.getMethod("moveToWorld", ClassFinder.NMS.PlayerList, playerList, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.EntityPlayer, int.class, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, player.getLocation(), true}
            ));
        }
    }

    /**
     * Freeze the given player.<br>
     * He will not be allowed to move over a half of block away
     * @param player player
     */
    public static void freeze(Player player){
        PlayerListener.freezedPlayers.put(player.getUniqueId(), player.getLocation());
    }

    /**
     * Unfreeze the given player
     * @param player player
     */
    public static void unfreeze(Player player){
        PlayerListener.freezedPlayers.remove(player.getUniqueId());
    }

    /**
     * Forces the given player to execute the command as a fake operator
     * @param player player
     * @param command command
     */
    public static void execCmdAsOp(Player player, String command){
        if(player.isOp()){
            player.performCommand(command);
        } else {
            player.setOp(true);
            player.setOp(false);
        }
    }
}
