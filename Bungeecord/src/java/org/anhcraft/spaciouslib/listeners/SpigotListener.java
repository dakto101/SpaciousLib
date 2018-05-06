package org.anhcraft.spaciouslib.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.mojang.Skin;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class SpigotListener implements Listener {
    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {
        if (!ev.getTag().equals(SpaciousLib.CHANNEL)) {
            return;
        }
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
            DataInputStream data = new DataInputStream(stream);
            String sc = data.readUTF();
            switch(sc) {
                case "skin":
                    String player = data.readUTF();
                    String value = data.readUTF();
                    String signature = data.readUTF();
                    new PlayerManager(BungeeCord.getInstance().getPlayer(player))
                            .changeSkin(new Skin(value, signature));
                    break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
