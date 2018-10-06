package org.anhcraft.spaciouslib.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public abstract class DataType<T> {
    private byte id;

    public byte getIdentifier(){
        return id;
    }

    public abstract Class<?>[] getClazz();
    public abstract T read(DataInputStream in) throws IOException;
    public abstract void write(DataSerializerStream out, T data) throws IOException;

    static LinkedHashMap<Class<?>, DataType> typeLookupByClass = new LinkedHashMap<>();
    static DataType[] typeLookupById = new DataType[128];

    protected DataType(byte id) {
        this.id = id;
        typeLookupById[id] = this;
        for(Class<?> clazz : getClazz()) {
            typeLookupByClass.put(clazz, this);
        }
    }
}
