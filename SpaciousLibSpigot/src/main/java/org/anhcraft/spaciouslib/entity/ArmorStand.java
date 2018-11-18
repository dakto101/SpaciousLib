package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.stream.Collectors;

public class ArmorStand extends PacketBuilder<ArmorStand> {
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
     * @param location the location of the armor stand
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
        Validate.notNull(packetSender, "You must use the method #buildPackets first!");
        this.viewers.add(player);
        add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the unique id of viewer
     * @return this object
     */
    public ArmorStand removeViewer(UUID player){
        remove(player);
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
        Validate.notNull(packetSender, "You must use the method #buildPackets first!");
        // sends armor stands to new viewers
        Set<UUID> add = new HashSet<>(viewers); // clones
        add.removeAll(this.viewers); // removes all existed viewers
        for(UUID player : add){
            add(player);
        }
        // removes the armor stands of old viewers which aren't existed in the new list
        Set<UUID> remove = new HashSet<>(this.viewers); // clones
        remove.removeAll(viewers); // removes all non-existed viewers
        for(UUID player : remove){
            remove(player);
        }
        // ... of course, any viewers that aren't affected won't have any updates for their armor stands
        this.viewers = viewers;
        return this;
    }

    /**
     * Teleports this armor stand to a new location
     * @return this object
     */
    public ArmorStand teleport(Location location){
        this.location = location;
        ReflectionUtils.getMethod("setLocation", ClassFinder.NMS.Entity, entity, new Group<>(
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
        EntityTeleport.create(entity).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
        return this;
    }

    private ArmorStand add(UUID uuid){
        Player player = Bukkit.getServer().getPlayer(uuid);
        packetSender.sendPlayer(player);
        return this;
    }

    private static float a(double x) {
        return (float) Math.toDegrees(x);
    }

    /**
     * Removes this armor stand.<br>
     * Once you call this method, this instance can no longer be used
     */
    public void remove(){
        for(Iterator<UUID> it = getViewers().iterator(); it.hasNext(); ) {
            UUID p = it.next();
            remove(p);
            it.remove();
        }
        entity = null;
        id = -1;
        AnnotationHandler.unregister(ArmorStand.class, this);
    }

    private void remove(UUID p) {
        EntityDestroy.create(id).sendPlayer(Bukkit.getServer().getPlayer(p));
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

    public ArmorStand setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ArmorStand setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ArmorStand setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ArmorStand setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public boolean isMarker() {
        return marker;
    }

    public ArmorStand setMarker(boolean marker) {
        this.marker = marker;
        return this;
    }

    public boolean isSmall() {
        return small;
    }

    public ArmorStand setSmall(boolean small) {
        this.small = small;
        return this;
    }

    public boolean isArms() {
        return arms;
    }

    public ArmorStand setArms(boolean arms) {
        this.arms = arms;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public ArmorStand setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public EulerAngle getHeadPose() {
        return headPose;
    }

    public ArmorStand setHeadPose(EulerAngle headPose) {
        this.headPose = headPose;
        return this;
    }

    public EulerAngle getLeftLegPose() {
        return leftLegPose;
    }

    public ArmorStand setLeftLegPose(EulerAngle leftLegPose) {
        this.leftLegPose = leftLegPose;
        return this;
    }

    public EulerAngle getRightLegPose() {
        return rightLegPose;
    }

    public ArmorStand setRightLegPose(EulerAngle rightLegPose) {
        this.rightLegPose = rightLegPose;
        return this;
    }

    public EulerAngle getLeftArmPose() {
        return leftArmPose;
    }

    public ArmorStand setLeftArmPose(EulerAngle leftArmPose) {
        this.leftArmPose = leftArmPose;
        return this;
    }

    public EulerAngle getRightArmPose() {
        return rightArmPose;
    }

    public ArmorStand setRightArmPose(EulerAngle rightArmPose) {
        this.rightArmPose = rightArmPose;
        return this;
    }

    public boolean isGravity() {
        return gravity;
    }

    public ArmorStand setGravity(boolean gravity) {
        this.gravity = gravity;
        return this;
    }

    public boolean isBasePlate() {
        return basePlate;
    }

    public ArmorStand setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
        return this;
    }

    public String getCustomName() {
        return customName;
    }

    public ArmorStand setCustomName(String customName) {
        this.customName = Chat.color(customName);
        return this;
    }

    public boolean isShowCustomName() {
        return showCustomName;
    }

    public ArmorStand setShowCustomName(boolean showCustomName) {
        this.showCustomName = showCustomName;
        return this;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public ArmorStand setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
        return this;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public ArmorStand setOffHand(ItemStack offHand) {
        this.offHand = offHand;
        return this;
    }

    public EulerAngle getBodyPose() {
        return bodyPose;
    }

    public ArmorStand setBodyPose(EulerAngle bodyPose) {
        this.bodyPose = bodyPose;
        return this;
    }

    @Override
    public ArmorStand buildPackets() {
        Object craftWorld = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, location.getWorld());
        Object nmsWorld = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorld);
        entity = ReflectionUtils.getConstructor(ClassFinder.NMS.EntityArmorStand, new Group<>(
                new Class<?>[]{ClassFinder.NMS.World}, new Object[]{nmsWorld}
        ));
        ReflectionUtils.getMethod("setLocation", ClassFinder.NMS.Entity, entity, new Group<>(
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
        ReflectionUtils.getMethod("setHeadPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(headPose.getX()), a(headPose.getY()), a(headPose.getZ())}))}));
        ReflectionUtils.getMethod("setBodyPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(bodyPose.getX()), a(bodyPose.getY()), a(bodyPose.getZ())}))}));
        ReflectionUtils.getMethod("setLeftArmPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(leftArmPose.getX()), a(leftArmPose.getY()), a(leftArmPose.getZ())}))}));
        ReflectionUtils.getMethod("setRightArmPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(rightArmPose.getX()), a(rightArmPose.getY()), a(rightArmPose.getZ())}))}));
        ReflectionUtils.getMethod("setLeftLegPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(leftLegPose.getX()), a(leftLegPose.getY()), a(leftLegPose.getZ())}))}));
        ReflectionUtils.getMethod("setRightLegPose", ClassFinder.NMS.EntityArmorStand, entity, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Vector3f}, new Object[]{ReflectionUtils.getConstructor(ClassFinder.NMS.Vector3f,
                new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                        a(rightLegPose.getX()), a(rightLegPose.getY()), a(rightLegPose.getZ())}))}));
        if(GameVersion.is1_9Above()) {
            ReflectionUtils.getMethod("setMarker", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{marker})
            );
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("MAINHAND", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))}));
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("OFFHAND", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{offHand}))}));
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("HEAD", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))}));
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("CHEST", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))}));
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("LEGS", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))}));
            ReflectionUtils.getMethod("setSlot", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack}, new Object[]{ReflectionUtils.getEnum("FEET", ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))}));
        } else {
            ReflectionUtils.getMethod("setEquipment", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{int.class, ClassFinder.NMS.ItemStack}, new Object[]{0, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))})
            );
            ReflectionUtils.getMethod("setEquipment", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{int.class, ClassFinder.NMS.ItemStack}, new Object[]{1, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))})
            );
            ReflectionUtils.getMethod("setEquipment", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{int.class, ClassFinder.NMS.ItemStack}, new Object[]{2, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))})
            );
            ReflectionUtils.getMethod("setEquipment", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{int.class, ClassFinder.NMS.ItemStack}, new Object[]{3, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))})
            );
            ReflectionUtils.getMethod("setEquipment", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{int.class, ClassFinder.NMS.ItemStack}, new Object[]{4, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))})
            );
        }
        ReflectionUtils.getMethod("setBasePlate", ClassFinder.NMS.EntityArmorStand, entity,
                new Group<>(new Class<?>[]{boolean.class}, new Object[]{basePlate})
        );
        ReflectionUtils.getMethod("setInvisible", ClassFinder.NMS.EntityArmorStand, entity,
                new Group<>(new Class<?>[]{boolean.class}, new Object[]{!visible})
        );
        if(GameVersion.is1_13Above()){
            if(!CommonUtils.isValidJSON(customName)){
                customName = "{\"text\": \"" + customName + "\"}";
            }
            Object customNameComponent = ReflectionUtils.getStaticMethod("a", ClassFinder.NMS.ChatSerializer,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{customName}
                    ));
            ReflectionUtils.getMethod("setCustomName", ClassFinder.NMS.Entity, entity,
                    new Group<>(new Class<?>[]{ClassFinder.NMS.IChatBaseComponent}, new Object[]{customNameComponent})
            );
        } else {
            ReflectionUtils.getMethod("setCustomName", ClassFinder.NMS.Entity, entity,
                    new Group<>(new Class<?>[]{String.class}, new Object[]{customName})
            );
        }
        ReflectionUtils.getMethod("setCustomNameVisible", ClassFinder.NMS.Entity, entity,
                new Group<>(new Class<?>[]{boolean.class}, new Object[]{showCustomName})
        );
        ReflectionUtils.getMethod("setSmall", ClassFinder.NMS.EntityArmorStand, entity,
                new Group<>(new Class<?>[]{boolean.class}, new Object[]{small})
        );
        if(GameVersion.is1_10Above()) {
            ReflectionUtils.getMethod("setNoGravity", ClassFinder.NMS.Entity, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{!gravity})
            );
        } else {
            ReflectionUtils.getMethod("setGravity", ClassFinder.NMS.EntityArmorStand, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{gravity})
            );
        }
        ReflectionUtils.getMethod("setArms", ClassFinder.NMS.EntityArmorStand, entity,
                new Group<>(new Class<?>[]{boolean.class}, new Object[]{arms})
        );
        id = (int) ReflectionUtils.getMethod("getId", ClassFinder.NMS.Entity, entity);
        packets.add(LivingEntitySpawn.create(entity));
        packets.add(EntityEquipment.create(id, EquipSlot.MAINHAND, mainHand));
        packets.add(EntityEquipment.create(id, EquipSlot.OFFHAND, mainHand));
        packets.add(EntityEquipment.create(id, EquipSlot.HEAD, helmet));
        packets.add(EntityEquipment.create(id, EquipSlot.CHEST, chestplate));
        packets.add(EntityEquipment.create(id, EquipSlot.LEGS, leggings));
        packets.add(EntityEquipment.create(id, EquipSlot.FEET, boots));
        createPacketSender();
        return this;
    }
}
