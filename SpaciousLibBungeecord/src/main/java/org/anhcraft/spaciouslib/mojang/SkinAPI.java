package org.anhcraft.spaciouslib.mojang;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.utils.GZipUtils;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.TimeUnit;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * A class helps you to manage player skins
 */
public class SkinAPI {
    private static LinkedHashMap<UUID, CachedSkin> cachedSkins;

    /**
     * Initializes SkinAPI
     */
    public SkinAPI() {
        try {
            cachedSkins = new LinkedHashMap<>();
            for(File file : SpaciousLib.SKINS_FOLDER.listFiles((dir, name) -> name.endsWith(".skin2"))) {
                CachedSkin cs = DataSerialization.deserialize(CachedSkin.class, GZipUtils.decompress(new FileManager(file).read()));
                cachedSkins.put(cs.getOwner(), cs);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downloads a skin from Mojang server and gets the result as a CachedSkin object.<br>
     * Warning: The limit request is 1 request/minute
     * @param player the unique id of the skin owner
     * @param cachedTime the cached time
     * @return the CachedSkin object
     */
    public static CachedSkin downloadSkin(UUID player, int cachedTime) throws Exception {
        Group<String, String> data = MojangAPI.getSkin(player);
        return new CachedSkin(new Skin(data.getA(), data.getB()), player, cachedTime);
    }

    /**
     * Gets a cached skin.<br>
     * It will download a new one if the skin doesn't exist.
     * @param player the unique id of the skin owner
     * @return the CachedSkin object
     */
    public static CachedSkin getSkin(UUID player) throws Exception {
        if(!cachedSkins.containsKey(player)){
            cachedSkins.put(player, downloadSkin(player, (int) (7 * TimeUnit.DAY.getSeconds())));
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
    public static CachedSkin getSkin(UUID player, int cachedTime) throws Exception {
        if(!cachedSkins.containsKey(player)){
            cachedSkins.put(player, downloadSkin(player, cachedTime));
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
    public static CachedSkin renewSkin(UUID player) throws Exception {
        if(cachedSkins.containsKey(player)) {
            CachedSkin cs = cachedSkins.get(player);
            cachedSkins.remove(player);
            return getSkin(player, cs.getCachedTime());
        }
        return null;
    }

    /**
     * Gets the cache file of the given skin
     * @param skin the CachedSkin object
     * @return the File object
     */
    public static File getSkinFile(CachedSkin skin) {
       return new File(SpaciousLib.SKINS_FOLDER, skin.getOwner().toString()+".skin2");
    }
}
