package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;

public class ObjectArraySerializer extends DataType<Object[]> {
    public ObjectArraySerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class[0];
    }

    @Override
    public Object[] read(DataInputStream in) throws IOException {
        int length = in.readInt();
        if(length > 0){
            try {
                Class<?> clazz = Class.forName(in.readUTF());
                Object[] array = (Object[]) Array.newInstance(clazz, length);
                DataType type = DataSerialization.lookupType(clazz);
                if(type instanceof ObjectSerializer) {
                    for(int i = 0; i < length; i++) {
                        array[i] = DataSerialization.deserialize(clazz, in);
                    }
                } else {
                    for(int i = 0; i < length; i++) {
                        array[i] = type.read(in);
                    }
                }
                return array;
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Object[0];
    }

    @Override
    public void write(DataSerializerStream out, Object[] data) throws IOException {
        out.writeInt(data.length);
        if(data.length > 0) {
            out.writeUTF(data.getClass().getComponentType().getCanonicalName());
            for(Object d : data) {
                DataSerialization.lookupType(data.getClass().getComponentType()).write(out, d);
            }
        }
    }
}
