package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;
import org.bukkit.util.Vector;

import java.io.DataInputStream;
import java.io.IOException;

public class VectorSerializer extends DataType<Vector> {
    public VectorSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{Vector.class};
    }

    @Override
    public Vector read(DataInputStream in) throws IOException {
        return new Vector(in.readDouble(), in.readDouble(), in.readDouble());
    }

    @Override
    public void write(DataSerializerStream out, Vector data) throws IOException {
        ExceptionThrower.ifNull(data, new IOException("The given vector mustn't be null."));
        out.writeDouble(data.getX());
        out.writeDouble(data.getY());
        out.writeDouble(data.getZ());
    }
}