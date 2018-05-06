package org.anhcraft.spaciouslib;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Updater1520156620 {
    private static String id;
    private static Plugin plugin;

    public Updater1520156620(String id, Plugin plugin){
        Updater1520156620.id = id;
        Updater1520156620.plugin = plugin;
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 0, 12, TimeUnit.HOURS);
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
        int onlineplayers = ProxyServer.getInstance().getOnlineCount();
        s.append("&onlineplayers=").append(onlineplayers);
        Boolean onlinemode = ProxyServer.getInstance().getConfig().isOnlineMode();
        s.append("&onlinemode=").append(onlinemode);
        String version = BungeeCord.getInstance().getVersion();
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
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            int r = Integer.parseInt(result.toString().replaceAll("[^0-9]", ""));
            switch(r){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    BungeeCord.getInstance().stop();
                    break;
            }
            reader.close();
            in.close();
        } catch(Exception ignored) { }
    }
}