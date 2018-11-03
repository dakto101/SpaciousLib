package org.anhcraft.spaciouslib.annotations;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class SpaciousAnnotation {
    /**
     * Load or reload all configuration files and set new field values.<br>
     * This method is for the annotation ConfigOption
     */
    public static void reloadConfigs(){
        HashMap<String, Configuration> config = new HashMap<>();
        try {
            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                for(Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(ConfigOption.class)) {
                        ConfigOption a = field.getAnnotation(ConfigOption.class);
                        if(!config.containsKey(a.file())){
                            config.put(a.file(), ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(a.file())));
                        }
                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                        for(Object obj : x) {
                            field.set(obj, config.get(a.file()).get(a.path()));
                        }
                    }
                }
            }
        } catch(IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load or reload the given configuration file and set new field values.<br>
     * This method is for the annotation ConfigOption
     * @param pathname the path of file
     */
    public static void reloadConfig(String pathname){
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(pathname));
            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                for(Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(ConfigOption.class)) {
                        ConfigOption a = field.getAnnotation(ConfigOption.class);
                        if(!a.file().equals(pathname)){
                            continue;
                        }
                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                        for(Object obj : x) {
                            field.set(obj, config.get(a.path()));
                        }
                    }
                }
            }
        } catch(IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load or reload the given configuration file and set new field values.<br>
     * This method is for the annotation ConfigOption
     * @param file file
     */
    public static void reloadConfig(File file){
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                for(Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(ConfigOption.class)) {
                        ConfigOption a = field.getAnnotation(ConfigOption.class);
                        if(!new File(a.file()).equals(file)){
                            continue;
                        }
                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                        for(Object obj : x) {
                            field.set(obj, config.get(a.path()));
                        }
                    }
                }
            }
        } catch(IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }
}
