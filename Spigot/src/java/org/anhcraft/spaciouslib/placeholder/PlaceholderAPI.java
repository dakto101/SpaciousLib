package org.anhcraft.spaciouslib.placeholder;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.utils.MathUtils;
import org.anhcraft.spaciouslib.utils.ServerUtils;
import org.anhcraft.spaciouslib.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Placeholder p : data.values()) {
                        if(p instanceof CachedPlaceholder && !(p instanceof FixedPlaceholder)) {
                            ((CachedPlaceholder) p).updateCache();
                        }
                    }
                }
            }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, 20 * SpaciousLib.
                    config.getLong("placeholder_cache_duration"));
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Placeholder p : data.values()) {
                        if(p instanceof CachedPlaceholder && !(p instanceof FixedPlaceholder)) {
                            ((CachedPlaceholder) p).updateCache();
                        }
                    }
                }
            }.runTaskTimer(SpaciousLib.instance, 0, 20 * SpaciousLib.
                    config.getLong("placeholder_cache_duration"));
        }

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_name}";
            }

            @Override
            public String getValue(Player player) {
                return player.getName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_id}";
            }

            @Override
            public String getValue(Player player) {
                return player.getUniqueId().toString();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_display_name}";
            }

            @Override
            public String getValue(Player player) {
                return player.getDisplayName();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ping}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(new PlayerManager(player).getPing());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_world}";
            }

            @Override
            public String getValue(Player player) {
                return player.getWorld().getName();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_x}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getX()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_y}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getY()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_z}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getZ()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_yaw}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getLocation().getYaw()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_pitch}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getLocation().getPitch()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_max_health}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getMaxHealth()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_health}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getHealth()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_health_scale}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getHealthScale()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exhaustion}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getExhaustion()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exp}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getExp()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_walk_speed}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getWalkSpeed()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_fly_speed}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getFlySpeed()));
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getLevel());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_food_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getFoodLevel());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_total_exp}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getTotalExperience());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exp_to_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getExpToLevel());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ip_host}";
            }

            @Override
            public String getValue(Player player) {
                return player.getAddress().getHostName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_ip_port}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getAddress().getPort());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{vault_balance}";
            }

            @Override
            public String getValue(Player player) {
                try {
                    return Double.toString(Math.round(VaultUtils.getBalance(player)));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return "0";
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{vault_permission_group}";
            }

            @Override
            public String getValue(Player player) {
                try {
                    return VaultUtils.getPrimaryPermissionGroup(player);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                return "0";
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{server_name}";
            }

            @Override
            public String getValue(Player player) {
                return Bukkit.getServer().getServerName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{server_motd}";
            }

            @Override
            public String getValue(Player player) {
                return Bukkit.getServer().getMotd();
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{server_online}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(Bukkit.getServer().getOnlinePlayers().size());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{server_max_players}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(Bukkit.getServer().getMaxPlayers());
            }
        });

        register(new CachedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{server_tps}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(ServerUtils.getTPS()[0]));
            }
        });

        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            PlaceholderAPI.updateCache(player);
        }
    }

    /**
     * Registers the given placeholder
     * @param placeholder Placeholder object
     */
    public static void register(Placeholder placeholder){
        if(!data.containsKey(placeholder.getPlaceholder()) && placeholder instanceof CachedPlaceholder){
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
    public static String replace(String text, Player player){
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
    public static void updateCache(Player player) {
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