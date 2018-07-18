package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.awt.*;

/**
 * A class helps you to send particle packets
 */
public class Particle {
    public class Dust {
        private Color color;
        private float size;

        public Dust(Color color, float size) {
            this.color = color;
            this.size = size;
        }

        public Color getColor() {
            return color;
        }

        public float getSize() {
            return size;
        }
    }

    public enum Type{
        EXPLOSION_NORMAL(0),
        EXPLOSION_LARGE(1),
        EXPLOSION_HUGE(2),
        FIREWORKS_SPARK(3),
        WATER_BUBBLE(4),
        WATER_SPLASH(5),
        WATER_WAKE(6),
        SUSPENDED(7),
        SUSPENDED_DEPTH(8),
        CRIT(9),
        CRIT_MAGIC(10),
        SMOKE_NORMAL(11),
        SMOKE_LARGE(12),
        SPELL(13),
        SPELL_INSTANT(14),
        SPELL_MOB(15),
        SPELL_MOB_AMBIENT(16),
        SPELL_WITCH(17),
        DRIP_WATER(18),
        DRIP_LAVA(19),
        VILLAGER_ANGRY(20),
        VILLAGER_HAPPY(21),
        TOWN_AURA(22),
        NOTE(23),
        PORTAL(24),
        ENCHANTMENT_TABLE(25),
        FLAME(26),
        LAVA(27),
        FOOTSTEP(28),
        CLOUD(29),
        REDSTONE(30),
        SNOWBALL(31),
        SNOW_SHOVEL(32),
        SLIME(33),
        HEART(34),
        BARRIER(35),
        ITEM_CRACK(36),
        BLOCK_CRACK(37),
        BLOCK_DUST(38),
        WATER_DROP(39),
        ITEM_TAKE(40),
        MOB_APPEARANCE(41),
        DRAGON_BREATH(42),
        END_ROD(43),
        DAMAGE_INDICATOR(44),
        SWEEP_ATTACK(45),
        FALLING_DUST(46),
        TOTEM(47),
        SPIT(48);

        private int id;

        Type(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }

    public static PacketSender create(Type type, Location location, int count){
        return create(type, location, count, 0, 0, 0, false, 0, Material.AIR, 0);
    }

    public static PacketSender create(Type type, Location location, Color color){
        float offsetX = (float) color.getRed() / 255;
        float offsetY = (float) color.getGreen() / 255;
        float offsetZ = (float) color.getBlue() / 255;

        if (offsetX < 0) {
            offsetX = 0;
        }
        if (offsetY < 0) {
            offsetY = 0;
        }
        if (offsetZ < 0) {
            offsetZ = 0;
        }

        return create(type, location, 0, offsetX, offsetY, offsetZ, false, 1, Material.AIR, 0);
    }

    public static PacketSender create(Color color, Location location){
        float offsetX = (float) color.getRed() / 255;
        float offsetY = (float) color.getGreen() / 255;
        float offsetZ = (float) color.getBlue() / 255;

        if (offsetX < 0) {
            offsetX = 0;
        }
        if (offsetY < 0) {
            offsetY = 0;
        }
        if (offsetZ < 0) {
            offsetZ = 0;
        }

        return create(Type.SPELL_MOB, location, 0, offsetX, offsetY, offsetZ, false, 1, Material.AIR, 0);
    }

    public static PacketSender create(Type type, Location location, int count, int speed){
        return create(type, location, count, 0, 0, 0, false, speed, Material.AIR, 0);
    }

    public static PacketSender create(Type type, Location location, int count, Material material, int data){
        return create(type, location, count, 0, 0, 0, false, 0, material, data);
    }

    public static PacketSender create(Type type, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float speed, Material material, int data){
        // TO-DO: supports 1.13
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> enumParticleClass = Class.forName("net.minecraft.server."+v+".EnumParticle");
            Object enumParticle = ReflectionUtils.getEnum(type.toString(), enumParticleClass);
            Class<?> packetPlayOutWorldParticlesClass = Class.forName("net.minecraft.server."+v+".PacketPlayOutWorldParticles");
            int[] i = new int[]{};
            if(type.equals(Type.ITEM_CRACK)){
                i = new int[]{material.getId(), data};
            }
            else if(type.equals(Type.BLOCK_CRACK) || type.equals(Type.BLOCK_DUST) ||
                    type.equals(Type.FALLING_DUST)){
                i = new int[]{material.getId() + (data << 12)};
            }
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutWorldParticlesClass, new Group<>(
                    new Class<?>[]{enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class},
                    new Object[]{enumParticle, longDistance,
                            (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count, i}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}