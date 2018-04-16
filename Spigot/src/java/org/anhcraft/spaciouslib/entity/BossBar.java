package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    private List<Player> viewers = new ArrayList<>();

    // 1.8
    private LinkedHashMap<Player, String> locationTracker = new LinkedHashMap<>();
    private LinkedHashMap<Player, Group<Object, Integer>> entities = new LinkedHashMap<>();

    // 1.9 and above
    private LinkedHashMap<Player, Object> bossBattles = new LinkedHashMap<>();

    private void startTaskFakeEnderDragon() {
        if(GameVersion.is1_9Above()){
            return;
        }
        //// ONLY FOR 1.8 ////
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String v = GameVersion.getVersion().toString();
                    Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                    for(Player player : viewers){
                        if(!locationTracker.containsKey(player)){
                            locationTracker.put(player, LocationUtils.loc2str(player.getLocation()));
                            return;
                        }
                        if(entities.containsKey(player)) {
                            Location old = LocationUtils.str2loc(locationTracker.get(player));
                            // if a player has teleported to a new world
                            // we will remove the bar of that player and add it again
                            // the dragon will teleport to the new world
                            if(!old.getWorld().getName().equals(player.getWorld().getName())){
                                remove(player);
                                add(player);
                                return;
                            }
                            // if the player just move around the world, we will send a teleport packet
                            // to tell the client to update the location of the dragon
                            if(old.getBlockX() != player.getLocation().getBlockX()
                                    || old.getBlockY() != player.getLocation().getBlockY()
                                    || old.getBlockZ() != player.getLocation().getBlockZ()) {
                                locationTracker.put(player, LocationUtils.loc2str(player.getLocation()));
                                Location location = player.getLocation().getDirection().multiply(32).add(player.getLocation().toVector()).toLocation(player.getWorld());
                                Object nmsEntityDragon = entities.get(player).getA();
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
                                entities.put(player, entities.get(player).setA(nmsEntityDragon));
                                EntityTeleport.create(nmsEntityDragon).sendPlayer(player);
                            }
                        }
                    }
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, 20);
    }

    /**
     * Creates a new boss bar instance
     * @param title the title of the boss bar
     */
    public BossBar(String title){
        this.title = title;
        startTaskFakeEnderDragon();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar
     */
    public BossBar(String title, Color color){
        this.title = title;
        this.color = color;
        startTaskFakeEnderDragon();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar
     * @param style the style for the boss bar
     */
    public BossBar(String title, Color color, Style style){
        this.title = title;
        this.color = color;
        this.style = style;
        startTaskFakeEnderDragon();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar
     * @param style the style for the boss bar
     * @param health the health amount for the boss bar, must be from 0 to 1
     */
    public BossBar(String title, Color color, Style style, float health){
        this.title = title;
        this.color = color;
        this.style = style;
        this.health = health;
        startTaskFakeEnderDragon();
    }

    /**
     * Creates a new boss bar instance
     * @param title the title for the boss bar
     * @param color the color for the boss bar
     * @param style the style for the boss bar
     * @param health the health amount for the boss bar, must be from 0 to 1
     * @param flags the array of flags for the boss bar
     */
    public BossBar(String title, Color color, Style style, float health, Flag... flags){
        this.title = title;
        this.color = color;
        this.style = style;
        this.health = health;
        this.flags = new HashSet<>(CommonUtils.toList(flags));
        startTaskFakeEnderDragon();
    }

    /**
     * Adds the given viewer
     * @param player the viewer
     * @return this object
     */
    public BossBar addViewer(Player player){
        if(!this.viewers.contains(player)) {
            this.viewers.add(player);
            add(player);
        }
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the viewer
     * @return this object
     */
    public BossBar removeViewer(Player player){
        if(this.viewers.contains(player)) {
            this.viewers.remove(player);
            remove(player);
        }
        return this;
    }

    /**
     * Gets all viewers
     * @return list of viewers
     */
    public List<Player> getViewers(){
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
     * @param viewers the list of viewers
     * @return this object
     */
    public BossBar setViewers(List<Player> viewers) {
        // adds the boss bars for new viewers
        List<Player> add = new ArrayList<>(viewers); // clones
        add.removeAll(this.viewers); // removes all existed viewers
        for(Player player : add){
            add(player);
        }
        // removes the boss bars of old viewers which aren't existed in the new list
        List<Player> remove = new ArrayList<>(this.viewers); // clones
        remove.removeAll(viewers); // removes all non-existed viewers
        for(Player player : remove){
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
            for(Player viewer : this.viewers){
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
        for(Player viewer : this.viewers) {
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
                    EntityMetadata.create(nmsEntityDragon).sendPlayer(viewer);
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
            for(Player viewer : this.viewers){
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
        for(Player viewer : this.viewers) {
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
                    EntityMetadata.create(nmsEntityDragon).sendPlayer(viewer);
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
            for(Player viewer : this.viewers) {
                removeViewer(viewer);
                addViewer(viewer);
            }
        }
        return this;
    }
    
    private BossBar add(Player viewer){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v + ".IChatBaseComponent");
            Object title = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{this.title}
                    ));

            if(GameVersion.is1_9Above()){
                Class<?> bossBarPacketClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss");
                Class<?> bossBarActionClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBoss$Action");
                Class<?> bossBattleClass = Class.forName("net.minecraft.server." + v + ".BossBattle");
                Class<?> barColorClass = Class.forName("net.minecraft.server." + v + ".BossBattle$BarColor");
                Class<?> barStyleClass = Class.forName("net.minecraft.server." + v + ".BossBattle$BarStyle");
                Object bossBarAction = ReflectionUtils.getEnum("ADD", bossBarActionClass);
                Object barColor = ReflectionUtils.getEnum(this.color.toString(), barColorClass);
                Object barStyle = ReflectionUtils.getEnum(this.style.toString(), barStyleClass);
                Object bossBattle = ReflectionUtils.getConstructor(bossBattleClass, new Group<>(
                        new Class<?>[]{UUID.class, chatBaseComponentClass, barColorClass, barStyleClass},
                        new Object[]{UUID.randomUUID(), title, barColor, barStyle}
                ));
                if(this.health < 0){
                    this.health = 0;
                }
                if(this.health > 1){
                    this.health = 1;
                }
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
                this.bossBattles.put(viewer, bossBattle);
            }

            else {
                // 1.8 versions doesn't have boss bar packet so we need to spawn a fake ender dragon
                
                Class<?> entityEnderDragonClass = Class.forName("net.minecraft.server." + v + ".EntityEnderDragon");
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                Class<?> nmsEntityLivingClass = Class.forName("net.minecraft.server." + v + ".EntityLiving");

                Location location = viewer.getLocation().getDirection().multiply(32).add(viewer.getLocation().toVector()).toLocation(viewer.getWorld());
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
                ReflectionUtils.getMethod("setInvisible", nmsEntityClass, nmsEntityDragon,
                        new Group<>(new Class<?>[]{boolean.class}, new Object[]{true})
                );
                ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsEntityDragon,
                        new Group<>(new Class<?>[]{String.class}, new Object[]{this.title})
                );
                ReflectionUtils.getMethod("setHealth", nmsEntityLivingClass, nmsEntityDragon,
                        new Group<>(new Class<?>[]{float.class}, new Object[]{this.health})
                );
                LivingEntitySpawn.create(nmsEntityDragon).sendPlayer(viewer);
                this.entities.put(viewer, new Group<>(nmsEntityDragon, (int) ReflectionUtils.getMethod("getId", nmsEntityClass, nmsEntityDragon)));
                this.locationTracker.put(viewer, LocationUtils.loc2str(viewer.getLocation()));
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Removes this boss bar
     * @return this object
     */
    private BossBar remove(Player viewer){
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
                new PacketSender(packet).sendPlayer(viewer);
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
        return new HashCodeBuilder(14, 29)
                .append(this.entities).append(this.title).append(this.viewers).append(this.style)
                .append(this.color).append(this.health).append(this.flags)
                .append(this.bossBattles).append(this.locationTracker).toHashCode();
    }
}
