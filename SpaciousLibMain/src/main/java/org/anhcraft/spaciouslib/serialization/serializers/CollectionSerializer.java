package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionSerializer extends DataType<Collection<Object>> {
    public CollectionSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{Collection.class};
    }

    @Override
    public Collection<Object> read(DataInputStream in) throws IOException {
        int size = in.readInt();
        if(size > 0) {
            List<Object> list = new ArrayList<>(size);
            for(int i = 0; i < size; i++) {
                DataType<Object> type = DataSerialization.lookupType(in.readByte());
                if(type instanceof ObjectSerializer) {
                    try {
                        Class<?> clazz = Class.forName(in.readUTF());
                        list.add(DataSerialization.deserialize(clazz, in));
                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    list.add(type.read(in));
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void write(DataSerializerStream out, Collection<Object> data) throws IOException {
        out.writeInt(data.size());
        if(data.size() > 0) {
            for(Object obj : data) {
                DataType<Object> type = DataSerialization.lookupType(obj.getClass());
                out.writeByte(type.getIdentifier());
                if(type instanceof ObjectSerializer){
                    out.writeUTF(obj.getClass().getCanonicalName());
                }
                type.write(out, obj);
            }
        }
    }
}