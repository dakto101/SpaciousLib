package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Location;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Particle {
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

        public int getID(){
            return id;
        }
    }

    public static PacketSender create(Type type, Location location, int count){
        return create(type, (float) location.getX(), (float) location.getY(), (float) location.getZ(), count, 0, 0, 0, false, 0, Material.AIR, 0);
    }

    public static PacketSender create(Type type, float x, float y, float z, int count){
        return create(type, x, y, z, count, 0, 0, 0, false, 0, Material.AIR, 0);
    }

    public static PacketSender create(Type type, Location location, int count, Material material, int data){
        return create(type, (float) location.getX(), (float) location.getY(), (float) location.getZ(), count, 0, 0, 0, false, 0, material, data);
    }

    public static PacketSender create(Type type, float x, float y, float z, int count, Material material, int data){
        return create(type, x, y, z, count, 0, 0, 0, false, 0, material, data);
    }

    public static PacketSender create(Type type, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float particleData, Material material, int data){
        return create(type, (float) location.getX(), (float) location.getY(), (float) location.getZ(), count, offsetX, offsetY, offsetZ, longDistance, particleData, material, data);
    }

    public static PacketSender create(Type type, float x, float y, float z, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float particleData, Material material, int data){
        try {
            Class<?> enumParticleClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".EnumParticle");
            Field enumParticleField = enumParticleClass.getDeclaredField(type.toString());
            enumParticleField.setAccessible(true);
            Object enumParticle = enumParticleField.get(null);
            Class<?> particleClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".PacketPlayOutWorldParticles");
            Constructor<?> packetCons = particleClass.getDeclaredConstructor(enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);
            Object packet;
            if(type.equals(Type.ITEM_CRACK)){
                packet = packetCons.newInstance(enumParticle, longDistance,
                        x, y, z, offsetX, offsetY, offsetZ, particleData, count, (Object) new int[] {
                                material.getId(),
                                data
                        });
            }
            else if(type.equals(Type.BLOCK_CRACK)){
                packet = packetCons.newInstance(enumParticle, longDistance,
                        x, y, z, offsetX, offsetY, offsetZ, particleData, count, (Object) new int[] {
                                material.getId()+(data << 12)
                        });
            }
            else if(type.equals(Type.BLOCK_DUST)){
                packet = packetCons.newInstance(enumParticle, longDistance,
                        x, y, z, offsetX, offsetY, offsetZ, particleData, count, (Object) new int[] {
                                material.getId()
                        });
            } else {
                packet = packetCons.newInstance(enumParticle, longDistance,
                        x, y, z, offsetX, offsetY, offsetZ, particleData, count, (Object) new int[] {});
            }
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}