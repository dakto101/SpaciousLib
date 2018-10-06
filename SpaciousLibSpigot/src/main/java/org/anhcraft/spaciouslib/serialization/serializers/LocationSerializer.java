package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.DataInputStream;
import java.io.IOException;

public class LocationSerializer extends DataType<Location> {
    public LocationSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{Location.class};
    }

    @Override
    public Location read(DataInputStream in) throws IOException {
        return new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
    }

    @Override
    public void write(DataSerializerStream out, Location data) throws IOException {
        ExceptionThrower.ifNull(data, new IOException("The given location mustn't be null."));
        out.writeUTF(data.getWorld().getName());
        out.writeDouble(data.getX());
        out.writeDouble(data.getY());
        out.writeDouble(data.getZ());
        out.writeFloat(data.getYaw());
        out.writeFloat(data.getPitch());
    }
}