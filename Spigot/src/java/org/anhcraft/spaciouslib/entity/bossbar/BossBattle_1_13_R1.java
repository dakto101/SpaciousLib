package org.anhcraft.spaciouslib.entity.bossbar;

import net.minecraft.server.v1_13_R1.BossBattle;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;

import java.util.UUID;

public class BossBattle_1_13_R1 extends BossBattle {
    public BossBattle_1_13_R1(UUID uuid, IChatBaseComponent iChatBaseComponent, BarColor barColor, BarStyle barStyle) {
        super(uuid, iChatBaseComponent, barColor, barStyle);
    }
}
