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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
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

    /**
     * Initializes NPCManager
     */
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
                        players.removeAll(n.getViewers());
                        for(Player player : players){
                            if(player.getLocation().distance(npc.getLocation()) <= npc.getNearbyRadius()) {
                                n.addViewer(player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, 20);
    }

    @EventHandler
    public void packetHandler(PacketHandleEvent ev){
        if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInUseEntity")){
            for(NPCWrapper n : data.values()){
                if(n.getEntityID() == (int) ev.getPacketValue("a")){
                    EquipSlot es = EquipSlot.MAINHAND;
                    if(GameVersion.is1_9Above()){
                        Object d = ev.getPacketValue("d");
                        if(d != null && d.toString().equals("OFF_HAND")) {
                            es = EquipSlot.OFFHAND;
                        }
                    }
                    NPCInteractEvent e = new NPCInteractEvent(ev.getPlayer(), n.getNPC(), ev.getPacketValue("action").toString().equals("ATTACK"), es);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    ev.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void playerQuitHandler(PlayerQuitEvent ev){
        for(String n : data.keySet()){
            NPCWrapper npc = data.get(n);
            if(npc.getViewers().contains(ev.getPlayer())){
                npc.removeViewer(ev.getPlayer());
            }
        }
    }

    @EventHandler
    public void worldUnloadHandler(WorldUnloadEvent ev){
        List<String> remove = new ArrayList<>();
        for(String n : data.keySet()){
            NPCWrapper npc = data.get(n);
            if(npc.getNPC().getLocation().getWorld().getName().equals(ev.getWorld().getName())){
                remove.add(n);
            }
        }
        for(String i : remove){
            unregister(i);
        }
    }

    /**
     * Registers a new NPC
     * @param id the id of that NPC
     * @param npc the NPC Object
     * @return NPCWrapper object
     */
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

    /**
     * Unregisters a specific NPC
     * @param id the id of the NPC
     */
    public static void unregister(String id){
        if(data.containsKey(id)){
            NPCWrapper w = data.get(id);
            w.remove();
            data.remove(id);
        }
    }

    /**
     * Unregisters all NPCs
     */
    public static void unregisterAll() {
        for(String id : data.keySet()){
            NPCWrapper w = data.get(id);
            w.remove();
            data.remove(id);
        }
    }
}
