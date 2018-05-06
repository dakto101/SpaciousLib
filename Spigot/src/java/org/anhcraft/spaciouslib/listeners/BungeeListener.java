package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player x, byte[] bytes) {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            if(s.equals(SpaciousLib.CHANNEL)){
                String sc = data.readUTF();
                switch(sc) {
                    case "skin":
                        String player = data.readUTF();
                        String value = data.readUTF();
                        String signature = data.readUTF();
                        new PlayerManager(Bukkit.getServer().getPlayer(player))
                                .changeSkin(new Skin(value, signature));
                        break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
