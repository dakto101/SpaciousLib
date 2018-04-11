package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage recipes
 */
public class RecipeManager {
    private Recipe recipe;

    /**
     * Creates new RecipeManager isntance
     * @param recipe the recipe
     */
    public RecipeManager(Recipe recipe){
        this.recipe = recipe;
    }

    /**
     * Registers the specified recipe
     */
    public void register(){
        Bukkit.getServer().addRecipe(recipe);
    }

    /**
     * Unregisters the specified recipe
     */
    public void unregister(){
        try {
            Class craftingManagerClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".CraftingManager");
            Class recipeClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".IRecipe");
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", craftingManagerClass);
            Method nmsRecipesMethod = craftingManagerClass.getDeclaredMethod("getRecipes");
            List<Object> newNmsRecipes = new ArrayList<>();
            List<Object> nmsRecipes = (List<Object>) nmsRecipesMethod.invoke(craftingManager);
            for(Object nr : nmsRecipes){
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", recipeClass, nr);
                if(compare(recipeBukkit)){
                    continue;
                }
                newNmsRecipes.add(nr);
            }
            ReflectionUtils.setField("recipes", craftingManagerClass, craftingManager, newNmsRecipes);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks is the specified recipe registered
     * @return true if yes
     */
    public boolean isRegistered(){
        try {
            Class<?> craftingManagerClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".CraftingManager");
            Class<?> recipeClass = Class.forName("net.minecraft.server."+ GameVersion.getVersion().toString()+".IRecipe");
            Object craftingManager = ReflectionUtils.getStaticMethod("getInstance", craftingManagerClass);
            List<Object> nmsRecipes = (List<Object>) ReflectionUtils.getMethod("getRecipes", craftingManagerClass, craftingManager);
            for(Object nr : nmsRecipes){
                Recipe recipeBukkit = (Recipe) ReflectionUtils.getMethod("toBukkitRecipe", recipeClass, nr);
                if(compare(recipeBukkit)){
                    return true;
                }
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compares the specified recipe with another recipe
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
            if(a.getExperience() == b.getExperience()
                    && InventoryUtils.compare(a.getInput(), b.getInput())
                    && InventoryUtils.compare(a.getResult(), b.getResult())){
                return true;
            }
        }
        return false;
    }
}