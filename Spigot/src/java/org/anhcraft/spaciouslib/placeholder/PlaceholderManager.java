package org.anhcraft.spaciouslib.placeholder;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.utils.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaceholderManager {
    private static LinkedHashMap<String, Placeholder> data = new LinkedHashMap<>();

    public PlaceholderManager(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Placeholder p : data.values()) {
                    if(p instanceof FixedPlaceholder) {
                        ((FixedPlaceholder) p).updateCache();
                    }
                }
            }
        }.runTaskTimer(SpaciousLib.instance, 100, 100);

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
                return "{player_display_name}";
            }

            @Override
            public String getValue(Player player) {
                return player.getDisplayName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_world}";
            }

            @Override
            public String getValue(Player player) {
                return player.getWorld().getName();
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_x}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getX()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_y}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getY()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_z}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getLocation().getZ()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_yaw}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getLocation().getYaw()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_pitch}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getLocation().getPitch()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_max_health}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getMaxHealth()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_health}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getHealth()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_health_scale}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round(player.getHealthScale()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exhaustion}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getExhaustion()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exp}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getExp()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_walk_speed}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getWalkSpeed()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_fly_speed}";
            }

            @Override
            public String getValue(Player player) {
                return Double.toString(MathUtils.round((double) player.getFlySpeed()));
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getLevel());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_food_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getFoodLevel());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_total_exp}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getTotalExperience());
            }
        });

        register(new FixedPlaceholder() {
            @Override
            public String getPlaceholder() {
                return "{player_exp_to_level}";
            }

            @Override
            public String getValue(Player player) {
                return Integer.toString(player.getExpToLevel());
            }
        });
    }

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

    public static void unregister(Placeholder placeholder){
        data.remove(placeholder.getPlaceholder());
    }

    public static void unregister(String placeholder){
        data.remove(placeholder);
    }

    public static String replace(String text, Player player){
        for(Placeholder p : data.values()){
            if(p instanceof FixedPlaceholder){
                text = text.replace(p.getPlaceholder(), ((FixedPlaceholder) p).getCache(player));
            } else {
                text = text.replace(p.getPlaceholder(), p.getValue(player));
            }
        }
        return text;
    }

    public static void updateCache(Player player) {
        for(Placeholder p : data.values()) {
            if(p instanceof FixedPlaceholder) {
                ((FixedPlaceholder) p).updateCache(player);
            }
        }
    }

    public static List<Placeholder> getPlaceholders() {
        return new ArrayList<>(data.values());
    }
}
