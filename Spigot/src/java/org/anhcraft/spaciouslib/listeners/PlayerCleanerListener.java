package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A class helps you to remove an online player when his/her quits the game
 */
public class PlayerCleanerListener implements Listener {
    @EventHandler
    public void quit(PlayerQuitEvent event){
        try {
            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                for(Field field : clazz.getDeclaredFields()) {
                    if(field.isAnnotationPresent(PlayerCleaner.class)) {
                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                        for(Object obj : x) {
                            if(field.getType().isAssignableFrom(Collection.class)) {
                                Collection v = (Collection) field.get(obj);
                                v.remove(event.getPlayer().getUniqueId());
                                field.set(obj, v);
                            } else if(obj instanceof Map) {
                                Map<UUID, Object> v = (Map<UUID, Object>) field.get(obj);
                                v.remove(event.getPlayer().getUniqueId());
                                field.set(obj, v);
                            } else if(obj instanceof UUID) {
                                field.set(obj, null);
                            }
                        }
                    }
                }
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
