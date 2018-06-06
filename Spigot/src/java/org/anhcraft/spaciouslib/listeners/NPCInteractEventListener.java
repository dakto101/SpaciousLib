package org.anhcraft.spaciouslib.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.entity.NPC;
import org.anhcraft.spaciouslib.events.NPCInteractEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class NPCInteractEventListener{
    public static List<NPC> data = new ArrayList<>();

    public NPCInteractEventListener(){
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(SpaciousLib.instance, ListenerPriority.NORMAL,
                        PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType() ==
                                PacketType.Play.Client.USE_ENTITY) {
                            for(NPC n : data){
                                if(n.getEntityID() == event.getPacket().getIntegers().read(0)){
                                    NPCInteractEvent.Action action = NPCInteractEvent.Action.valueOf(
                                            event.getPacket().getEntityUseActions()
                                                    .read(0).toString().toUpperCase());
                                    EquipSlot es = EquipSlot.MAINHAND;
                                    if(!action.equals(NPCInteractEvent.Action.ATTACK) && GameVersion.is1_9Above()){
                                        EnumWrappers.Hand h = event.getPacket().getHands().read(0);
                                        if(h != null && h.equals(EnumWrappers.Hand.OFF_HAND)) {
                                            es = EquipSlot.OFFHAND;
                                        }
                                    }
                                    NPCInteractEvent e = new NPCInteractEvent(event.getPlayer(), n, action, es);
                                    Bukkit.getServer().getPluginManager().callEvent(e);
                                    event.setCancelled(true);
                                    break;
                                }
                            }
                        }
                    }
                });
    }
}
