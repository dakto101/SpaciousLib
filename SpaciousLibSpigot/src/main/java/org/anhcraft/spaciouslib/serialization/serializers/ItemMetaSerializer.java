package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ItemMetaSerializer extends DataType<ItemMeta> {
    public ItemMetaSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{ItemMeta.class};
    }

    @Override
    public ItemMeta read(DataInputStream in) throws IOException {
        try {
            Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".inventory.CraftMetaItem$SerializableMeta");
            return (ItemMeta) ReflectionUtils.getStaticMethod("deserialize", clazz, new Group<>(
                    new Class<?>[]{Map.class},
                    new Object[]{DataSerialization.lookupType(Map.class).read(in)}
            ));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(DataSerializerStream out, ItemMeta data) throws IOException {
        ExceptionThrower.ifNull(data, new IOException("The given item meta mustn't be null."));
        DataSerialization.lookupType(Map.class).write(out, data.serialize());
    }
}