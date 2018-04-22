package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.listeners.PlayerCleaner;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.EntityTeleport;
import org.anhcraft.spaciouslib.protocol.LivingEntitySpawn;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Represents a hologram implementation.
 */
public class Hologram {
    private LinkedHashMap<Integer, Object> entities = new LinkedHashMap<>();
    private LinkedList<String> lines = new LinkedList<>();
    private Location location;
    private double lineSpacing = 0.25;
    private List<UUID> viewers = new ArrayList<>();

    /**
     * Creates a new Hologram instance
     * @param location the hologram location
     */
    public Hologram(Location location){
        this.location = location;
        init();
    }

    /**
     * Creates a new Hologram instance
     * @param location the hologram location
     * @param lineSpacing the spacing between lines
     */
    public Hologram(Location location, double lineSpacing){
        this.location = location;
        this.lineSpacing = lineSpacing;
        init();
    }

    /**
     * Creates a new Hologram instance
     * @param location the hologram location
     * @param lineSpacing the spacing between lines
     * @param lines the array of lines
     */
    public Hologram(Location location, double lineSpacing, String... lines){
        this.location = location;
        this.lineSpacing = lineSpacing;
        addLines(lines);
        init();
    }

    private void init() {
        PlayerCleaner.add(this.viewers);
    }

    /**
     * Adds the given viewer
     * @param player the unique id of the viewer
     * @return this object
     */
    public Hologram addViewer(UUID player){
        this.viewers.add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the unique id of viewer
     * @return this object
     */
    public Hologram removeViewer(UUID player){
        this.viewers.remove(player);
        return this;
    }

    /**
     * Gets all viewers
     * @return a list contains unique ids of viewers
     */
    public List<UUID> getViewers(){
        return this.viewers;
    }

    /**
     * Adds a new line to this hologram
     * @param content a line
     * @return this object
     */
    public Hologram addLine(String content){
        this.lines.add(Chat.color(content));
        return this;
    }

    /**
     * Adds new lines to that hologram
     * @param content array of lines
     * @return this object
     */
    public Hologram addLines(String... content){
        for(String cont : content){
            addLine(cont);
        }
        return this;
    }

    /**
     * Removes a specific line of this hologram
     * @param index the index of a line
     * @return this object
     */
    public Hologram removeLine(int index){
        this.lines.remove(index);
        return this;
    }

    /**
     * Gets the lines amount of this hologram
     * @return the amount
     */
    public int getLineAmount(){
        return this.lines.size();
    }

    /**
     * Gets the line spacing of this hologram
     * @return the line spacing
     */
    public double getLineSpacing(){
        return this.lineSpacing;
    }

    /**
     * Gets the location of this hologram
     * @return the Location object
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * Gets all lines of this hologram
     * @return a linked list of lines
     */
    public LinkedList<String> getLines(){
        return this.lines;
    }

    /**
     * Sets the viewers who you want to show this hologram to
     * @param viewers a list contains unique ids of viewers
     * @return this object
     */
    public Hologram setViewers(List<UUID> viewers) {
        this.viewers = viewers;
        return this;
    }

    /**
     * Teleports this hologram to a new location
     * @return this object
     */
    public Hologram teleport(Location location){
        this.location = location;
        LinkedHashMap<Integer, Object> a = new LinkedHashMap<>();
        String v = GameVersion.getVersion().toString();
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        try {
            int i = 0;
            for(int id : this.entities.keySet()) {
                Object nmsArmorStand = this.entities.get(id);
                double y = i * getLineSpacing();
                location = location.clone().add(0, y, 0);
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
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
                EntityTeleport.create(nmsArmorStand).sendPlayers(receivers);
                a.put(id, nmsArmorStand);
                i++;
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.entities = a;
        return this;
    }

    /**
     * Spawns this hologram.<br>
     * If you just want to teleport this hologram, please use the method "teleport" instead
     * @return this object
     */
    public Hologram spawn(){
        String v = GameVersion.getVersion().toString();
        if(0 < this.entities.size()){
            remove();
        }
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        int i = 0;
        try {
            for(String line : getLines()){
                double y = i * getLineSpacing();
                Location location = getLocation().clone().add(0, y, 0);
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                Class<?> nmsArmorStandClass = Class.forName("net.minecraft.server." + v + ".EntityArmorStand");
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
                this.entities.put(entityID, nmsArmorStand);
                LivingEntitySpawn.create(nmsArmorStand).sendPlayers(receivers);
                i++;
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Removes this hologram
     * @return this object
     */
    public Hologram remove(){
        List<Player> receivers = new ArrayList<>();
        for(UUID uuid : getViewers()){
            receivers.add(Bukkit.getServer().getPlayer(uuid));
        }
        for(int hw : this.entities.keySet()){
            EntityDestroy.create(hw).sendPlayers(receivers);
        }
        this.entities = new LinkedHashMap<>();
        return this;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Hologram h = (Hologram) o;
            return new EqualsBuilder()
                    .append(h.entities, this.entities)
                    .append(h.lines, this.lines)
                    .append(h.location, this.location)
                    .append(h.lineSpacing, this.lineSpacing)
                    .append(h.viewers, this.viewers)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(9, 16)
                .append(this.entities).append(this.lineSpacing).append(this.viewers)
                .append(this.lines).append(this.location).toHashCode();
    }
}
