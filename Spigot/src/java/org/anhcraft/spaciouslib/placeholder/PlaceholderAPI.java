package org.anhcraft.spaciouslib.placeholder;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.utils.MathUtils;
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
    private static LinkedHashMap<String, Placeholder> data = new LinkedHashMap<>();

    /**
     * Initializes PlaceholderAPI
     */
    public PlaceholderAPI(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Placeholder p : data.values()) {
                    if(p instanceof CachedPlaceholder
                            && !(p instanceof FixedPlaceholder)) {
                        ((CachedPlaceholder) p).updateCache();
                    }
                }
            }
        }.runTaskTimer(SpaciousLib.instance, 200, 200);

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

        for(Player player : Bukkit.getServer().getOnlinePlayers()){
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

    public static String replace(String text, Player player){
        for(Placeholder p : data.values()){
            if(p instanceof CachedPlaceholder){
                text = text.replace(p.getPlaceholder(), ((CachedPlaceholder) p).getCache(player));
            } else {
                text = text.replace(p.getPlaceholder(), p.getValue(player));
            }
        }
        return text;
    }

    public static void updateCache(Player player) {
        for(Placeholder p : data.values()) {
            if(p instanceof CachedPlaceholder) {
                ((CachedPlaceholder) p).updateCache(player);
            }
        }
    }

    public static List<Placeholder> getPlaceholders() {
        return new ArrayList<>(data.values());
    }
}
