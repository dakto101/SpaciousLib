package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapSerializer extends DataType<Map<Object, Object>> {
    public MapSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class<?>[]{Map.class};
    }

    @Override
    public Map<Object, Object> read(DataInputStream in) throws IOException {
        int size = in.readInt();
        if(size > 0) {
            HashMap<Object, Object> map = new HashMap<>(size);
            for(int i = 0; i < size; i++) {
                Object k = null;
                Object v = null;
                DataType<Object> typeKey = DataSerialization.lookupType(in.readByte());
                if(typeKey instanceof ObjectSerializer) {
                    try {
                        k = DataSerialization.deserialize(Class.forName(in.readUTF()), in);
                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    k = typeKey.read(in);
                }
                DataType<Object> typeValue = DataSerialization.lookupType(in.readByte());
                if(typeValue instanceof ObjectSerializer) {
                    try {
                        v = DataSerialization.deserialize(Class.forName(in.readUTF()), in);
                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    v = typeValue.read(in);
                }
                map.put(k, v);
            }
            return map;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public void write(DataSerializerStream out, Map<Object, Object> data) throws IOException {
        out.writeInt(data.size());
        if(data.size() > 0) {
            for(Map.Entry<Object, Object> obj : data.entrySet()) {
                DataType<Object> typeKey = DataSerialization.lookupType(obj.getKey().getClass());
                DataType<Object> typeValue = DataSerialization.lookupType(obj.getValue().getClass());
                out.writeByte(typeKey.getIdentifier());
                if(typeKey instanceof ObjectSerializer){
                    out.writeUTF(obj.getKey().getClass().getCanonicalName());
                }
                typeKey.write(out, obj.getKey());
                out.writeByte(typeValue.getIdentifier());
                if(typeValue instanceof ObjectSerializer){
                    out.writeUTF(obj.getValue().getClass().getCanonicalName());
                }
                typeValue.write(out, obj.getValue());
            }
        }
    }
}
