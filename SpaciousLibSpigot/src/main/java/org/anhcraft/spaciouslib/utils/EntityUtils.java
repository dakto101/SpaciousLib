package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTLoader;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityUtils {
    public static void setAttribute(Entity entity, Attribute attribute){
        NBTCompound c = NBTLoader.fromEntity(entity);
        List<NBTCompound> newAttrs = new ArrayList<>();
        if(c.hasKey("Attributes")){
            List<NBTCompound> oldAttrs = c.getList("Attributes");
            for(NBTCompound a : oldAttrs){
                if(!a.getString("Name").equals(attribute.getType().getId())){
                    newAttrs.add(a);
                }
            }
        }
        List<NBTCompound> modifiers = new ArrayList<>();
        for(AttributeModifier modifier : attribute.getModifiers()){
            modifiers.add(NBTLoader.create()
                    .set("Name", modifier.getName())
                    .set("Amount", modifier.getAmount())
                    .set("Operation", modifier.getOperation().getId())
                    .set("UUIDMost", modifier.getUniqueId().getMostSignificantBits())
                    .set("UUIDLeast", modifier.getUniqueId().getLeastSignificantBits()));
        }
        newAttrs.add(NBTLoader.create().setString("Name", attribute.getType().getId())
                .setDouble("Base", attribute.getType().getBaseValue())
                .setList("Modifiers", modifiers));
        c.setList("Attributes", newAttrs).toEntity(entity);
    }

    public static List<Attribute> getAttributes(Entity entity){
        NBTCompound c = NBTLoader.fromEntity(entity);
        if(!c.hasKey("Attributes")){
            return new ArrayList<>();
        }
        List<Attribute> attrs = new ArrayList<>();
        List<NBTCompound> nbtattrs = NBTLoader.fromEntity(entity).getList("Attributes");
        for(NBTCompound a : nbtattrs){
            Attribute attr = new Attribute(Attribute.Type.getById(a.getString("Name")));
            List<NBTCompound> nbtmodifiers = a.getList("Modifiers");
            if(nbtmodifiers != null) {
                for(NBTCompound modifier : nbtmodifiers) {
                    attr.addModifier(new AttributeModifier(new UUID(modifier.getLong("UUIDMost"), modifier.getLong("UUIDLeast")), modifier.getString("Name"), modifier.getDouble("Amount"), AttributeModifier.Operation.getById(modifier.getInt("Operation"))));
                }
            }
            attrs.add(attr);
        }
        return attrs;
    }
}
