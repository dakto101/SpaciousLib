package org.anhcraft.spaciouslib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater1520156620 {
    private static String id;
    private static Plugin plugin;

    public Updater1520156620(String id, Plugin plugin){
        Updater1520156620.id = id;
        Updater1520156620.plugin = plugin;
        new BukkitRunnable() {
            @Override
            public void run() {
                start();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 864000);
    }

    private static void start(){
        StringBuilder s = new StringBuilder();
        s.append("https://update.stats.anhcraft.org/?id=").append(id).append("&version=").append(
                plugin.getDescription().getVersion());
        long maxram = Runtime.getRuntime().maxMemory() / 102400000;
        s.append("&maxram=").append(maxram);
        long freeram = Runtime.getRuntime().freeMemory() / 102400000;
        s.append("&freeram=").append(freeram);
        long totalram = Runtime.getRuntime().totalMemory() / 102400000;
        s.append("&totalram=").append(totalram);
        long processor = Runtime.getRuntime().availableProcessors();
        s.append("&processor=").append(processor);
        String os = System.getProperty("os.name").replace(" ", "");
        s.append("&os=").append(os);
        String arch = System.getProperty("os.arch").replace(" ", "");
        s.append("&arch=").append(arch);
        int onlineplayers = Bukkit.getServer().getOnlinePlayers().size();
        s.append("&onlineplayers=").append(onlineplayers);
        Boolean onlinemode = Bukkit.getServer().getOnlineMode();
        s.append("&onlinemode=").append(onlinemode);
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        s.append("&gameversion=").append(version);
        try {
            URL url = new URL(s.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/51.0.2704.103 Safari/537.36");
            conn.setConnectTimeout(2800);
            conn.setReadTimeout(2000);
            conn.connect();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            reader.close();
            in.close();
        } catch(Exception ignored) { }
    }
}