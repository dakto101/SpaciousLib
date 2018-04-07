package org.anhcraft.spaciouslib.npc;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.events.NPCInteractEvent;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A class helps you to manage all NPCs which are created by this library
 */
public class NPCManager implements Listener {
    protected static LinkedHashMap<String, NPCWrapper> data = new LinkedHashMap<>();

    public NPCManager(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(NPCWrapper n : data.values()){
                    NPC npc = n.getNPC();
                    if(npc.getAdditions().contains(NPC.Addition.NEARBY_RENDER)){
                        List<Player> removePlayers = new ArrayList<>();
                        for(Player player : n.getViewers()) {
                            if(player.getLocation().distance(npc.getLocation()) > npc.getNearbyRadius()) {
                                removePlayers.add(player);
                            }
                        }
                        n.removeViewers(CommonUtils.toArray(removePlayers, Player.class));
                        List<Player> players = npc.getLocation().getWorld().getPlayers();
                        players.removeAll(removePlayers);
                        for(Player player : players){
                            if(player.getLocation().distance(npc.getLocation()) <= npc.getNearbyRadius()) {
                                n.addViewer(player);
                            }
                        }
                    }
                    if(npc.getAdditions().contains(NPC.Addition.LOOK_VIEWER)
                             && 0 < n.getViewers().size()){
                        Player nearest = null;
                        double last = 0;
                        for(Player player : n.getViewers()) {
                            double dis = player.getLocation().distance(npc.getLocation());
                            if(dis < last){
                                nearest = player;
                                last = dis;
                            }
                        }
                        double dx = nearest.getLocation().getX()-npc.getLocation().getX();
                        double dy = nearest.getLocation().getY()-npc.getLocation().getY();
                        double dz = nearest.getLocation().getZ()-npc.getLocation().getZ();
                        double r = Math.sqrt(dx*dx + dy*dy + dz*dz);
                        double yaw = -Math.atan2(dx,dz)/Math.PI*180;
                        if (yaw < 0) {
                            yaw = 360 + yaw;
                        }
                        double pitch = -Math.asin(dy/r)/Math.PI*180;
                        n.rotate(yaw, pitch);
                    }
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 20, 20);
    }

    @EventHandler
    public void packetHandler(PacketHandleEvent ev){
        if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInUseEntity")){
            for(NPCWrapper n : data.values()){
                if(n.getEntityID() == (int) ev.getPacketValue("a")){
                    EquipSlot es = EquipSlot.MAINHAND;
                    if(GameVersion.is1_9Above() && ev.getPacketValue("d").toString().equals("OFF_HAND")){
                        es = EquipSlot.OFFHAND;
                    }
                    NPCInteractEvent e = new NPCInteractEvent(ev.getPlayer(), n.getNPC(), ev.getPacketValue("action").toString().equals("ATTACK"), es);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    break;
                }
            }
        }
    }

    public static NPCWrapper register(String id, NPC npc){
        NPCWrapper wrapper = null;
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.npc.NPC_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor(NPC.class);
            wrapper = (NPCWrapper) c.newInstance(npc);
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        data.put(id, wrapper);
        return wrapper;
    }

    public static void unregister(String id){
        if(data.containsKey(id)){
            NPCWrapper w = data.get(id);
            w.despawn();
        }
    }
}
