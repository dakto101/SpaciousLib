package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.EntityTeleport;
import org.anhcraft.spaciouslib.protocol.LivingEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PacketBuilder;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a hologram implementation.
 */
public class Hologram extends PacketBuilder<Hologram> {
    private LinkedHashMap<Integer, Object> entities = new LinkedHashMap<>();
    private LinkedList<String> lines = new LinkedList<>();
    private Location location;
    private double lineSpacing = 0.25;
    @PlayerCleaner
    private Set<UUID> viewers = new HashSet<>();

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
        AnnotationHandler.register(Hologram.class, this);
    }

    /**
     * Adds the given viewer
     * @param player the unique id of the viewer
     * @return this object
     */
    public Hologram addViewer(UUID player){
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
    public Hologram removeViewer(UUID player){
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
     * Adds a new line to this hologram
     * @param content a line
     * @return this object
     */
    public Hologram addLine(String content){
        this.lines.addFirst(Chat.color(content));
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
    public Hologram setViewers(Set<UUID> viewers) {
        Validate.notNull(packetSender, "You must use the method #buildPackets first!");
        // sends holograms to new viewers
        Set<UUID> add = new HashSet<>(viewers); // clones
        add.removeAll(this.viewers); // removes all existed viewers
        for(UUID player : add){
            add(player);
        }
        // removes the holograms of old viewers which aren't existed in the new list
        Set<UUID> remove = new HashSet<>(this.viewers); // clones
        remove.removeAll(viewers); // removes all non-existed viewers
        for(UUID player : remove){
            remove(player);
        }
        // ... of course, any viewers that aren't affected won't have any updates for their holograms
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
                EntityTeleport.create(nmsArmorStand).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
                a.put(id, nmsArmorStand);
                i++;
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.entities = a;
        return this;
    }

    private Hologram add(UUID uuid){
        Player player = Bukkit.getServer().getPlayer(uuid);
        packetSender.sendPlayer(player);
        return this;
    }

    /**
     * Removes this hologram.<br>
     * Once you call this method, this instance can no longer be used
     */
    public void remove(){
        for(Iterator<UUID> it = getViewers().iterator(); it.hasNext(); ) {
            UUID p = it.next();
            remove(p);
            it.remove();
        }
        this.entities = new LinkedHashMap<>();
        AnnotationHandler.unregister(Hologram.class, this);
    }

    private void remove(UUID p) {
        for(int id : entities.keySet()) {
            EntityDestroy.create(id).sendPlayer(Bukkit.getServer().getPlayer(p));
        }
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
        return new HashCodeBuilder(9, 17)
                .append(this.entities).append(this.lineSpacing).append(this.viewers)
                .append(this.lines).append(this.location).toHashCode();
    }

    @Override
    public Hologram buildPackets() {
        String v = GameVersion.getVersion().toString();
        Location location = getLocation().clone()
                .add(0, getLineSpacing() * getLines().size(), 0);
        try {
            for(String line : getLines()){
                location = location.subtract(0, getLineSpacing(), 0);
                Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
                Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
                Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
                Class<?> nmsArmorStandClass = Class.forName("net.minecraft.server." + v + ".EntityArmorStand");
                Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "IChatBaseComponent$") + "ChatSerializer");
                Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v + ".IChatBaseComponent");
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
                if(GameVersion.is1_13Above()) {
                    if(!CommonUtils.isValidJSON(line)){
                        line = "{\"text\": \"" + line + "\"}";
                    }
                    Object customNameComponent = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                            new Group<>(
                                    new Class<?>[]{String.class},
                                    new String[]{line}
                            ));
                    ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsArmorStand,
                            new Group<>(new Class<?>[]{chatBaseComponentClass}, new Object[]{customNameComponent})
                    );
                } else {
                    ReflectionUtils.getMethod("setCustomName", nmsEntityClass, nmsArmorStand,
                            new Group<>(new Class<?>[]{String.class}, new Object[]{line})
                    );
                }
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
                int entityId = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, nmsArmorStand);
                this.entities.put(entityId, nmsArmorStand);
                packets.add(LivingEntitySpawn.create(nmsArmorStand));
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        createPacketSender();
        return this;
    }
}
