package org.anhcraft.spaciouslib.placeholder;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anhcraft.spaciouslib.SpaciousLib;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class helps you to manage placeholders
 */
public class PlaceholderAPI {
    private static LinkedHashMap<String, Placeholder> data = new LinkedHashMap<>();

    /**
     * Initializes PlaceholderAPI
     */
    public PlaceholderAPI(){
        ProxyServer.getInstance().getScheduler().schedule(SpaciousLib.instance, new Runnable() {
            @Override
            public void run() {
                for(Placeholder p : data.values()) {
                    if(p instanceof CachedPlaceholder && !(p instanceof FixedPlaceholder)){
                        ((CachedPlaceholder) p).updateCache();
                    }
                }
            }
        }, 20, 20, TimeUnit.SECONDS);

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_name}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return player.getName();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_display_name}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return player.getDisplayName();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ping}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return Integer.toString(player.getPing());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_server}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return player.getServer().getInfo().getName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_id}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return player.getUniqueId().toString();
            }
        });

        for(ProxiedPlayer player: ProxyServer.getInstance().getPlayers()) {
            PlaceholderAPI.updateCache(player);
        }
    }

    /**
     * Registers the given placeholder
     * @param placeholder Placeholder object
     */
    public static void register(Placeholder placeholder){
        if(data.containsKey(placeholder.getPlaceholder())){
            try {
                throw new Exception("Placeholder have already initialized!");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(placeholder instanceof CachedPlaceholder){
            ((CachedPlaceholder) placeholder).updateCache();
        }
        data.put(placeholder.getPlaceholder(), placeholder);
    }

    /**
     * Unregisters the given placeholder
     * @param placeholder Placeholder object
     */
    public static void unregister(Placeholder placeholder){
        data.remove(placeholder.getPlaceholder());
    }

    /**
     * Unregisters the given placeholder
     * @param placeholder the placeholder name
     */
    public static void unregister(String placeholder){
        data.remove(placeholder);
    }

    /**
     * Replaces all placeholders in the given text
     * @param text a text
     * @param player a player
     * @return the replaced text
     */
    public static String replace(String text, ProxiedPlayer player){
        for(Placeholder p : data.values()){
            if(p instanceof CachedPlaceholder){
                text = text.replace(p.getPlaceholder(), ((CachedPlaceholder) p).getCache(player));
            } else {
                text = text.replace(p.getPlaceholder(), p.getValue(player));
            }
        }
        return text;
    }

    /**
     * Updates all placeholder caches of a player
     * @param player the player
     */
    public static void updateCache(ProxiedPlayer player) {
        for(Placeholder p : data.values()) {
            if(p instanceof CachedPlaceholder) {
                ((CachedPlaceholder) p).updateCache(player);
            }
        }
    }

    /**
     * Gets all placeholders
     * @return list of placeholders
     */
    public static List<Placeholder> getPlaceholders() {
        return new ArrayList<>(data.values());
    }
}
