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
                Object[] array = (Object[]) Array.newInstance(Class.forName(in.readUTF()), length);
                for(int i = 0; i < length; i++) {
                    DataType<Object> type = DataSerialization.lookupType(in.readByte());
                    if(type instanceof ObjectSerializer) {
                        array[i] = DataSerialization.deserialize(Class.forName(in.readUTF()), in);
                    } else {
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
                DataType type = DataSerialization.lookupType(d.getClass());
                out.writeByte(type.getIdentifier());
                if(type instanceof ObjectSerializer){
                    out.writeUTF(d.getClass().getCanonicalName());
                }
                type.write(out, d);
            }
        }
    }
}
