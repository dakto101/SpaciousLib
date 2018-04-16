package org.anhcraft.spaciouslib.entity;

import com.mojang.authlib.GameProfile;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.listeners.NPCInteractEventListener;
import org.anhcraft.spaciouslib.protocol.EntityDestroy;
import org.anhcraft.spaciouslib.protocol.NamedEntitySpawn;
import org.anhcraft.spaciouslib.protocol.PlayerInfo;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class NPC {
    private Object nmsEntityPlayer;
    private int entity = -1;
    private GameProfile gameProfile;
    private Location location;
    private List<Player> viewers = new ArrayList<>();
    private boolean tablist = false;

    /**
     * Creates a new NPC instance
     * @param gameProfile the game profile of the NPC
     * @param location the location of the NPC
     */
    public NPC(GameProfile gameProfile, Location location){
        this.gameProfile = gameProfile;
        this.location = location;
        NPCInteractEventListener.data.add(this);
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
        NPCInteractEventListener.data.add(this);
    }

    /**
     * Gets the location of this NPC
     * @return the lcoation
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
    public int getEntityID(){
        return this.entity;
    }

    /**
     * Adds the given viewer
     * @param player the viewer
     * @return this object
     */
    public NPC addViewer(Player player){
        this.viewers.add(player);
        return this;
    }

    /**
     * Removes the given viewer
     * @param player the viewer
     * @return this object
     */
    public NPC removeViewer(Player player){
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
     * Checks does the tablist enable
     * @return true if yes
     */
    public boolean isTabListEnabled(){
        return this.tablist;
    }

    /**
     * Sets the viewers who you want to show this NPC to
     * @param viewers the list of viewers
     * @return this object
     */
    public NPC setViewers(List<Player> viewers) {
        this.viewers = viewers;
        return this;
    }

    /**
     * Spawns this NPC
     * @return this object
     */
    public NPC spawn(){
        String v = GameVersion.getVersion().toString();
        if(this.entity != -1){
            remove();
        }
        try {
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");
            Class<?> nmsWorldClass = Class.forName("net.minecraft.server." + v + ".World");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftServer");
            Class<?> nmsServerClass = Class.forName("net.minecraft.server." + v + ".MinecraftServer");
            Class<?> nmsPlayerInteractManagerClass = Class.forName("net.minecraft.server." + v + ".PlayerInteractManager");
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            Object nmsServer = ReflectionUtils.getField("console", craftServerClass, craftServer);
            Object craftWorld = ReflectionUtils.cast(craftWorldClass, location.getWorld());
            Object nmsWorld = ReflectionUtils.getMethod("getHandle", craftWorldClass, craftWorld);
            Object playerInteractManager = ReflectionUtils.getConstructor(nmsPlayerInteractManagerClass, new Group<>(
                    new Class<?>[]{
                            nmsWorldClass
                    }, new Object[]{
                    nmsWorld
            }
            ));
            this.nmsEntityPlayer = ReflectionUtils.getConstructor(nmsEntityPlayerClass, new Group<>(
                    new Class<?>[]{
                            nmsServerClass, nmsWorldClass, GameProfile.class, nmsPlayerInteractManagerClass
                    }, new Object[]{
                    nmsServer, nmsWorld, this.gameProfile, playerInteractManager
            }
            ));
            ReflectionUtils.getMethod("setLocation", nmsEntityClass, nmsEntityPlayer, new Group<>(
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

            this.entity = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, this.nmsEntityPlayer);
            PlayerInfo.create(PlayerInfo.Type.ADD_PLAYER, this.nmsEntityPlayer).sendPlayers(getViewers());
            NamedEntitySpawn.create(this.nmsEntityPlayer).sendPlayers(getViewers());
            if(!this.tablist) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, nmsEntityPlayer).sendPlayers(getViewers());
                    }
                }.runTaskLaterAsynchronously(SpaciousLib.instance, 10);
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Removes this NPC
     * @return this object
     */
    public NPC remove(){
        PlayerInfo.create(PlayerInfo.Type.REMOVE_PLAYER, this.nmsEntityPlayer).sendPlayers(getViewers());
        EntityDestroy.create(this.entity).sendPlayers(getViewers());
        this.entity = -1;
        return this;
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
        return new HashCodeBuilder(6, 17)
                .append(this.gameProfile).append(this.location).append(this.tablist).append(this.entity).append(this.viewers).toHashCode();
    }
}
