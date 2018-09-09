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
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
            Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + v + ".inventory.CraftItemStack");
            Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
            Class<?> itemClass = Class.forName("net.minecraft.server." + v + ".ItemStack");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> entityClass = Class.forName("net.minecraft.server." + v + ".EntityArmorStand");
            Class<?> enumItemSlotClass = Class.forName("net.minecraft.server." + v + ".EnumItemSlot");
            Class<?> vectorClass = Class.forName("net.minecraft.server." + v + ".Vector3f");
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v + ".IChatBaseComponent");
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
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(headPose.getX()), a(headPose.getY()), a(headPose.getZ())}))}));
            ReflectionUtils.getMethod("setBodyPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(bodyPose.getX()), a(bodyPose.getY()), a(bodyPose.getZ())}))}));
            ReflectionUtils.getMethod("setLeftArmPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(leftArmPose.getX()), a(leftArmPose.getY()), a(leftArmPose.getZ())}))}));
            ReflectionUtils.getMethod("setRightArmPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(rightArmPose.getX()), a(rightArmPose.getY()), a(rightArmPose.getZ())}))}));
            ReflectionUtils.getMethod("setLeftLegPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(leftLegPose.getX()), a(leftLegPose.getY()), a(leftLegPose.getZ())}))}));
            ReflectionUtils.getMethod("setRightLegPose", entityClass, entity, new Group<>(
                    new Class<?>[]{vectorClass}, new Object[]{ReflectionUtils.getConstructor(vectorClass,
                    new Group<>(new Class<?>[]{float.class, float.class, float.class}, new Object[]{
                            a(rightLegPose.getX()), a(rightLegPose.getY()), a(rightLegPose.getZ())}))}));
            if(GameVersion.is1_9Above()) {
                ReflectionUtils.getMethod("setMarker", entityClass, entity,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{marker})
                );
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("MAINHAND", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("OFFHAND", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{offHand}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("HEAD", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("CHEST", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("LEGS", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))}));
                ReflectionUtils.getMethod("setSlot", entityClass, entity,
                        new Group<>(new Class<?>[]{enumItemSlotClass, itemClass}, new Object[]{ReflectionUtils.getEnum("FEET", enumItemSlotClass), ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))}));
            } else {
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class, itemClass}, new Object[]{0, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{mainHand}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class, itemClass}, new Object[]{1, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{helmet}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class, itemClass}, new Object[]{2, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{chestplate}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class, itemClass}, new Object[]{3, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{leggings}))})
                );
                ReflectionUtils.getMethod("setEquipment", entityClass, entity,
                        new Group<>(new Class<?>[]{int.class, itemClass}, new Object[]{4, ReflectionUtils.getStaticMethod("asNMSCopy", craftItemStackClass, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{boots}))})
                );
            }
            ReflectionUtils.getMethod("setBasePlate", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{basePlate})
            );
            ReflectionUtils.getMethod("setInvisible", entityClass, entity,
                    new Group<>(new Class<?>[]{boolean.class}, new Object[]{!visible})
            );
            if(GameVersion.is1_13Above()){
                if(!CommonUtils.isValidJSON(customName)){
                    customName = "{\"text\": \"" + customName + "\"}";
                }
                Object customNameComponent = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                        new Group<>(
                                new Class<?>[]{String.class},
                                new String[]{customName}
                        ));
                ReflectionUtils.getMethod("setCustomName", nmsEntityClass, entity,
                        new Group<>(new Class<?>[]{chatBaseComponentClass}, new Object[]{customNameComponent})
                );
            } else {
                ReflectionUtils.getMethod("setCustomName", nmsEntityClass, entity,
                        new Group<>(new Class<?>[]{String.class}, new Object[]{customName})
                );
            }
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
            packets.add(LivingEntitySpawn.create(entity));
            packets.add(EntityEquipment.create(id, EquipSlot.MAINHAND, mainHand));
            packets.add(EntityEquipment.create(id, EquipSlot.OFFHAND, mainHand));
            packets.add(EntityEquipment.create(id, EquipSlot.HEAD, helmet));
            packets.add(EntityEquipment.create(id, EquipSlot.CHEST, chestplate));
            packets.add(EntityEquipment.create(id, EquipSlot.LEGS, leggings));
            packets.add(EntityEquipment.create(id, EquipSlot.FEET, boots));
        } catch(ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        createPacketSender();
        return this;
    }
}
