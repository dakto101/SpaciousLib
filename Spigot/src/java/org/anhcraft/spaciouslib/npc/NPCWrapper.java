package org.anhcraft.spaciouslib.npc;

import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class NPCWrapper {
    protected List<Player> viewers = new ArrayList<>();
    protected NPC npc;

    public abstract int getEntityID();

    public abstract void rotate(double yaw, double pitch);

    public abstract void setCustomName(String name, boolean show);

    public abstract void addViewer(Player player);

    public abstract void addViewers(Player... player);

    public abstract void removeViewer(Player player);

    public abstract void removeViewers(Player... player);

    public List<Player> getViewers(){
        return this.viewers;
    }

    public abstract void addTabList();

    public abstract void removeTabList();

    public NPC getNPC(){
        return npc;
    }

    public void despawn() {
        removeViewers(CommonUtils.toArray(viewers, Player.class));
        removeTabList();
        viewers = new ArrayList<>();
    }
}
