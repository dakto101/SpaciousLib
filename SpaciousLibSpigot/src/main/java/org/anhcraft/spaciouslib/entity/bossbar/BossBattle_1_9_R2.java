package org.anhcraft.spaciouslib.entity.bossbar;

import net.minecraft.server.v1_9_R2.BossBattle;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

import java.util.UUID;

public class BossBattle_1_9_R2 extends BossBattle {
    public BossBattle_1_9_R2(UUID uuid, IChatBaseComponent iChatBaseComponent, BarColor barColor, BarStyle barStyle) {
        super(uuid, iChatBaseComponent, barColor, barStyle);
    }
}
