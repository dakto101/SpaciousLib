package org.anhcraft.spaciouslib.placeholder;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class helps you to manage placeholders
 */
public class PlaceholderAPI {
    private static final LinkedHashMap<String, Placeholder> data = new LinkedHashMap<>();
    /**
     * Initializes PlaceholderAPI
     */
    public PlaceholderAPI(){
        if(SpaciousLib.config.getBoolean("placeholder_cache_async", true)) {
            ProxyServer.getInstance().getScheduler().schedule(SpaciousLib.instance, () -> {
                for(Placeholder p : data.values()) {
                    if(p instanceof CachedPlaceholder && !(p instanceof FixedPlaceholder)){
                        ((CachedPlaceholder) p).updateCache();
                    }
                }
            }, 0, SpaciousLib.
                    config.getLong("placeholder_cache_duration"), TimeUnit.SECONDS);
        } else {
            ProxyServer.getInstance().getScheduler().schedule(SpaciousLib.instance, () -> {
                ProxyServer.getInstance().getScheduler().runAsync(SpaciousLib.instance, () -> {
                    for(Placeholder p : data.values()) {
                        if(p instanceof CachedPlaceholder && !(p instanceof FixedPlaceholder)){
                            ((CachedPlaceholder) p).updateCache();
                        }
                    }
                });
            }, 0, SpaciousLib.
                    config.getLong("placeholder_cache_duration"), TimeUnit.SECONDS);
        }
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
                if(player.getServer() == null){
                    return "";
                }
                if(player.getServer().getInfo() == null){
                    return "";
                }
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

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ip_host}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return player.getPendingConnection().getVirtualHost().getHostName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ip_port}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return Integer.toString(player.getPendingConnection().getVirtualHost().getPort());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{proxy_name}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return BungeeCord.getInstance().getName();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{proxy_online}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return Integer.toString(BungeeCord.getInstance().getOnlineCount());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{proxy_max_players}";
            }

            @Override
            public String getValue(ProxiedPlayer player) {
                return Integer.toString(BungeeCord.getInstance().getConfig().getPlayerLimit());
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
            CachedPlaceholder cache = (CachedPlaceholder) placeholder;
            cache.updateCache();
            AnnotationHandler.register(CachedPlaceholder.class, cache);
        }
        data.put(placeholder.getPlaceholder(), placeholder);
    }

    /**
     * Unregisters the given placeholder
     * @param placeholder Placeholder object
     */
    public static void unregister(Placeholder placeholder){
        if(placeholder instanceof CachedPlaceholder){
            CachedPlaceholder cache = (CachedPlaceholder) placeholder;
            AnnotationHandler.unregister(CachedPlaceholder.class, cache);
        }
        data.remove(placeholder.getPlaceholder());
    }

    /**
     * Unregisters the given placeholder
     * @param placeholder the placeholder name
     */
    public static void unregister(String placeholder){
        Placeholder p = data.get(placeholder);
        if(p instanceof CachedPlaceholder){
            CachedPlaceholder cache = (CachedPlaceholder) p;
            AnnotationHandler.unregister(CachedPlaceholder.class, cache);
        }
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
            String x;
            if(p instanceof CachedPlaceholder){
                x = ((CachedPlaceholder) p).getCache(player);
            } else {
                x = p.getValue(player);
            }
            if(x != null) {
                text = text.replace(p.getPlaceholder(), x);
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
