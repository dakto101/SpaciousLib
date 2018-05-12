package org.anhcraft.spaciouslib.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;

public class ColorUtils {
    public static Color toBukkitColor(java.awt.Color color){
        return toBukkitColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color toBukkitColor(int red, int green, int blue){
        int x = Integer.MAX_VALUE;
        Color color = null;
        for(DyeColor dye : DyeColor.values()){
            Color c = dye.getColor();
            int n = ((red - c.getRed()) ^ 2 + (green - c.getGreen()) ^ 2 + (blue - c.getBlue()) ^ 2) / 3;
            if(n < x){
                x = n;
                color = c;
            }
        }
        return color;
    }
}
