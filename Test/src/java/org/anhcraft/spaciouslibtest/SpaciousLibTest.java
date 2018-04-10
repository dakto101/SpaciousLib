package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslibtest.tests.DatabaseTest;
import org.anhcraft.spaciouslibtest.tests.MojangAPITest;
import org.anhcraft.spaciouslibtest.tests.SocketTest;
import org.anhcraft.spaciouslibtest.tests.TimedMapTest;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SpaciousLibTest extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        try {
            SocketTest.Client();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new MojangAPITest();
        new DatabaseTest();
        new TimedMapTest();
        SocketTest.Server();
    }
}