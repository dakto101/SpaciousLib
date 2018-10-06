package org.anhcraft.spaciouslib.serialization.serializers;

import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.DataSerializerStream;
import org.anhcraft.spaciouslib.serialization.DataType;
import org.anhcraft.spaciouslib.serialization.Serializable;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;

import java.io.DataInputStream;
import java.io.IOException;

public class ObjectSerializer extends DataType<Object> {
    public ObjectSerializer(byte id) {
        super(id);
    }

    @Override
    public Class<?>[] getClazz() {
        return new Class[0];
    }

    @Override
    public Object read(DataInputStream in) throws IOException {
        String c = in.readUTF();
        try {
            return DataSerialization.deserialize(Class.forName(c), in);
        } catch(ClassNotFoundException e) {
            throw new IOException("Couldn't find the serialization class ("+c+")");
        }
    }

    @Override
    public void write(DataSerializerStream out, Object data) throws IOException {
        ExceptionThrower.ifNull(data, new IOException("The given object mustn't be null."));
        ExceptionThrower.ifFalse(data.getClass().isAnnotationPresent(Serializable.class), new IOException("The given object couldn't be serialized due to class ("+data.getClass().getCanonicalName()+") wasn't serializable."));
        DataSerialization.serialize(data.getClass(), data, out);
    }
}
