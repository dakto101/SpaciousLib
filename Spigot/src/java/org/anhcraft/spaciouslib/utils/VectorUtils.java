package org.anhcraft.spaciouslib.utils;

import org.bukkit.util.Vector;

public class VectorUtils {
    /**
     * Serializes the given vector to string
     * @param vec a vector object
     * @return a string represents for the object
     */
    public static String vec2str(Vector vec) {
        if(vec == null){
            return "null";
        }
        return Double.toString(vec.getX()) + ":" +
                Double.toString(vec.getY()) + ":" + Double.toString(vec.getZ());
    }

    /**
     * Deserialize the given string to its original vector object
     * @param str a string represents for the object
     * @return the vector
     */
    public static Vector str2vec(String str) {
        if(str.equalsIgnoreCase("null")){
            return new Vector();
        }
        String str2vec[] = str.split(":");
        return new Vector(Double.parseDouble(str2vec[0]),
                Double.parseDouble(str2vec[1]),Double.parseDouble(str2vec[2]));
    }

    // https://www.spigotmc.org/threads/rotating-particle-effects.166854/
    public static Vector rotateAroundAxisX(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector rotateAroundAxisZ(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
