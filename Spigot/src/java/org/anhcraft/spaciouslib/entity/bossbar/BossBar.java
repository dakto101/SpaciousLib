package org.anhcraft.spaciouslib.entity.bossbar;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Represents a boss bar implementation.
 */
public class BossBar {
    public enum Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE
    }

    public enum Style {
        PROGRESS,
        NOTCHED_6,
        NOTCHED_10,
        NOTCHED_12,
        NOTCHED_20
    }

    public enum Flag {
        /**
         * Creates fog effect around the viewers.
         */
        CREATE_FOG,
        /**
         * Darkens the sky.
         */
        DARKEN_SKY,
        /**
         * Plays the Ender Dragon boss music for the viewers.
         */
        PLAY_BOSS_MUSIC
    }

    private String title;
    private float health = 0;
    private Color color = Color.BLUE;
    private Style style = Style.PROGRESS;
    private HashSet<Flag> flags = new HashSet<>();
    @PlayerCleaner
    private Set<UUID> viewers = new HashSet<>();

    // 1.8
    private BukkitTask task;
    @PlayerCleaner
    private LinkedHashMap<UUID, String> locationTracker = new LinkedHashMap<>();
    @PlayerCleaner
    private LinkedHashMap<UUID, Group<Object, Integer>> entities = new LinkedHashMap<>();

    // 1.9 and above
    @PlayerCleaner
    private LinkedHashMap<UUID, Object> bossBattles = new LinkedHashMap<>();

    /**
     * Removes this boss bar
     */
    public void remove(){
        for(Iterator<UUID> it = getViewers().iterator(); it.hasNext(); ) {
            UUID p = it.next();
            remove(p);
            it.remove();
        }
        if(task != null){
            task.cancel();
        }
        locationTracker = new LinkedHashMap<>();
        entities = new LinkedHashMap<>();
        bossBattles = new LinkedHashMap<>();
        viewers = new HashSet<>();
        AnnotationHandler.unregister(BossBar.class, this);
    }

    private void init() {
        AnnotationHandler.register(BossBar.class, this);
        if(GameVersion.is1_9Above()){
            return;
        }
        //// ONLY FOR 1.8 ////
        task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String v = GameVersion.getVersion().toString();
                    Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                    for(UUID uuid : viewers){
                        Player player = Bukkit.getServer().getPlayer(uuid);
                        if(!locationTracker.containsKey(uuid)){
                            locationTracker.put(uuid, LocationUtils.loc2str(player.getLocation()));
                            return;
                        }
                        if(entities.containsKey(uuid)) {
                            Location old = LocationUtils.str2loc(locationTracker.get(uuid));
                            // if a player has teleported to a new world
                            // we will remove the bar of that player and add it again
                            // the dragon will teleport to the new world
                            if(!old.getWorld().getName().equals(player.getWorld().getName())){
                                remove(uuid);
                                add(uuid);
                                return;
                            }
                            // if the player just move around the world, we will send a teleport packet
                            // to tell the client to update the location of the dragon
                            locationTracker.put(uuid, LocationUtils.loc2str(player.getLocation()));
                            Location location = player.getLocation().getDirection().multiply(64).subtract(player.getLocation().toVector()).toLocation(player.getWorld());
                            Object nmsEntityDragon = entities.get(uuid).getA();
                            ReflectionUtils.getMethod("setLocation", nmsEntityClass, nmsEntityDragon, new Group<>(
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
                                    })
                            );
                            entities.put(uuid, entities.get(uuid).setA(nmsEntityDragon));
                            EntityTeleport.create(nmsEntityDragon).sendPlayer(player);
                        }
                    }
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, 30);
    }

    /**
     * Creates a new boss bar instance
     * @param title the title of the boss bar
     */
    public BossBar(String title){
        this.title = title;
        init();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar (1.9+)
     */
    public BossBar(String title, Color color){
        this.title = title;
        this.color = color;
        init();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar (1.9+)
     * @param style the style for the boss bar (1.9+)
     */
    public BossBar(String title, Color color, Style style){
        this.title = title;
        this.color = color;
        this.style = style;
        init();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar (1.9+)
     * @param style the style for the boss bar (1.9+)
     * @param health the health amount for the boss bar, must be from 0 to 1
     */
    public BossBar(String title, Color color, Style style, float health){
        this.title = title;
        this.color = color;
        this.style = style;
        this.health = health;
        init();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar (1.9+)
     * @param style the style for the boss bar (1.9+)
     * @param health the health amount for the boss bar, must be from 0 to 1
     * @param flags the array of flags for the boss bar (1.9+)
     */
    public BossBar(String title, Color color, Style style, float health, Flag... flags){
        this.title = title;
        this.color = color;
        this.style = style;
        this.health = health;
        this.flags = new HashSet<>(CommonUtils.toList(flags));
        init();
    }

    /**
     * Adds the given viewer
     * @param player the unique id of the viewer
     * @return this object
     */
    public BossBar addViewer(UUID player){
        if(!this.viewers.contains(player)) {
            this.viewers.add(player);
            add(player);
        }
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the unique id of the viewer
     * @return this object
     */
    public BossBar removeViewer(UUID player){
        if(this.viewers.contains(player)) {
            this.viewers.remove(player);
            remove(player);
        }
        return this;
    }

    /**
     * Gets all viewers
     * @return a list contains unique ids of the viewers
     */
    public Set<UUID> getViewers(){
        return this.viewers;
    }

    /**
     * Gets the color of this boss bar
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets the health value of this boss bar
     * @return the health value
     */
    public float getHealth() {
        return this.health;
    }

    /**
     * Gets the style type of this boss bar
     * @return the style
     */
    public Style getStyle() {
        return this.style;
    }

    /**
     * Gets all flags of this boss bar
     * @return an collection of flags with no duplicates
     */
    public HashSet<Flag> getFlags() {
        return this.flags;
    }

    /**
     * Gets the title of this boss bar
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the viewers who you want to show this boss bar to
     * @param viewers a list contains unique ids of the viewers
     * @return this object
     */
    public BossBar setViewers(Set<UUID> viewers) {
        // adds the boss bars for new viewers
        Set<UUID> add = new HashSet<>(viewers); // clones
        add.removeAll(this.viewers); // removes all existed viewers
        for(UUID player : add){
            add(player);
        }
        // removes the boss bars of old viewers which aren't existed in the new list
        Set<UUID> remove = new HashSet<>(this.viewers); // clones
        remove.removeAll(viewers); // removes all non-existed viewers
        for(UUID player : remove){
            remove(player);
        }
        // ... of course, any viewers that aren't affected won't have any updates for their boss bars
        this.viewers = viewers;
        return this;
    }

    /**
     * Sets the color for this boss bar (1.9+)
     * @param color the color
     * @return this object
     */
    public BossBar setColor(Color color) {
        this.color = color;
        // the color feature only support for 1.9 versions or above
        if(GameVersion.is1_9Above()){
            for(UUID viewer : this.viewers){
                removeViewer(viewer);
                addViewer(viewer);
            }
        }
        return this;
    }

    /**
     * Sets the title for this boss bar
     * @param title the title
     * @return this object
     */
    public BossBar setTitle(String title) {
        this.title = title;
        for(UUID viewer : this.viewers) {
            if(GameVersion.is1_9Above()) {
                removeViewer(viewer);
                addViewer(viewer);
            } else {
                try {
                    String v = GameVersion.getVersion().toString();
                    Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                    Group<Object, Integer> dragonData = this.entities.get(viewer);
                    Object nmsEntityDragon = dragonData.getA();
                    ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsEntityDragon,
                            new Group<>(new Class<?>[]{String.class}, new Object[]{this.title})
                    );
                    dragonData.setA(nmsEntityDragon);
                    this.entities.put(viewer, dragonData);
                    EntityMetadata.create(nmsEntityDragon).sendPlayer(Bukkit.getServer().getPlayer(viewer));
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * Sets the flag for this boss bar (1.9+)
     * @param flags an array of flags
     * @return this object
     */
    public BossBar setFlags(Flag... flags) {
        this.flags = new HashSet<>(CommonUtils.toList(flags));
        // the flags feature only support for 1.9 versions or above
        if(GameVersion.is1_9Above()){
            for(UUID viewer : this.viewers){
                removeViewer(viewer);
                addViewer(viewer);
            }
        }
        return this;
    }

    /**
     * Sets the health value for this boss bar.<br>
     * Must be from 0 to 1
     * @param health the health value
     * @return this object
     */
    public BossBar setHealth(float health) {
        this.health = health;
        for(UUID viewer : this.viewers) {
            if(GameVersion.is1_9Above()) {
                removeViewer(viewer);
                addViewer(viewer);
            } else {
                try {
                    String v = GameVersion.getVersion().toString();
                    Class<?> nmsEntityLivingClass = Class.forName("net.minecraft.server." + v + ".EntityLiving");
                    Group<Object, Integer> dragonData = this.entities.get(viewer);
                    Object nmsEntityDragon = dragonData.getA();
                    ReflectionUtils.getMethod("setHealth", nmsEntityLivingClass, nmsEntityDragon,
                            new Group<>(new Class<?>[]{float.class}, new Object[]{this.health})
                    );
                    dragonData.setA(nmsEntityDragon);
                    this.entities.put(viewer, dragonData);
                    EntityMetadata.create(nmsEntityDragon).sendPlayer(Bukkit.getServer().getPlayer(viewer));
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * Sets the style for this boss bar (1.9+)
     * @param style the style
     * @return this object
     */
    public BossBar setStyle(Style style) {
        this.style = style;
        if(GameVersion.is1_9Above()) {
            for(UUID viewer : this.viewers) {
                removeViewer(viewer);
                addViewer(viewer);
            }
        }
        return this;
    }
    
    private BossBar add(UUID uuid){
        Player viewer = Bukkit.getServer().getPlayer(uuid);
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v + ".IChatBaseComponent");
            Object title = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{
                                    CommonUtils.isValidJSON(this.title) ? this.title :
                                            "{\"text\": \"" + Chat.color(this.title) + "\"}"
                            }
                    ));

            if(this.health < 0){
                this.health = 0;
            }
            if(this.health > 1){
                this.health = 1;
            }

            if(GameVersion.is1_9Above()){
                Class<?> bossBarPacketClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss");
                Class<?> bossBarActionClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss$Action");
                Class<?> bossBattleClass = Class.forName("net.minecraft.server." + v + ".BossBattle");
                Class<?> abstractedbossBattleClass = Class.forName(
                        "org.anhcraft.spaciouslib.entity.bossbar.BossBattle_" +
                                GameVersion.getVersion().toString().replace("v", ""));
                Class<?> barColorClass = Class.forName("net.minecraft.server." + v + ".BossBattle$BarColor");
                Class<?> barStyleClass = Class.forName("net.minecraft.server." + v + ".BossBattle$BarStyle");
                Object bossBarAction = ReflectionUtils.getEnum("ADD", bossBarActionClass);
                Object barColor = ReflectionUtils.getEnum(this.color.toString(), barColorClass);
                Object barStyle = ReflectionUtils.getEnum(this.style.toString(), barStyleClass);
                Object bossBattle = ReflectionUtils.getConstructor(abstractedbossBattleClass, new Group<>(
                        new Class<?>[]{UUID.class, chatBaseComponentClass, barColorClass, barStyleClass},
                        new Object[]{UUID.randomUUID(), title, barColor, barStyle}
                ));
                ReflectionUtils.setField("b", bossBattleClass, bossBattle, this.health);
                ReflectionUtils.setField("e", bossBattleClass, bossBattle,
                        this.flags.contains(Flag.DARKEN_SKY));
                ReflectionUtils.setField("f", bossBattleClass, bossBattle,
                        this.flags.contains(Flag.PLAY_BOSS_MUSIC));
                ReflectionUtils.setField("g", bossBattleClass, bossBattle,
                        this.flags.contains(Flag.CREATE_FOG));
                Object packet = ReflectionUtils.getConstructor(bossBarPacketClass, new Group<>(
                        new Class<?>[]{bossBarActionClass, bossBattleClass},
                        new Object[]{bossBarAction, bossBattle}
                ));
                new PacketSender(packet).sendPlayer(viewer);
                this.bossBattles.put(uuid, bossBattle);
            }

            else {
                // 1.8 versions doesn't have boss bar packet so we need to spawn a fake ender dragon
                // don't try to visible the dragon because it doesn't work after many testing times

                Class<?> entityEnderDragonClass = Class.forName("net.minecraft.server." + v + ".EntityEnderDragon");
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                Class<?> nmsEntityLivingClass = Class.forName("net.minecraft.server." + v + ".EntityLiving");

                // why -64? to make the ender dragon move behind the viewer
                Location location = viewer.getLocation().getDirection().multiply(64).subtract(viewer.getLocation().toVector()).toLocation(viewer.getWorld());
                Object craftWorld = ReflectionUtils.cast(craftWorldClass, location.getWorld());
                Object nmsWorld = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);

                Object nmsEntityDragon = ReflectionUtils.getConstructor(entityEnderDragonClass, new Group<>(
                        new Class<?>[]{nmsWorldClass},
                        new Object[]{nmsWorld}
                ));
                ReflectionUtils.getMethod("setLocation", nmsEntityClass, nmsEntityDragon, new Group<>(
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
                ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsEntityDragon,
                        new Group<>(new Class<?>[]{String.class}, new Object[]{this.title})
                );
                float maxHealth = (float) ReflectionUtils.getMethod("getMaxHealth", nmsEntityLivingClass, nmsEntityDragon);
                float health = maxHealth * this.health;
                ReflectionUtils.getMethod("setHealth", nmsEntityLivingClass, nmsEntityDragon,
                        new Group<>(new Class<?>[]{float.class}, new Object[]{health})
                );
                LivingEntitySpawn.create(nmsEntityDragon).sendPlayer(viewer);

                int id = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, nmsEntityDragon);
                this.entities.put(uuid, new Group<>(nmsEntityDragon, id));
                this.locationTracker.put(uuid, LocationUtils.loc2str(viewer.getLocation()));
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    private BossBar remove(UUID viewer){
        String v = GameVersion.getVersion().toString();
        try {
            if(GameVersion.is1_9Above()) {
                Class<?> bossBarPacketClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss");
                Class<?> bossBarActionClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss$Action");
                Class<?> bossBattleClass = Class.forName("net.minecraft.server." + v + ".BossBattle");
                Object bossBarAction = ReflectionUtils.getEnum("REMOVE", bossBarActionClass);
                Object packet = ReflectionUtils.getConstructor(bossBarPacketClass, new Group<>(
                        new Class<?>[]{bossBarActionClass, bossBattleClass},
                        new Object[]{bossBarAction, bossBattles.get(viewer)}
                ));
                new PacketSender(packet).sendPlayer(Bukkit.getServer().getPlayer(viewer));
                this.bossBattles.remove(viewer);
            } else {
                EntityDestroy.create(this.entities.get(viewer).getB());
                this.entities.remove(viewer);
                this.locationTracker.remove(viewer);
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            BossBar h = (BossBar) o;
            return new EqualsBuilder()
                    .append(h.entities, this.entities)
                    .append(h.title, this.title)
                    .append(h.health, this.health)
                    .append(h.color, this.color)
                    .append(h.style, this.style)
                    .append(h.viewers, this.viewers)
                    .append(h.flags, this.flags)
                    .append(h.bossBattles, this.bossBattles)
                    .append(h.locationTracker, this.locationTracker)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(29, 15)
                .append(this.entities).append(this.title).append(this.viewers).append(this.style)
                .append(this.color).append(this.health).append(this.flags)
                .append(this.bossBattles).append(this.locationTracker).toHashCode();
    }
}
