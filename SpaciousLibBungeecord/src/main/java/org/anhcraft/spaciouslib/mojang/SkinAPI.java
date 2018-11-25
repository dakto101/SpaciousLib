package org.anhcraft.spaciouslib.mojang;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.TimeUnit;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * An API of skins
 */
public class SkinAPI {
    private static final LinkedHashMap<UUID, CachedSkin> cachedSkins = new LinkedHashMap<>();
    private static final String ext = ".skin3";
    
    /**
     * Initializes SkinAPI
     */
    public SkinAPI() {
        try {
            for(File file : SpaciousLib.SKINS_FOLDER.listFiles((dir, name) -> name.endsWith(ext))) {
                CachedSkin cs = DataSerialization.deserialize(CachedSkin.class, new FileManager(file).read());
                cachedSkins.put(cs.getOwner(), cs);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download the skin of a profile from Mojang server.<br>
     * The limit request is 1 request per 1 minute
     * @param profileId the unique id of the profile
     * @param cachedTime caching time
     * @return CachedSkin
     */
    public static CachedSkin downloadSkin(UUID profileId, int cachedTime) throws Exception {
        Group<String, String> data = MojangAPI.getSkin(profileId);
        return new CachedSkin(new Skin(data.getA(), data.getB()), profileId, cachedTime);
    }

    /**
     * Get the skin of a profile (or download it if it is not existed).<br>
     * By getting, the skin will be stored and cached
     * @param profileId the unique id of the profile
     * @return CachedSkin
     */
    public static CachedSkin getSkin(UUID profileId) throws Exception {
        if(!cachedSkins.containsKey(profileId)){
            cachedSkins.put(profileId, downloadSkin(profileId, (int) (7 * TimeUnit.DAY.getSeconds())));
        }
        return cachedSkins.get(profileId);
    }

    /**
     * Get the skin of a profile (or download it if it is not existed).<br>
     * By getting, the skin will be stored and cached
     * @param profileId the unique id of the profile
     * @param cachedTime the caching time
     * @return CachedSkin
     */
    public static CachedSkin getSkin(UUID profileId, int cachedTime) throws Exception {
        if(!cachedSkins.containsKey(profileId)){
            cachedSkins.put(profileId, downloadSkin(profileId, cachedTime));
        }
        return cachedSkins.get(profileId);
    }

    /**
     * Get all cached skins
     * @return a map of cached skins
     */
    public static LinkedHashMap<UUID, CachedSkin> getSkins(){
        return cachedSkins;
    }

    /**
     * Forces renew an existed cached skin.<br>
     * All changes are temporary and will be removed
     * @param profileId the unique id of the skin owner
     * @return new CachedSkin object
     */
    public static CachedSkin renewSkin(UUID profileId) throws Exception {
        if(cachedSkins.containsKey(profileId)) {
            CachedSkin cs = cachedSkins.get(profileId);
            cachedSkins.remove(profileId);
            return getSkin(profileId, cs.getCachedTime());
        }
        return null;
    }

    /**
     * Get the cache file of the given skin
     * @param skin the CachedSkin
     * @return file
     */
    public static File getSkinFile(CachedSkin skin) {
       return new File(SpaciousLib.SKINS_FOLDER, skin.getOwner().toString()+ext);
    }
}
