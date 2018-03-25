package org.anhcraft.spaciouslib.anvil;

import net.minecraft.server.v1_12_R1.*;
import org.anhcraft.spaciouslib.compatibility.CompatibilityInventoryClickEvent;
import org.anhcraft.spaciouslib.events.AnvilClickEvent;
import org.anhcraft.spaciouslib.events.AnvilCloseEvent;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class Anvil_1_12_R1 implements AnvilWrapper {
    public class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0,0,0), entity);
        }
        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    private Player p;
    private ContainerAnvil container;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    private Inventory inv;
    private Listener listener;
    private EntityHuman human;
    private EntityPlayer ep;
    private String id;

    public Anvil_1_12_R1(Player p, String id){
        this.p = p;
        this.id = id;
        CraftPlayer cp = (CraftPlayer) p;
        this.human = cp.getHandle();
        this.ep = (EntityPlayer) human;
        AnvilWrapper anvil = this;
        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event){
                if ((event.getWhoClicked() instanceof Player)) {
                    if (CompatibilityInventoryClickEvent.getInventory(event) != null && CompatibilityInventoryClickEvent.getInventory(event)
                            .equals(inv)){
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";
                        if ((item != null) &&
                                (item.hasItemMeta())){
                            ItemMeta meta = item.getItemMeta();
                            if (meta.hasDisplayName()) {
                                name = meta.getDisplayName();
                            }
                        }
                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name,
                                (Player) event
                                .getWhoClicked(),
                                anvil);
                        Bukkit.getServer().getPluginManager().callEvent(clickEvent);
                        if (clickEvent.isDestory()) {
                            event.getWhoClicked().closeInventory();
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if ((event.getPlayer() instanceof Player)) {
                    Inventory i = event.getInventory();
                    if (i != null && i.equals(inv)){
                        p.setLevel(p.getLevel()-1);
						AnvilCloseEvent clickEvent = new AnvilCloseEvent(i.getTitle(),
								(Player) event.getPlayer(),
                                anvil);
                        Bukkit.getServer().getPluginManager().callEvent(clickEvent);
                        i.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(p)){
                    p.setLevel(p.getLevel()-1);
                    destroy();
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(this.listener, SpaciousLib.instance);
    }

    @Override
    public void setSlot(AnvilSlot slot, ItemStack item){
        this.items.put(slot, item);
    }

    @Override
    public String getAnvilID() {
        return id;
    }

    public void destroy(){
        this.p = null;
        this.items = new HashMap<>();
        HandlerList.unregisterAll(this.listener);
        this.listener = null;
    }

    @Override
    public void open() {
        p.setLevel(p.getLevel()+1);
        container = new AnvilContainer(human);
        CraftInventoryView civ = container.getBukkitView();
        this.inv = civ.getTopInventory();
        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getSlot(), this.items.get(slot));
        }
        int id = ep.nextContainerCounter();
        ep.playerConnection.sendPacket(new PacketPlayOutOpenWindow(id,
                "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));
        ep.activeContainer = container;
        ep.activeContainer.windowId = id;
        ep.activeContainer.addSlotListener(ep);
    }
}
