package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ItemStackSerializer extends DataType<ItemStack> {
    public ItemStackSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{ItemStack.class};
    }

    @Override
    public ItemStack read(DataInputStream in) throws IOException {
        return ItemStack.deserialize((Map<String, Object>) DataSerialization.lookupType(Map.class).read(in));
    }

    @Override
    public void write(DataSerializerStream out, ItemStack data) throws IOException {
        data = data == null ? new ItemStack(Material.AIR) : data;
        DataSerialization.lookupType(Map.class).write(out, data.serialize());
    }
}