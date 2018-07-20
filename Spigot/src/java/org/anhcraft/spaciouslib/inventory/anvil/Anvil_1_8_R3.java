package org.anhcraft.spaciouslib.inventory.anvil;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.entity.Player;

public class Anvil_1_8_R3 extends AnvilWrapper<Anvil_1_8_R3> {
   private class Container extends ContainerAnvil {
        Container(EntityHuman e) {
            super(e.inventory, e.world, new BlockPosition(0,0,0), e);
        }

        @Override
        public boolean a(EntityHuman e) {
            return true;
        }
    }
    private EntityPlayer player;

    public Anvil_1_8_R3(Player player) {
        this.player = ((CraftPlayer) player).getHandle();
    }

    @Override
    public Anvil_1_8_R3 open() {
        ContainerAnvil container = new Container(player);
        CraftInventoryView civ = container.getBukkitView();
        this.inv = civ.getTopInventory();
        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getId(), this.items.get(slot));
        }
        int id = this.player.nextContainerCounter();
        this.player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(id,
                "minecraft:anvil", new ChatMessage("Repairing"), 0));
        this.player.activeContainer = container;
        this.player.activeContainer.windowId = id;
        this.player.activeContainer.addSlotListener(this.player);
        return this;
    }
}
