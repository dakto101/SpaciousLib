package org.anhcraft.spaciouslib.anvil;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class Anvil_1_12_R1 extends AnvilWrapper {
   private class Container extends ContainerAnvil{
       Container(EntityHuman e) {
           super(e.inventory, e.world, new BlockPosition(0,0,0), e);
           this.checkReachable = false;
       }
   }

    private LinkedHashMap<Anvil.Slot, ItemStack> items = new LinkedHashMap<>();
    private EntityPlayer player;

    public Anvil_1_12_R1(Player player) {
        this.player = ((CraftPlayer) player).getHandle();
    }

    @Override
    public void open() {
        ContainerAnvil container = new Container(player);
        CraftInventoryView civ = container.getBukkitView();
        this.inv = civ.getTopInventory();
        for (Anvil.Slot slot : this.items.keySet()) {
            this.inv.setItem(slot.getID(), this.items.get(slot));
        }
        int id = this.player.nextContainerCounter();
        this.player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(id,
                "minecraft:anvil", new ChatMessage("tile.anvil.name"), 0));
        this.player.activeContainer = container;
        this.player.activeContainer.windowId = id;
        this.player.activeContainer.addSlotListener(this.player);
    }

    @Override
    public void setItem(Anvil.Slot slot, ItemStack item) {
        this.items.put(slot, item);
    }
}
