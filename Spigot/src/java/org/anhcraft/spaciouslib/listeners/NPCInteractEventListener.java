package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.entity.NPC;
import org.anhcraft.spaciouslib.events.NPCInteractEvent;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class NPCInteractEventListener implements Listener{
    public static List<NPC> data = new ArrayList<>();

    @EventHandler
    public void packetHandler(PacketHandleEvent ev){
        if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInUseEntity")){
            for(NPC n : data){
                if(n.getEntityID() == (int) ev.getPacketValue("a")){
                    EquipSlot es = EquipSlot.MAINHAND;
                    if(GameVersion.is1_9Above()){
                        Object d = ev.getPacketValue("d");
                        if(d != null && d.toString().equals("OFF_HAND")) {
                            es = EquipSlot.OFFHAND;
                        }
                    }
                    NPCInteractEvent e = new NPCInteractEvent(ev.getPlayer(), n,
                            ev.getPacketValue("action").toString().equals("ATTACK"), es);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    ev.setCancelled(true);
                    break;
                }
            }
        }
    }
}
