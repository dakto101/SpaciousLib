package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;

public class GameVersion {
    public static GVersion getVersion(){
        return GVersion.valueOf(Bukkit.getServer().getClass().getPackage()
                .getName().replace(".",  ",").split(",")[3]);
    }

    public static Boolean is1_8Above(){
        return (Bukkit.getBukkitVersion().contains("1.8") ||
                Bukkit.getBukkitVersion().contains("1.9") ||
            Bukkit.getBukkitVersion().contains("1.10") ||
            Bukkit.getBukkitVersion().contains("1.11") ||
                Bukkit.getBukkitVersion().contains("1.12"));
    }

    public static Boolean is1_9Above(){
        return (Bukkit.getBukkitVersion().contains("1.9") ||
                Bukkit.getBukkitVersion().contains("1.10") ||
                Bukkit.getBukkitVersion().contains("1.11") ||
                Bukkit.getBukkitVersion().contains("1.12"));
    }

    public static Boolean is1_10Above(){
        return (Bukkit.getBukkitVersion().contains("1.10") ||
                Bukkit.getBukkitVersion().contains("1.11") ||
                Bukkit.getBukkitVersion().contains("1.12"));
    }

    public static Boolean is1_11Above(){
        return (Bukkit.getBukkitVersion().contains("1.11") ||
                Bukkit.getBukkitVersion().contains("1.12"));
    }

    public static Boolean is1_12Above(){
        return (Bukkit.getBukkitVersion().contains("1.12"));
    }
}
