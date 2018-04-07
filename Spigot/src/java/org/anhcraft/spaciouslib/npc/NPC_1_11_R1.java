package org.anhcraft.spaciouslib.npc;

import net.minecraft.server.v1_11_R1.*;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.protocol.PacketSender;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NPC_1_11_R1 extends NPCWrapper {
    private EntityPlayer entity;

    public NPC_1_11_R1(NPC npc) {
        this.npc = npc;
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer w = ((CraftWorld) npc.getLocation().getWorld()).getHandle();
        this.entity = new EntityPlayer(server, w, npc.getGameProfile(), new PlayerInteractManager(w));
        this.entity.setLocation(npc.getLocation().getX(), npc.getLocation().getY(),
                npc.getLocation().getZ(), npc.getLocation().getYaw(), npc.getLocation().getPitch());
    }

    @Override
    public int getEntityID() {
        return this.entity.getId();
    }

    @Override
    public void rotate(double yaw, double pitch) {
        new PacketSender(new PacketPlayOutEntity.PacketPlayOutEntityLook(
                this.entity.getId(), (byte) yaw, (byte) pitch, true)
        ).sendPlayers(this.viewers);
    }

    @Override
    public void setCustomName(String name, boolean show) {
        DataWatcher dw = this.entity.getDataWatcher();
        dw.set(new DataWatcherObject<>(2, DataWatcherRegistry.d), name == null ? "" : Chat.color(name));
        dw.set(new DataWatcherObject<>(3, DataWatcherRegistry.h), show);
        new PacketSender(new PacketPlayOutEntityMetadata(this.entity.getId(), dw, true)).sendPlayers(this.viewers);
    }

    @Override
    public void addViewer(Player player){
        this.viewers.add(player);
        new PacketSender(new PacketPlayOutNamedEntitySpawn(this.entity)).sendPlayer(player);

        // https://www.spigotmc.org/threads/protocollib-hide-change-player-npc-name.298555/
        tablist(true, player);
        new BukkitRunnable() {
            @Override
            public void run() {
                tablist(false, player);
            }
        }.runTaskLaterAsynchronously(SpaciousLib.instance, 20);
    }

    @Override
    public void addViewers(Player... players){
        this.viewers.addAll(CommonUtils.toList(players));
        new PacketSender(new PacketPlayOutNamedEntitySpawn(this.entity)).sendPlayers(players);
        tablist(true, CommonUtils.toList(players));
        new BukkitRunnable() {
            @Override
            public void run() {
                tablist(false, CommonUtils.toList(players));
            }
        }.runTaskLaterAsynchronously(SpaciousLib.instance, 20);
    }

    @Override
    public void removeViewer(Player player){
        this.viewers.remove(player);
        new PacketSender(new PacketPlayOutEntityDestroy(this.entity.getId())).sendPlayer(player);
        tablist(false, player);
    }

    @Override
    public void removeViewers(Player... players){
        this.viewers.removeAll(CommonUtils.toList(players));
        new PacketSender(new PacketPlayOutEntityDestroy(this.entity.getId())).sendPlayers(players);
        tablist(false, CommonUtils.toList(players));
    }

    @Override
    public void addTabList() {
        tablist(true, this.viewers);
    }

    @Override
    public void removeTabList() {
        tablist(false, this.viewers);
    }

    private void tablist(boolean add, Player player) {
        new PacketSender(new PacketPlayOutPlayerInfo(add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER : PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity)).sendPlayer(player);
    }

    private void tablist(boolean add, List<Player> players) {
        new PacketSender(new PacketPlayOutPlayerInfo(add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER : PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity)).sendPlayers(players);
    }
}
