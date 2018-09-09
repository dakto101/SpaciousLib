package org.anhcraft.spaciouslib.inventory.anvil;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftInventoryView;
import org.bukkit.entity.Player;

public class Anvil_1_13_R2 extends AnvilWrapper<Anvil_1_13_R2> {
   private class Container extends ContainerAnvil{
       Container(EntityHuman e) {
           super(e.inventory, e.world, new BlockPosition(0,0,0), e);
           this.checkReachable = false;
       }
   }
    private EntityPlayer player;

    public Anvil_1_13_R2(Player player) {
        this.player = ((CraftPlayer) player).getHandle();
    }

    @Override
    public Anvil_1_13_R2 open() {
        ContainerAnvil container = new Container(player);
        CraftInventoryView civ = container.getBukkitView();
        this.inv = civ.getTopInventory();
        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getId(), this.items.get(slot));
        }
        int id = this.player.nextContainerCounter();
        this.player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(id,
                "minecraft:anvil", new ChatMessage("tile.anvil.name"), 0));
        this.player.activeContainer = container;
        this.player.activeContainer.windowId = id;
        this.player.activeContainer.addSlotListener(this.player);
        return this;
    }
}
