package org.anhcraft.spaciouslib.tasks;

import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.mojang.CachedSkin;
import org.anhcraft.spaciouslib.mojang.SkinManager;
import org.anhcraft.spaciouslib.utils.GZipUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CachedSkinTask extends BukkitRunnable {
    @Override
    public void run() {
        try {
            for(CachedSkin skin : SkinManager.getSkins().values()){
                boolean save = false;
                if(skin.isExpired()){
                    SkinManager.renew(skin.getOwner());
                    save = true;
                }
                File file = SkinManager.getSkinFile(skin);
                if(save || !file.exists()) {
                    new FileManager(file).delete().initFile(GZipUtils.compress(SkinManager.toJSON(skin)
                            .getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
