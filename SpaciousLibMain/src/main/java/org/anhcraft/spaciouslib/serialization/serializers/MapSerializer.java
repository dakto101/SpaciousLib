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
                DataType<Object> typeKey = DataSerialization.lookupType(in.readByte());
                DataType<Object> typeValue = DataSerialization.lookupType(in.readByte());
                map.put(typeKey.read(in), typeValue.read(in));
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
                out.writeByte(typeValue.getIdentifier());
                typeKey.write(out, obj.getKey());
                typeValue.write(out, obj.getValue());
            }
        }
    }
}
