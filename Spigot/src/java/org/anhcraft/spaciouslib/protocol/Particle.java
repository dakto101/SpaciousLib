package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

/**
 * A class helps you to send particle packets
 */
public class Particle {
    public static class Param{
        private Type particle;

        Param(Type particle) {
            this.particle = particle;
        }

        public Type getParticle() {
            return particle;
        }
    }

    public static class DustParam extends Param{
        private Color color;
        private float size;

        public DustParam(Type particle, Color color, float size) {
            super(particle);
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

    public static class ItemParam extends Param {
        private ItemStack item;

        public ItemParam(Type particle, ItemStack item) {
            super(particle);
            this.item = item;
        }

        public ItemParam(Type particle, Material material) {
            super(particle);
            this.item = new ItemStack(material, 1);
        }
    }

    public static class BlockDataParam extends Param{
        private BlockData data;

        public BlockDataParam(Type particle, BlockData data) {
            super(particle);
            this.data = data;
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
        SPELL_MOB("entity_effect", DustParam.class),
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
        REDSTONE("dust", DustParam.class),
        SNOWBALL("item_snowball"),
        @Deprecated
        SNOW_SHOVEL(null),
        SLIME("item_slime"),
        HEART("heart"),
        BARRIER("barrier"),
        ITEM_CRACK("item", ItemParam.class),
        BLOCK_CRACK("block", BlockDataParam.class),
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
        FALLING_DUST("falling_dust", BlockDataParam.class),
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

        public Class<?> getParamClass() {
            return clazz;
        }
    }

    public static PacketSender create(Type type, Location location, int count){
        return create(new Param(type), location, count, 0, 0, 0, false, 0);
    }

    public static PacketSender create(Type type, Location location, Color color){
        return create(new DustParam(type, color, 1), location, 0, 0, 0, 0, false, 1);
    }

    public static PacketSender create(Type type, Location location, Color color, int size){
        return create(new DustParam(type, color, size), location, 0, 0, 0, 0, false, 1);
    }

    public static PacketSender create(Color color, Location location){
        return create(new DustParam(Type.REDSTONE, color, 1), location, 0, 0, 0, 0, false, 1);
    }

    public static PacketSender create(Type type, Location location, ItemStack item){
        return create(new ItemParam(type, item), location, 0, 0, 0, 0, false, 1);
    }

    public static PacketSender create(ItemStack item, Location location){
        return create(new ItemParam(Type.ITEM_CRACK, item), location, 0, 0, 0, 0, false, 1);
    }

    public static PacketSender create(Type type, Location location, int count, Material material,
                                      @Deprecated int data){
        return create(new ItemParam(type, material), location, count, 0, 0, 0, false, 0);
    }

    public static PacketSender create(Type type, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float speed, Material material, @Deprecated int data){
        return create(new ItemParam(type, material), location, count, offsetX, offsetY, offsetZ,
                longDistance, speed);
    }

    public static PacketSender create (Param param, Location location, int count, float offsetX, float offsetY, float offsetZ, boolean longDistance, float speed){
        if(param.particle.getId() == null){
            return null;
        }
        if(param.particle.getParamClass() != null &&
                param.getClass().isAssignableFrom(param.particle.getParamClass())){
            try {
                throw new Exception(param.particle.toString() + " must have a valid particle param!");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String v = GameVersion.getVersion().toString();
            Class<?> packetPlayOutWorldParticlesClass = Class.forName("net.minecraft.server."+v+".PacketPlayOutWorldParticles");
            if(GameVersion.is1_13Above()){
                Class<?> particleClass = Class.forName("net.minecraft.server."+v+".Particle");
                Class<?> particleParamClass = Class.forName("net.minecraft.server."+v+".ParticleParam");
                Class<?> registryMaterialClass = Class.forName("net.minecraft.server."+v+".RegistryMaterials");
                Class<?> minecraftKeyClass = Class.forName("net.minecraft.server."+v+".MinecraftKey");
                Class<?> particleParamRedstoneClass = Class.forName("net.minecraft.server."+v+".ParticleParamRedstone");
                Class<?> particleParamItemClass = Class.forName("net.minecraft.server."+v+".ParticleParamItem");
                Class<?> particleParamBlockClass = Class.forName("net.minecraft.server."+v+".ParticleParamBlock");
                Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + v + ".inventory.CraftItemStack");
                Class<?> craftBlockDataClass = Class.forName("org.bukkit.craftbukkit." + v + ".block.data.CraftBlockData");
                Class<?> IBlockData = Class.forName("net.minecraft.server."+v+".IBlockData");
                Class<?> nmsItemStackClass = Class.forName("net.minecraft.server." + v + ".ItemStack");
                Object minecraftKey = ReflectionUtils.getConstructor(minecraftKeyClass, new Group<>(
                        new Class<?>[]{String.class},
                        new Object[]{param.particle.getId()}
                ));
                Object particleRegistry = ReflectionUtils.getStaticField("REGISTRY", particleClass);
                Object particle = ReflectionUtils.getMethod("get", registryMaterialClass,
                        particleRegistry, new Group<>(
                        new Class<?>[]{minecraftKeyClass},
                        new Object[]{minecraftKey}
                ));
                Object particleParam = null;
                if(param.particle.getParamClass() == null){
                    particleParam = ReflectionUtils.cast(particleParamClass, particle);
                } else if(param instanceof ItemParam){
                    particleParam = ReflectionUtils.getConstructor(particleParamItemClass, new Group<>(
                            new Class<?>[]{particleClass, nmsItemStackClass},
                            new Object[]{particle, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{((ItemParam) param).item}))}
                    ));
                } else if(param instanceof BlockDataParam){
                    Object blockData = ReflectionUtils.getMethod("getState", craftBlockDataClass,ReflectionUtils.cast(craftBlockDataClass, ((BlockDataParam) param).data));
                    particleParam = ReflectionUtils.getConstructor(particleParamBlockClass, new Group<>(
                            new Class<?>[]{particleClass, IBlockData},
                            new Object[]{particle, blockData}
                    ));
                } else if(param instanceof DustParam){
                    DustParam dust = (DustParam) param;
                    particleParam = ReflectionUtils.getConstructor(particleParamRedstoneClass, new Group<>(
                            new Class<?>[]{particleClass, float.class, float.class, float.class, float.class},
                            new Object[]{particle, dust.getColor().getRed(), dust.getColor().getGreen(),
                                    dust.getColor().getBlue(), dust.size}
                    ));
                }
                return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutWorldParticlesClass, new Group<>(
                        new Class<?>[]{particleParamClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class},
                        new Object[]{particleParam, longDistance, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count}
                )));
            } else {
                    Class<?> enumParticleClass = Class.forName("net.minecraft.server."+v+".EnumParticle");
                    Object enumParticle = ReflectionUtils.getEnum(param.particle.toString(), enumParticleClass);
                    int[] i = new int[]{};
                    if(param instanceof ItemParam){
                        i = new int[]{((ItemParam) param).item.getType().getId()};
                    } else if(param instanceof DustParam){
                        DustParam dust = (DustParam) param;
                        offsetX = (float) dust.color.getRed() / 255;
                        offsetY = (float) dust.color.getGreen() / 255;
                        offsetZ = (float) dust.color.getBlue() / 255;
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
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}