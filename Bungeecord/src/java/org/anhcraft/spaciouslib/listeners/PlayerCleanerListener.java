package org.anhcraft.spaciouslib.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.utils.TimedList;
import org.anhcraft.spaciouslib.utils.TimedMap;
import org.anhcraft.spaciouslib.utils.TimedSet;

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
    public void disconnect(PlayerDisconnectEvent event){
        BungeeCord.getInstance().getScheduler().runAsync(SpaciousLib.instance, () -> {
            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                for(Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(PlayerCleaner.class)) {
                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                        for(Object obj : x) {
                            a(field, obj, event.getPlayer().getUniqueId());
                        }
                    }
                }
            }
        });
    }

    private void a(Field field, Object obj, UUID uniqueId) {
        try {
            if(Collection.class.isAssignableFrom(field.getType())) {
                Collection v = (Collection) field.get(obj);
                v.remove(uniqueId);
            } else if(Map.class.isAssignableFrom(field.getType())) {
                Map<UUID, Object> v = (Map<UUID, Object>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedMap.class.isAssignableFrom(field.getType())) {
                TimedMap<UUID, Object> v = (TimedMap<UUID, Object>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedSet.class.isAssignableFrom(field.getType())) {
                TimedSet<UUID> v = (TimedSet<UUID>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedList.class.isAssignableFrom(field.getType())) {
                TimedList<UUID> v = (TimedList<UUID>) field.get(obj);
                v.remove(uniqueId);
            } else if(UUID.class.isAssignableFrom(field.getType())) {
                field.set(obj, null);
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
