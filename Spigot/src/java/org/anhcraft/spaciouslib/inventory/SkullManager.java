package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTLoader;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A manage helps you to manage skulls.
 */
public class SkullManager extends ItemManager {
    public SkullManager(ItemStack skull) {
        super(skull);
        if(skull != null){
            return;
        } else {
            if(GameVersion.is1_13Above()){
                if(InventoryUtils.getSkulls().contains(skull.getType())){
                    return;
                }
            } else {
                if(skull.getType().equals(Material.valueOf("SKULL_ITEM"))){
                    return;
                }
            }
        }
        try {
            throw new Exception("The item must be a skull");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new SkullManager instance
     * @param skull the type of this skull
     */
    public SkullManager(SkullType skull) {
        super(new ItemStack(GameVersion.is1_13Above() ? Material.PLAYER_HEAD : Material
                .valueOf("SKULL_ITEM"), 1));
        if(GameVersion.is1_13Above()) {
            return;
        }
        byte data = -1;
        switch(skull){
            case DRAGON:
                data = 5;
                break;
            case CREEPER:
                data = 4;
                break;
            case PLAYER:
                data = 3;
                break;
            case ZOMBIE:
                data = 2;
                break;
            case WITHER:
                data = 1;
                break;
            case SKELETON:
                data = 0;
                break;
        }
        this.item.setDurability(data);
    }

    /**
     * Creates a player skull with skin
     * @param skin the skin for this skull
     */
    public SkullManager(Skin skin) {
        super(new ItemStack(GameVersion.is1_13Above() ? Material.PLAYER_HEAD : Material
                .valueOf("SKULL_ITEM"), 1));
        if(!GameVersion.is1_13Above()) {
            this.item.setDurability((byte) 3);
        }
        setSkin(skin);
    }

    /**
     * Sets the skin for this this skull.<br>
     * Only works with a player skull
     * @param skin the skin
     */
    public void setSkin(Skin skin){
        NBTCompound root = NBTLoader.fromItem(this.item);
        NBTCompound skullOwner = NBTLoader.create();
        if(root.hasKey("SkullOwner")){
            skullOwner = root.getCompound("SkullOwner");
        }
        if(!skullOwner.hasKey("Id")){
            skullOwner = skullOwner.set("Id", UUID.randomUUID().toString());
        }
        NBTCompound properties = NBTLoader.create();
        if(skullOwner.hasKey("Properties")){
            properties = skullOwner.getCompound("Properties");
        }
        List<NBTCompound> textures = new ArrayList<>();
        textures.add(NBTLoader.create().set("Value", skin.getValue())
                .set("Signature", skin.getSignature()));
        properties = properties.setList("textures", textures);
        skullOwner = skullOwner.setCompound("Properties", properties);
        this.item = root.setCompound("SkullOwner", skullOwner).toItem(this.item);
    }

    /**
     * Gets the skin of this skull.<br>
     * Only works with a player skull
     * @return the skin
     */
    public Skin getSkin(){
        NBTCompound root = NBTLoader.fromItem(this.item);
        NBTCompound skullOwner = NBTLoader.create();
        if(root.hasKey("SkullOwner")){
            skullOwner = root.getCompound("SkullOwner");
        }
        if(!skullOwner.hasKey("Id")){
            skullOwner = skullOwner.set("Id", UUID.randomUUID().toString());
        }
        NBTCompound properties = NBTLoader.create();
        if(skullOwner.hasKey("Properties")){
            properties = skullOwner.getCompound("Properties");
        }
        if(properties.hasKey("textures")) {
            List<NBTCompound> textures = properties.getList("textures");
            for(NBTCompound t : textures){
                if(t.hasKey("Value") && t.hasKey("Signature")){
                    return new Skin(t.getString("Value"), t.getString("Signature"));
                }
            }
        }
        return null;
    }
}