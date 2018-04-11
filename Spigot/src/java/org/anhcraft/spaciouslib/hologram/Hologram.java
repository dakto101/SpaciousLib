package org.anhcraft.spaciouslib.hologram;

import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.LivingEntitySpawn;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a hologram implementation.
 */
public class Hologram {
    private List<Integer> entities;
    private LinkedList<String> lines;
    private Location location;
    private double lineSpacing;
    private List<Player> viewers;

    /**
     * Creates a new Hologram instance
     * @param location the hologram location
     */
    public Hologram(Location location){
        this.lines = new LinkedList<>();
        this.location = location;
        this.lineSpacing = 0.25;
        this.viewers = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    /**
     * Creates a new Hologram instance
     * @param location the hologram location
     * @param lineSpacing the spacing between lines
     */
    public Hologram(Location location, double lineSpacing){
        this.lines = new LinkedList<>();
        this.location = location;
        this.lineSpacing = lineSpacing;
        this.viewers = new ArrayList<>();
        this.entities = new ArrayList<>();
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
        this.viewers = new ArrayList<>();
        this.entities = new ArrayList<>();
        addLines(lines);
    }

    /**
     * Adds the given viewer
     * @param player the viewer
     * @return this object
     */
    public Hologram addViewer(Player player){
        this.viewers.add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the viewer
     * @return this object
     */
    public Hologram removeViewer(Player player){
        this.viewers.remove(player);
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
     * Sets the new viewers
     * @param viewers list of viewers
     * @return this object
     */
    public Hologram setViewers(List<Player> viewers){
        this.viewers = viewers;
        return this;
    }

    /**
     * Adds a new line to the specified hologram
     * @param content the content of the line
     * @return this object
     */
    public Hologram addLine(String content){
        this.lines.add(Chat.color(content));
        return this;
    }

    /**
     * Adds new lines to that holgoram
     * @param content array of line
     * @return this object
     */
    public Hologram addLines(String... content){
        for(String cont : content){
            addLine(cont);
        }
        return this;
    }

    /**
     * Removes a specific line of the specified hologram
     * @param index the index of the line
     * @return this object
     */
    public Hologram removeLine(int index){
        this.lines.remove(index);
        return this;
    }

    /**
     * Gets the lines amount of the specified hologram
     * @return the amount
     */
    public int getLineAmount(){
        return this.lines.size();
    }

    /**
     * Gets the line spacing of the specified hologram
     * @return the line spacing
     */
    public double getLineSpacing(){
        return this.lineSpacing;
    }

    /**
     * Gets the location of the specified hologram
     * @return the Location object
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * Sets a new location for the specified hologram
     * @param location the Location object
     * @return this object
     */
    public Hologram setLocation(Location location){
        this.location = location;
        return this;
    }

    /**
     * Gets all lines of the specified hologram
     * @return a linked list of lines
     */
    public LinkedList<String> getLines(){
        return this.lines;
    }

    /**
     * Sets new lines for the specified hologram
     * @param lines a linked list of lines
     * @return this object
     */
    public Hologram setLines(LinkedList<String> lines){
        this.lines = lines;
        return this;
    }

    /**
     * Spawns the specified holgoram
     * @return this object
     */
    public Hologram spawn(){
        if(0 < this.entities.size()){
            remove();
        }
        int i = 0;
        for(String line : getLines()){
            double y = i * getLineSpacing();
            Location location = getLocation().clone().add(0, y, 0);
            try {
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".Entity");
                Class<?> nmsArmorStandClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".EntityArmorStand");
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
                this.entities.add(entityID);
                LivingEntitySpawn.create(nmsArmorStand).sendPlayers(getViewers());
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }
        return this;
    }

    /**
     * Removes the specified hologram
     * @return this object
     */
    public Hologram remove(){
        for(int hw : this.entities){
            EntityDestroy.create(hw).sendPlayers(getViewers());
        }
        this.entities = new ArrayList<>();
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
