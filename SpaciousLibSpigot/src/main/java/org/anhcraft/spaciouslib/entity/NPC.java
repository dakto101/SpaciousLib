package org.anhcraft.spaciouslib.entity;

import com.mojang.authlib.GameProfile;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.listeners.NPCInteractEventListener;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPC extends PacketBuilder<NPC> {
    private Object nmsEntityPlayer;
    private int entity = -1;
    private GameProfile gameProfile;
    private Location location;
    @PlayerCleaner
    private Set<UUID> viewers = new HashSet<>();
    private boolean tablist = false;
    private Object tabListPacket;

    private void init() {
        NPCInteractEventListener.data.add(this);
        AnnotationHandler.register(NPC.class, this);
    }

    /**
     * Creates a new NPC instance
     * @param gameProfile the game profile of the NPC
     * @param location the location of the NPC
     */
    public NPC(GameProfile gameProfile, Location location){
        this.gameProfile = gameProfile;
        this.location = location;
        init();
    }

    /**
     * Creates a new NPC instance
     * @param gameProfile the game profile of the NPC
     * @param location the location of the NPC
     * @param tablist true if you want to show the name of the NPC in the tab list
     */
    public NPC(GameProfile gameProfile, Location location, boolean tablist){
        this.gameProfile = gameProfile;
        this.location = location;
        this.tablist = tablist;
        init();
    }

    /**
     * Gets the location of this NPC
     * @return the location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Gets the profile of this NPC
     * @return the profile
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    /**
     * Gets the id of this NPC.<br>
     * If this NPC isn't spawned yet, this method will return "-1"
     * @return the id number
     */
    public int getEntityId(){
        return this.entity;
    }

    /**
     * Adds the given viewer
     * @param player the unique id of a player
     * @return this object
     */
    public NPC addViewer(UUID player){
        Validate.notNull(packetSender, "You must use the method #buildPackets first!");
        this.viewers.add(player);
        add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the unique id of the player
     * @return this object
     */
    public NPC removeViewer(UUID player){
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
     * Checks is the NPC show on the tab list
     * @return true if yes
     */
    public boolean isShowOnTabList(){
        return this.tablist;
    }

    /**
     * Sets the viewers who you want to show this NPC to
     * @param viewers a list contains unique ids of viewers
     * @return this object
     */
    public NPC setViewers(Set<UUID> viewers) {
        Validate.notNull(packetSender, "You must use the method #buildPackets first!");
        // sends NPC to new viewers
        Set<UUID> add = new HashSet<>(viewers); // clones
        add.removeAll(this.viewers); // removes all existed viewers
        for(UUID player : add){
            add(player);
        }
        // removes the NPCs of old viewers which aren't existed in the new list
        Set<UUID> remove = new HashSet<>(this.viewers); // clones
        remove.removeAll(viewers); // removes all non-existed viewers
        for(UUID player : remove){
            remove(player);
        }
        // ... of course, any viewers that aren't affected won't have any updates for their NPCs
        this.viewers = viewers;
        return this;
    }

    public NPC rotate(byte yaw, byte pitch){
        EntityLook.create(getEntityId(), yaw, pitch, false).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
        return this;
    }

    /**
     * Teleports this NPC to a new location
     * @return this object
     */
    public NPC teleport(Location location){
        this.location = location;
        ReflectionUtils.getMethod("setLocation", ClassFinder.NMS.Entity, this.nmsEntityPlayer, new Group<>(
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
        EntityTeleport.create(this.nmsEntityPlayer).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
        return this;
    }

    private NPC add(UUID uuid){
        Player player = Bukkit.getServer().getPlayer(uuid);
        packetSender.sendPlayer(player);
        Bukkit.getScheduler().runTaskLaterAsynchronously(SpaciousLib.instance, () -> new PacketSender(tabListPacket).sendPlayer(player), 40);
        return this;
    }

    /**
     * Removes this NPC.<br>
     * Once you call this method, this instance can no longer be used
     */
    public void remove(){
        for(Iterator<UUID> it = getViewers().iterator(); it.hasNext(); ) {
            UUID p = it.next();
            remove(p);
            it.remove();
        }
        this.entity = -1;
        NPCInteractEventListener.data.remove(this);
        AnnotationHandler.unregister(NPC.class, this);
    }

    private void remove(UUID u) {
        Player p = Bukkit.getServer().getPlayer(u);
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, this.nmsEntityPlayer).sendPlayer(p);
        EntityDestroy.create(this.entity).sendPlayer(p);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            NPC n = (NPC) o;
            return new EqualsBuilder()
                    .append(n.gameProfile, this.gameProfile)
                    .append(n.location, this.location)
                    .append(n.tablist, this.tablist)
                    .append(n.entity, this.entity)
                    .append(n.viewers, this.viewers)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(17, 27)
                .append(this.gameProfile).append(this.location).append(this.tablist).append(this.entity).append(this.viewers).toHashCode();
    }

    @Override
    public NPC buildPackets() {
        if(this.entity != -1){
            remove();
        }
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object nmsServer = ReflectionUtils.getField("console", ClassFinder.CB.CraftServer, craftServer);
        Object craftWorldServer = ReflectionUtils.cast(ClassFinder.CB.CraftWorld, location.getWorld());
        Object nmsWorldServer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftWorld, craftWorldServer);
        Object playerInteractManager = ReflectionUtils.getConstructor(ClassFinder.NMS.PlayerInteractManager, new Group<>(
                new Class<?>[]{
                        ClassFinder.NMS.World
                }, new Object[]{nmsWorldServer}
        ));
        this.nmsEntityPlayer = ReflectionUtils.getConstructor(ClassFinder.NMS.EntityPlayer, new Group<>(
                new Class<?>[]{
                        ClassFinder.NMS.MinecraftServer, ClassFinder.NMS.WorldServer, GameProfile.class, ClassFinder.NMS.PlayerInteractManager
                }, new Object[]{
                nmsServer, nmsWorldServer, this.gameProfile, playerInteractManager
        }
        ));
        ReflectionUtils.getMethod("setLocation", ClassFinder.NMS.Entity, nmsEntityPlayer, new Group<>(
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

        this.entity = (int) ReflectionUtils.getMethod("getId", ClassFinder.NMS.Entity, this.nmsEntityPlayer);
        packets.add(PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, this.nmsEntityPlayer));
        packets.add(NamedEntitySpawn.create(this.nmsEntityPlayer));
        if(!this.tablist) {
            tabListPacket = PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, nmsEntityPlayer);
        }
        createPacketSender();
        return this;
    }
}
