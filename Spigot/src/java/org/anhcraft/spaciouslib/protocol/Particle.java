package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

/**
 * A class helps you to send particle packets
 */
public class Particle {
    public static class Dust {
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
        EXPLOSION_NORMAL("poof"),
        EXPLOSION_LARGE("explosion"),
        EXPLOSION_HUGE("explosion_emitter"),
        FIREWORKS_SPARK("firework"),
        WATER_BUBBLE("bubble"),
        WATER_SPLASH("splash"),
        WATER_WAKE("fishing"),
        SUSPENDED("underwater"),
        @Deprecated
        SUSPENDED_DEPTH(null),
        CRIT("crit"),
        CRIT_MAGIC("enchanted_hit"),
        SMOKE_NORMAL("smoke"),
        SMOKE_LARGE("large_smoke"),
        SPELL("effect"),
        SPELL_INSTANT("instant_effect"),
        SPELL_MOB("entity_effect", Dust.class),
        SPELL_MOB_AMBIENT("ambient_entity_effect"),
        SPELL_WITCH("witch"),
        DRIP_WATER("dripping_water"),
        DRIP_LAVA("dripping_lava"),
        VILLAGER_ANGRY("angry_villager"),
        VILLAGER_HAPPY("happy_villager"),
        TOWN_AURA("mycelium"),
        NOTE("note"),
        PORTAL("portal"),
        ENCHANTMENT_TABLE("enchant"),
        FLAME("flame"),
        LAVA("lava"),
        @Deprecated
        FOOTSTEP(null),
        CLOUD("cloud"),
        REDSTONE("dust", Dust.class),
        SNOWBALL("item_snowball"),
        @Deprecated
        SNOW_SHOVEL(null),
        SLIME("item_slime"),
        HEART("heart"),
        BARRIER("barrier"),
        ITEM_CRACK("item", ItemStack.class),
        BLOCK_CRACK("block", Material.class),
        @Deprecated
        BLOCK_DUST(null),
        WATER_DROP("rain"),
        @Deprecated
        ITEM_TAKE(null),
        MOB_APPEARANCE("elder_guardian"),
        DRAGON_BREATH("dragon_breath"),
        END_ROD("end_rod"),
        DAMAGE_INDICATOR("damage_indicator"),
        SWEEP_ATTACK("sweep_attack"),
        FALLING_DUST("falling_dust", Material.class),
        TOTEM("totem_of_undying"),
        SPIT("spit"),
        SQUID_INK("squid_ink"),
        BUBBLE_POP("bubble_pop"),
        CURRENT_DOWN("current_down"),
        BUBBLE_COLUMN_UP("bubble_column_up"),
        NAUTILUS("nautilus"),
        DOLPHIN("dolphin");

        private String id;
        private Class<?> clazz;

        Type(String id){
            this.id = id;
        }

        Type(String id, Class<?> clazz){
            this.id = id;
            this.clazz = clazz;
        }

        public String getId(){
            return id;
        }

        public Class<?> getDataClass() {
            return clazz;
        }
    }

    public static PacketSender create(Type type, Location location, int count){
        return create(type, location, count, 0, 0, 0, false, 0, Material.AIR, 0);
    }

    public static PacketSender create(Type type, Location location, Color color){
        return create(type, location, 0, 0, 0, 0, false, 1, new Dust(color, 1));
    }

    public static PacketSender create(Color color, Location location){
        return create(Type.REDSTONE, location, 0, 0, 0, 0, false, 1, new Dust(color, 1));
    }

    public static PacketSender create(Type type, Location location, ItemStack item){
        return create(type, location, 0, 0, 0, 0, false, 1, item);
    }

    public static PacketSender create(ItemStack item, Location location){
        return create(Type.ITEM_CRACK, location, 0, 0, 0, 0, false, 1, item);
    }

    public static PacketSender create(Type type, Location location, int count, int speed){
        return create(type, location, count, 0, 0, 0, false, speed, null);
    }

