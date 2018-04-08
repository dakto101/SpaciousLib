package org.anhcraft.spaciouslib.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.NamedEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PlayerInfo;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class SkinManager {
    private static LinkedHashMap<UUID, CachedSkin> cachedSkins;

    public SkinManager() {
        try {
            cachedSkins = new LinkedHashMap<>();
            for(File file : SpaciousLib.SKINS_FOLDER.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".skin");
                }
            })) {
                CachedSkin cs = fromJSON(new String(GZipUtils.decompress(new FileManager(file).read())));
                cachedSkins.put(cs.getOwner(), cs);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a cached skin
     * @param player the unique id of the skin owner
     * @param skin the CachedSkin object
     */
    public static void register(UUID player, CachedSkin skin){
        cachedSkins.put(player, skin);
    }

    /**
     * Unregisters a specific cached skin
     * @param player the unique id of the skin owner
     */
    public static void unregister(UUID player) throws Exception {
        if(cachedSkins.containsKey(player)) {
            new FileManager(getSkinFile(get(player))).delete();
            cachedSkins.remove(player);
        }
    }

    /**
     * Downloads a skin from Mojang server and gets the result as a CachedSkin object.<br>
     * Warning: The limit request is 1 request/minute
     * @param player the unique id of the skin owner
     * @param cachedTime the cached time
     * @return the CachedSkin object
     */
    public static CachedSkin downloadCachedSkin(UUID player, int cachedTime) throws Exception {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/"+player.toString().replace("-", "")+"?unsigned=false";
        String json = IOUtils.toString(new URL(url));
        JsonObject obj = new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType());
        if(obj.has("properties")){
            JsonArray properties = obj.get("properties").getAsJsonArray();
            if(0 < properties.size()){
                JsonObject property = properties.get(0).getAsJsonObject();
                if(property.get("name").getAsString().equals("textures")){
                    return new CachedSkin(new Skin(property.get("value").getAsString(),
                            property.get("signature").getAsString()), player, cachedTime);
                }
            }
        }
        return null;
    }

    /**
     * Gets a cached skin.<br>
     * It will download a new one if the skin doesn't exist.
     * @param player the unique id of the skin owner
     * @return the CachedSkin object
     */
    public static CachedSkin get(UUID player) throws Exception {
        if(!cachedSkins.containsKey(player)){
            cachedSkins.put(player, downloadCachedSkin(player, (int) (7 * TimeUnit.DAY.getSeconds())));
        }
        return cachedSkins.get(player);
    }

    /**
     * Gets a cached skin.<br>
     * It will download a new one if the skin doesn't exist.
     * @param player the unique id of the skin owner
     * @param cachedTime the cached time for the skin (only need when the skin doesn't exist)
     * @return the CachedSkin object
     */
    public static CachedSkin get(UUID player, int cachedTime) throws Exception {
        if(!cachedSkins.containsKey(player)){
            cachedSkins.put(player, downloadCachedSkin(player, cachedTime));
        }
        return cachedSkins.get(player);
    }

    /**
     * Gets all cached skins
     * @return a map of cached skins
     */
    public static LinkedHashMap<UUID, CachedSkin> getSkins(){
        return cachedSkins;
    }

    /**
     * Forces renew an existed cached skin.<br>
     * Warning: all changes are temporary and will be removed when the plugin loads
     * @param player the unique id of the skin owner
     * @return the new CachedSkin object
     */
    public static CachedSkin renew(UUID player) throws Exception {
        if(cachedSkins.containsKey(player)) {
            CachedSkin cs = cachedSkins.get(player);
            cachedSkins.remove(player);
            return get(player, cs.getCachedTime());
        }
        return null;
    }

    /**
     * Serializes the given CachedSkin object to a JSON string
     * @param skin the CachedSkin object
     * @return the JSON string
     */
    public static String toJSON(CachedSkin skin){
        JsonObject o = new JsonObject();
        o.addProperty("a", skin.getCachedTime());
        o.addProperty("b", skin.getExpiredTime());
        o.addProperty("c", skin.getOwner().toString());
        o.addProperty("d", skin.getSkin().getValue());
        o.addProperty("e", skin.getSkin().getSignature());
        return new Gson().toJson(o, new TypeToken<JsonObject>(){}.getType());
    }

    /**
     * Deserializes the given JSON string to its CachedSkin object
     * @param json the JSON string
     * @return the CachedSkin object
     */
    public static CachedSkin fromJSON(String json){
        JsonObject o = new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType());
        return new CachedSkin(new Skin(o.get("d").getAsString(),
                o.get("e").getAsString()),
                UUID.fromString(o.get("c").getAsString()),
                o.get("a").getAsInt(),
                o.get("b").getAsLong());
    }

    /**
     * Gets the cache file of the given skin
     * @param skin the CachedSkin object
     * @return the File object
     */
    public static File getSkinFile(CachedSkin skin) {
       return new File(SpaciousLib.SKINS_FOLDER, skin.getOwner().toString()+".skin");
    }

    /**
     * Changes the skin of the given player
     * @param player the player
     * @param skin the skin
     */
    public static void changeSkin(Player player, Skin skin){
        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        players.remove(player);
        new GameProfileManager(player).setSkin(skin).apply(player);
        GameVersion v = GameVersion.getVersion();
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, player).sendAll();
        EntityDestroy.create(player.getEntityId()).sendPlayers(players);
        PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, player).sendAll();
        NamedEntitySpawn.create(player).sendPlayers(players);
        // requests the player client to reload the player skin
        // https://www.spigotmc.org/threads/reload-skin-client-help.196072/#post-2043595
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".CraftServer");
            Class<?> nmsPlayerListClass = Class.forName("net.minecraft.server." + v.toString() + ".PlayerList");
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".CraftWorld");
            Class<?> nmsWorldServerClass = Class.forName("net.minecraft.server." + v.toString() + ".WorldServer");
            Object craftWorld = ReflectionUtils.cast(craftWorldClass, player.getWorld());
            Object worldServer = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
            int dimension = (int) ReflectionUtils.getField("dimension", nmsWorldServerClass, worldServer);
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, player);
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            Object playerList = ReflectionUtils.getMethod("getHandle", craftServerClass, craftServer);
            ReflectionUtils.getMethod("moveToWorld", nmsPlayerListClass, playerList, new Group<>(
                    new Class<?>[]{nmsEntityPlayerClass, int.class, boolean.class, Location.class, boolean.class},
                    new Object[]{nmsEntityPlayer, dimension, true, player.getLocation(), true}
            ));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
