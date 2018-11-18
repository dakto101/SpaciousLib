package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class helps you to manage recipes
 */
public class RecipeManager {
    private Recipe recipe;

    /**
     * Creates new RecipeManager instance
     * @param recipe a recipe
     */
    public RecipeManager(Recipe recipe){
        this.recipe = recipe;
    }

    /**
     * Registers that recipe
     */
    public void register(){
        Bukkit.getServer().addRecipe(recipe);
    }

    /**
     * Unregisters that recipe
     */
    public void unregister(){
        if(GameVersion.is1_13Above()) {
            Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
            Object nmsServer = ReflectionUtils.getMethod("getServer", ClassFinder.CB.CraftServer, craftServer);
            Object craftingManager = ReflectionUtils.getField("ag", ClassFinder.NMS.MinecraftServer, nmsServer);
            Map<?, ?> recipes = (Map<?, ?>) ReflectionUtils
                    .getField("recipes", ClassFinder.NMS.CraftingManager, craftingManager);
            Object k = null;
            for(Object key : recipes.keySet()) {
                Object rcp = recipes.get(key);
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, rcp);
                if(compare(recipeBukkit)) {
                    k = key;
                    break;
                }
            }
            if(k != null){
                recipes.remove(k);
            }
        } else if(GameVersion.is1_12Above()) {
            Object registryMaterials = ReflectionUtils.getStaticField("recipes", ClassFinder.NMS.CraftingManager);
            List<Recipe> recipes = new ArrayList<>();
            Iterator<Object> iterator = (Iterator<Object>) ReflectionUtils.getMethod("iterator", ClassFinder.NMS.RegistryMaterials, registryMaterials);
            for(; iterator.hasNext(); ) {
                Object rcp = iterator.next();
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, rcp);
                if(!compare(recipeBukkit)) {
                    recipes.add(recipeBukkit);
                }
            }
            Bukkit.getServer().clearRecipes();
            for(Recipe rc : recipes){
                Bukkit.getServer().addRecipe(rc);
            }
        } else {
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", ClassFinder.NMS.CraftingManager);
            List<Object> newNmsRecipes = new ArrayList<>();
            List<Object> nmsRecipes = (List<Object>) ReflectionUtils.getMethod("getRecipes", ClassFinder.NMS.CraftingManager, craftingManager);
            for(Object nr : nmsRecipes) {
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, nr);
                if(compare(recipeBukkit)) {
                    continue;
                }
                newNmsRecipes.add(nr);
            }
            ReflectionUtils.setField("recipes", ClassFinder.NMS.CraftingManager, craftingManager, newNmsRecipes);
        }
    }

    /**
     * Checks is that recipe registered
     * @return true if yes
     */
    public boolean isRegistered(){
        if(GameVersion.is1_13Above()) {
            Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
            Object nmsServer = ReflectionUtils.getMethod("getServer", ClassFinder.CB.CraftServer, craftServer);
            Object craftingManager = ReflectionUtils.getField("ag", ClassFinder.NMS.MinecraftServer, nmsServer);
            Map<?, ?> recipes = (Map<?, ?>) ReflectionUtils
                    .getField("recipes", ClassFinder.NMS.CraftingManager, craftingManager);
            for(Object key : recipes.keySet()) {
                Object rcp = recipes.get(key);
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, rcp);
                if(compare(recipeBukkit)) {
                    return true;
                }
            }
        } else if(GameVersion.is1_12Above()) {
            Object registryMaterials = ReflectionUtils.getStaticField("recipes", ClassFinder.NMS.CraftingManager);
            Iterator<Object> iterator = (Iterator<Object>) ReflectionUtils.getMethod("iterator", ClassFinder.NMS.RegistryMaterials, registryMaterials);
            for(; iterator.hasNext(); ) {
                Object rcp = iterator.next();
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, rcp);
                if(compare(recipeBukkit)) {
                    return true;
                }
            }
        } else {
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", ClassFinder.NMS.CraftingManager);
            List<Object> nmsRecipes = (List<Object>) ReflectionUtils.getMethod("getRecipes", ClassFinder.NMS.CraftingManager, craftingManager);
            for(Object nr : nmsRecipes) {
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", ClassFinder.NMS.IRecipe, nr);
                if(compare(recipeBukkit)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compares that recipe with another recipe
     * @param otherRecipe another recipe
     * @return true if these recipes are same
     */
    public boolean compare(Recipe otherRecipe){
        if(recipe instanceof ShapedRecipe && otherRecipe instanceof ShapedRecipe) {
            ShapedRecipe a = (ShapedRecipe) recipe;
            ShapedRecipe b = (ShapedRecipe) otherRecipe;
            if(InventoryUtils.compare(a.getResult(), b.getResult())){
                List<ItemStack> ai = new ArrayList<>();
                List<ItemStack> bi = new ArrayList<>();
                for(String as : a.getShape()){
                    for(char ac : as.toCharArray()){
                        ai.add(a.getIngredientMap().get(ac));
                    }
                }
                for(String bs : b.getShape()){
                    for(char bc : bs.toCharArray()){
                        bi.add(b.getIngredientMap().get(bc));
                    }
                }
                return InventoryUtils.compare(ai, bi);
            }
        }
        if(recipe instanceof ShapelessRecipe && otherRecipe instanceof ShapelessRecipe){
            ShapelessRecipe a = (ShapelessRecipe) recipe;
            ShapelessRecipe b = (ShapelessRecipe) otherRecipe;
            if(InventoryUtils.compare(a.getResult(), b.getResult())){
                return InventoryUtils.compare(a.getIngredientList(), b.getIngredientList());
            }
        }
        if(recipe instanceof FurnaceRecipe && otherRecipe instanceof FurnaceRecipe){
            FurnaceRecipe a = (FurnaceRecipe) recipe;
            FurnaceRecipe b = (FurnaceRecipe) otherRecipe;
            return a.getExperience() == b.getExperience()
                    && InventoryUtils.compare(a.getInput(), b.getInput())
                    && InventoryUtils.compare(a.getResult(), b.getResult());
        }
        return false;
    }
}