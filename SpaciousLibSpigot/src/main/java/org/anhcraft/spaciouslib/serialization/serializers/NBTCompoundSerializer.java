package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTLoader;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class NBTCompoundSerializer extends DataType<NBTCompound> {
    public NBTCompoundSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{NBTCompound.class};
    }

    @Override
    public NBTCompound read(DataInputStream in) throws IOException {
        NBTCompound c = NBTLoader.create();
        c.getTags().putAll((Map<? extends String, ?>) DataSerialization.lookupType(Map.class).read(in));
        return c;
    }

    @Override
    public void write(DataSerializerStream out, NBTCompound data) throws IOException {
        ExceptionThrower.ifNull(data, new IOException("The given nbt compound mustn't be null."));
        DataSerialization.lookupType(Map.class).write(out, data.getTags());
    }
}