package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.EntityEquipment;
import org.anhcraft.spaciouslib.protocol.EntityTeleport;
import org.anhcraft.spaciouslib.protocol.LivingEntitySpawn;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;

public class ArmorStand {
    private Object entity;
    private int id = -1;
    private Location location;
    @PlayerCleaner
    private Set<UUID> viewers = new HashSet<>();
    private String customName = "";
    private ItemStack mainHand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private EulerAngle headPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private EulerAngle bodyPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private EulerAngle leftArmPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private EulerAngle rightArmPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private EulerAngle leftLegPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private EulerAngle rightLegPose = new EulerAngle(0.0F, 0.0F, 0.0F);
    private boolean marker = false;
    private boolean small = false;
    private boolean arms = false;
    private boolean visible = true;
    private boolean gravity = false;
    private boolean basePlate = false;
    private boolean showCustomName = false;

    /**
     * Creates a new ArmorStand instance
     * @param location the armor stand location
     */
    public ArmorStand(Location location){
        this.location = location;
        init();
    }

    private void init() {
        AnnotationHandler.register(ArmorStand.class, this);
    }

    /**
     * Adds the given viewer
     * @param player the unique id of the viewer
     * @return this object
     */
    public ArmorStand addViewer(UUID player){
        this.viewers.add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the unique id of viewer
     * @return this object
     */
    public ArmorStand removeViewer(UUID player){
        this.viewers.remove(player);
        return this;
    }

    /**
     * Gets all viewers
     * @return a list contains unique ids of viewers
     */
    public Set<UUID> getViewers(){
        return this.viewers;
    }

    /**
     * Gets the location of this armor stand
     * @return the Location object
     */
    public Location getLocation(){
        return this.location;
    }
    
    /**
     * Sets the viewers who you want to show this armor stand to
     * @param viewers a list contains unique ids of viewers
     * @return this object
     */
    public ArmorStand setViewers(Set<UUID> viewers) {
        this.viewers = viewers;
        return this;
    }

    /**
     * Teleports this armor stand to a new location
     * @return this object
     */
    public ArmorStand teleport(Location location){
        this.location = location;
        String v = GameVersion.getVersion().toString();
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        try {
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            ReflectionUtils.getMethod("setLocation", nmsEntityClass, entity, new Group<>(
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
            EntityTeleport.create(entity).sendPlayers(receivers);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Spawns this armor stand.<br>
     * If you just want to teleport this armor stand, please use the method "teleport" instead
     * @return this object
     */
    public ArmorStand spawn(){
        String v = GameVersion.getVersion().toString();
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        try {
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
            Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + v + ".inventory.CraftItemStack");
            Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
            Class<?> itemClass = Class.forName("net.minecraft.server." + v + ".ItemStack");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> entityClass = Class.forName("net.minecraft.server." + v + ".EntityArmorStand");
            Class<?> enumItemSlotClass = Class.forName("net.minecraft.server." + v + ".EnumItemSlot");
            Class<?> vectorClass = Class.forName("net.minecraft.server." + v + ".Vector3f");
            Object craftWorld = ReflectionUtils.cast(craftWorldClass, location.getWorld());
            Object nmsWorld = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
            entity = ReflectionUtils.getConstructor(entityClass, new Group<>(
                    new Class<?>[]{nmsWorldClass}, new Object[]{nmsWorld}
            ));
            ReflectionUtils.getMethod("setLocation", nmsEntityClass, entity, new Group<>(
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
            ReflectionUtils.getMethod("setHeadPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(headPose.getX()), a(headPose.getY()), a(headPose.getZ())}))}));
            ReflectionUtils.getMethod("setBodyPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(bodyPose.getX()), a(bodyPose.getY()), a(bodyPose.getZ())}))}));
            ReflectionUtils.getMethod("setLeftArmPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(leftArmPose.getX()), a(leftArmPose.getY()), a(leftArmPose.getZ())}))}));
            ReflectionUtils.getMethod("setRightArmPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(rightArmPose.getX()), a(rightArmPose.getY()), a(rightArmPose.getZ())}))}));
            ReflectionUtils.getMethod("setLeftLegPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(leftLegPose.getX()), a(leftLegPose.getY()), a(leftLegPose.getZ())}))}));
            ReflectionUtils.getMethod("setRightLegPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class,float.class,float.class}, new Object[]{
                            a(rightLegPose.getX()), a(rightLegPose.getY()), a(rightLegPose.getZ())}))}));
            if(GameVersion.is1_9Above()) {
                ReflectionUtils.getMethod("setMarker", entityClass, entity,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{marker})
                );
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("MAINHAND", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("OFFHAND", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{offHand}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("HEAD", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("CHEST", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("LEGS", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass,itemClass}, new Object[]{ReflectionUtils.getEnum("FEET", enumItemSlotClass),ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))}));
            } else {
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class,itemClass}, new Object[]{0,ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class,itemClass}, new Object[]{1,ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class,itemClass}, new Object[]{2,ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class,itemClass}, new Object[]{3,ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class,itemClass}, new Object[]{4,ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))})
                );
            }
            ReflectionUtils.getMethod("setBasePlate", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{basePlate})
            );
            ReflectionUtils.getMethod("setInvisible", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{!visible})
            );
            ReflectionUtils.getMethod("setCustomName", nmsEntityClass, entity,
                    new Group<>(new Class<?>[]{String.class}, new Object[]{customName})
            );
            ReflectionUtils.getMethod("setCustomNameVisible", nmsEntityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{showCustomName})
            );
            ReflectionUtils.getMethod("setSmall", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{small})
            );
            if(GameVersion.is1_10Above()) {
                ReflectionUtils.getMethod("setNoGravity", nmsEntityClass, entity,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{!gravity})
                );
            } else {
                ReflectionUtils.getMethod("setGravity", entityClass, entity,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{gravity})
                );
            }
            ReflectionUtils.getMethod("setArms", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{arms})
            );
            id = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, entity);
            LivingEntitySpawn.create(entity).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.MAINHAND, mainHand).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.OFFHAND, mainHand).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.HEAD, helmet).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.CHEST, chestplate).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.LEGS, leggings).sendPlayers(receivers);
            EntityEquipment.create(id, EquipSlot.FEET, boots).sendPlayers(receivers);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    private static float a(double x) {
        return (float) Math.toDegrees(x);
    }

    /**
     * Removes this armor stand
     */
    public void remove(){
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        EntityDestroy.create(id).sendPlayers(receivers);
        entity = null;
        id = -1;
        AnnotationHandler.unregister(ArmorStand.class, this);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            ArmorStand h = (ArmorStand) o;
            return new EqualsBuilder()
                    .append(h.location, this.location)
                    .append(h.viewers, this.viewers)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(17, 45).append(this.viewers).append(this.location).toHashCode();
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isArms() {
        return arms;
    }

    public void setArms(boolean arms) {
        this.arms = arms;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public EulerAngle getHeadPose() {
        return headPose;
    }

    public void setHeadPose(EulerAngle headPose) {
        this.headPose = headPose;
    }

    public EulerAngle getLeftLegPose() {
        return leftLegPose;
    }

    public void setLeftLegPose(EulerAngle leftLegPose) {
        this.leftLegPose = leftLegPose;
    }

    public EulerAngle getRightLegPose() {
        return rightLegPose;
    }

    public void setRightLegPose(EulerAngle rightLegPose) {
        this.rightLegPose = rightLegPose;
    }

    public EulerAngle getLeftArmPose() {
        return leftArmPose;
    }

    public void setLeftArmPose(EulerAngle leftArmPose) {
        this.leftArmPose = leftArmPose;
    }

    public EulerAngle getRightArmPose() {
        return rightArmPose;
    }

    public void setRightArmPose(EulerAngle rightArmPose) {
        this.rightArmPose = rightArmPose;
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public boolean isBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isShowCustomName() {
        return showCustomName;
    }

    public void setShowCustomName(boolean showCustomName) {
        this.showCustomName = showCustomName;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
    }

    public EulerAngle getBodyPose() {
        return bodyPose;
    }

    public void setBodyPose(EulerAngle bodyPose) {
        this.bodyPose = bodyPose;
    }
}
