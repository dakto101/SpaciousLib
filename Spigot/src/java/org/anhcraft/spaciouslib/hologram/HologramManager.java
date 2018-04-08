package org.anhcraft.spaciouslib.hologram;

import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.LivingEntitySpawn;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Location;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class HologramManager {
    private static LinkedHashMap<String, Group<Hologram, LinkedList<Integer>>> data = new LinkedHashMap<>();

    public static void register(Hologram hologram){
        Group<Hologram, LinkedList<Integer>> hg = new Group<>(hologram, new LinkedList<>());
        if(data.containsKey(hologram.getID())){
            hg = data.get(hologram.getID());
        }
        for(int hw : hg.getB()){
            EntityDestroy.create(hw).sendPlayers(hg.getA().getViewers());
        }
        LinkedList<Integer> nhw = new LinkedList<>();
        int i = 0;
        for(String line : hologram.getLines()){
            double y = i * hologram.getLineSpacing();
            Location location = hologram.getLocation().clone().add(0, y, 0);
            try {
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".Entity");
                Class<?> nmsArmorStandClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".EntityArmorStand");
                Object craftWorld = ReflectionUtils.cast(craftWorldClass, location.getWorld());
                Object nmsWorld = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
                Object nmsArmorStand = ReflectionUtils.getConstructor(nmsArmorStandClass, new Group<>(
                        new Class<?>[]{nmsWorldClass}, new Object[]{nmsWorld}
                ));
                ReflectionUtils.getMethod("setLocation", nmsEntityClass, nmsArmorStand, new Group<>(
                        new Class<?>[]{
                                double.class,
                                double.class,
                                double.class,
                                float.class,
                                float.class,
                        }, new Object[]{
                                location.getX(),
                                location.getY(),
                                location.getZ(),
                                location.getYaw(),
                                location.getPitch(),
                        }
                ));
                if(GameVersion.is1_9Above()) {
                    ReflectionUtils.getMethod("setMarker", nmsArmorStandClass, nmsArmorStand,
                            new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                    );
                }
                ReflectionUtils.getMethod("setBasePlate", nmsArmorStandClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{false})
                );
                ReflectionUtils.getMethod("setInvisible", nmsArmorStandClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                );
                ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{String.class}, new Object[]{line})
                );
                ReflectionUtils.getMethod("setCustomNameVisible", nmsEntityClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                );
                ReflectionUtils.getMethod("setSmall", nmsArmorStandClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                );
                if(GameVersion.is1_10Above()) {
                    ReflectionUtils.getMethod("setNoGravity", nmsEntityClass, nmsArmorStand,
                            new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                    );
                } else {
                    ReflectionUtils.getMethod("setGravity", nmsArmorStandClass, nmsArmorStand,
                            new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                    );
                }
                ReflectionUtils.getMethod("setArms", nmsArmorStandClass, nmsArmorStand,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{false})
                );
                int entityID = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, nmsArmorStand);
                nhw.add(entityID);
                LivingEntitySpawn.create(nmsArmorStand).sendPlayers(hologram.getViewers());
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        hg.setB(nhw);
        hg.setA(hologram);
        data.put(hologram.getID(), hg);
    }

    public static void unregister(Hologram hologram){
        if(data.containsKey(hologram.getID())){
            LinkedList<Integer> hg = data.get(hologram.getID()).getB();
            for(int hw : hg){
                EntityDestroy.create(hw).sendPlayers(hologram.getViewers());
            }
            data.remove(hologram.getID());
        }
    }

    public static void unregisterAll(){
        for(String id : data.keySet()){
            unregister(data.get(id).getA());
        }
    }
}