    public static PacketSender create(Type type, Location location, int count, Material material,
                                      @Deprecated int data){
        return create(type, location, count, 0, 0, 0, false, 0, material);
    }

    public static PacketSender create(Type type, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float speed, Material material, @Deprecated int data){
        return create(type, location, count, offsetX, offsetY, offsetZ, longDistance, speed, material);
    }

    public static PacketSender create (Type type, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float speed, Object data){
        if(type.getId() == null){
            return null;
        }
        if(type.getDataClass() != null && data.getClass().isAssignableFrom(type.getDataClass())){
            return null;
        }
        try {
            Class<?> packetPlayOutWorldParticlesClass = Class.forName("net.minecraft.server."+v+".PacketPlayOutWorldParticles");
            String v = GameVersion.getVersion().toString();
            if(GameVersion.is1_13Above()){
                Class<?> particleClass = Class.forName("net.minecraft.server."+v+".Particle");
                Class<?> particleParamClass = Class.forName("net.minecraft.server."+v+".ParticleParam");
                Class<?> registryMaterialClass = Class.forName("net.minecraft.server."+v+".RegistryMaterials");
                Class<?> minecraftKeyClass = Class.forName("net.minecraft.server."+v+".MinecraftKey");
                Class<?> particleParamRedstoneClass = Class.forName("net.minecraft.server."+v+".ParticleParamRedstone");
                Object minecraftKey = ReflectionUtils.getConstructor(minecraftKeyClass, new Group<>(
                        new Class<?>[]{String.class},
                        new Object[]{type.getId()}
                ));
                Object particleRegistry = ReflectionUtils.getStaticField("REGISTRY", particleClass);
                Object particle = ReflectionUtils.getMethod("get", registryMaterialClass,
                        particleRegistry, new Group<>(
                        new Class<?>[]{minecraftKeyClass},
                        new Object[]{minecraftKey}
                ));
                Object particleParam = null;
                if(type.getDataClass() == null){
                    particleParam = ReflectionUtils.cast(particleParamClass, particle);
                } else if(type.getDataClass().equals(ItemStack.class)){
                    particleParam = ;
                } else if(type.getDataClass().equals(Material.class)){
                    particleParam = ;
                } else if(type.getDataClass().equals(Dust.class)){
                    Dust dust = (Dust) data;
                    particleParam = ReflectionUtils.getConstructor(particleParamRedstoneClass, new Group<>(
                            new Class<?>[]{float.class, float.class, float.class, float.size},
                            new Object[]{dust.getColor().getRed(), dust.getColor().getGreen(), dust.getColor().getBlue(), dust.size}
                    ));
                }
                return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutWorldParticlesClass, new Group<>(
                        new Class<?>[]{particleParamClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class},
                        new Object[]{particleParam, longDistance, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count}
                )));
            } else {
                    Class<?> enumParticleClass = Class.forName("net.minecraft.server."+v+".EnumParticle");
                    Object enumParticle = ReflectionUtils.getEnum(type.toString(), enumParticleClass);
                    int[] i = new int[]{};
                    if(type.getDataClass().equals(Material.class)){
                        i = new int[]{((Material) data).getId()};
                    } else if(type.getDataClass().equals(ItemStack.class)){
                        i = new int[]{((ItemStack) data).getType().getId()};
                    } else if(type.getDataClass().equals(Dust.class)){
                        Color color = ((Dust) data).color;
                        offsetX = (float) color.getRed() / 255;
                        offsetY = (float) color.getGreen() / 255;
                        offsetZ = (float) color.getBlue() / 255;
                        if (offsetX < 0) {
                            offsetX = 0;
                        }
                        if (offsetY < 0) {
                            offsetY = 0;
                        }
                        if (offsetZ < 0) {
                            offsetZ = 0;
                        }
                    }
                    return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutWorldParticlesClass, new Group<>(
                            new Class<?>[]{enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class},
                            new Object[]{enumParticle, longDistance,
                                    (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count, i}
                    )));
                return null;
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}