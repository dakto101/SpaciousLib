package org.anhcraft.spaciouslib.npc;

import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class NPCWrapper {
    protected List<Player> viewers = new ArrayList<>();
    protected NPC npc;
    protected boolean tabList = false;

    protected abstract void init();

    public abstract int getEntityID();

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
        return this.npc;
    }

    public void reload() {
        remove();
        init();
        addViewers(CommonUtils.toArray(this.viewers, Player.class));
        if(this.tabList){
            addTabList();
        }
    }

    public void remove() {
        removeViewers(CommonUtils.toArray(this.viewers, Player.class));
    }

    public void teleport(Location location) {
        this.npc.location = location;
        reload();
    }

    public void setName(String name) {
        this.npc.gameProfile = new GameProfileManager(this.npc.gameProfile).setName(name).getGameProfile();
        reload();
    }
}
