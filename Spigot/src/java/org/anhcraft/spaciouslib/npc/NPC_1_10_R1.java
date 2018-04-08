package org.anhcraft.spaciouslib.npc;

import net.minecraft.server.v1_10_R1.*;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.protocol.PacketSender;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NPC_1_10_R1 extends NPCWrapper {
    private EntityPlayer entity;

    public NPC_1_10_R1(NPC npc) {
        this.npc = npc;
        init();
    }

    @Override
    protected void init() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer w = ((CraftWorld) this.npc.getLocation().getWorld()).getHandle();
        this.entity = new EntityPlayer(server, w, this.npc.getGameProfile(), new PlayerInteractManager(w));
        this.entity.setLocation(this.npc.getLocation().getX(), this.npc.getLocation().getY(),
                this.npc.getLocation().getZ(), this.npc.getLocation().getYaw(),
                this.npc.getLocation().getPitch());
    }

    @Override
    public int getEntityID() {
        return this.entity.getId();
    }

    @Override
    public void addViewer(Player player){
        this.viewers.add(player);
        // https://www.spigotmc.org/threads/protocollib-hide-change-player-npc-name.298555/
        // must send the tablist packet first
        tablist(true, player);

        new PacketSender(new PacketPlayOutNamedEntitySpawn(this.entity)).sendPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                tablist(false, player);
            }
        }.runTaskLaterAsynchronously(SpaciousLib.instance, 10);
    }

    @Override
    public void addViewers(Player... players){
        this.viewers.addAll(CommonUtils.toList(players));
        tablist(true, CommonUtils.toList(players));
        new PacketSender(new PacketPlayOutNamedEntitySpawn(this.entity)).sendPlayers(players);
        new BukkitRunnable() {
            @Override
            public void run() {
                tablist(false, CommonUtils.toList(players));
            }
        }.runTaskLaterAsynchronously(SpaciousLib.instance, 10);
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
        this.tabList = true;
        tablist(true, this.viewers);
    }

    @Override
    public void removeTabList() {
        this.tabList = false;
        tablist(false, this.viewers);
    }

    private void tablist(boolean add, Player player) {
        new PacketSender(new PacketPlayOutPlayerInfo(add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER : PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity)).sendPlayer(player);
    }

    private void tablist(boolean add, List<Player> players) {
        new PacketSender(new PacketPlayOutPlayerInfo(add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER : PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity)).sendPlayers(players);
    }
}
