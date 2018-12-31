package org.anhcraft.spaciouslib.serialization;

import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.annotations.Serializable;
import org.anhcraft.spaciouslib.serialization.serializers.*;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings(value = "unchecked")
public abstract class DataSerialization extends DataType {
    private static final int VERSION = 1;
    private static final HashMap<Class<?>, DataType> serializers = new HashMap<>();

    static {
        new ByteSerializer((byte) 0);
        new CharSerializer((byte) 1);
        new IntSerializer((byte) 2);
        new DoubleSerializer((byte) 3);
        new ShortSerializer((byte) 4);
        new FloatSerializer((byte) 5);
        new LongSerializer((byte) 6);
        new StringSerializer((byte) 7);
        new UUIDSerializer((byte) 8);
        new CollectionSerializer((byte) 9);
        new MapSerializer((byte) 10);
        new EnumSerializer((byte) 11);
        new BoolSerializer((byte) 12);

        // 30: ItemStack
        // 31: ItemMeta
        // 32: Location
        // 33: Vector
        // 34: NBTCompound

        serializers.put(boolean[].class, new PrimitiveArraySerializer((byte) 118, boolean.class));
        serializers.put(byte[].class, new PrimitiveArraySerializer((byte) 119, byte.class));
        serializers.put(char[].class, new PrimitiveArraySerializer((byte) 120, char.class));
        serializers.put(int[].class, new PrimitiveArraySerializer((byte) 121, int.class));
        serializers.put(double[].class, new PrimitiveArraySerializer((byte) 122, double.class));
        serializers.put(short[].class, new PrimitiveArraySerializer((byte) 123, short.class));
        serializers.put(float[].class, new PrimitiveArraySerializer((byte) 124, float.class));
        serializers.put(long[].class, new PrimitiveArraySerializer((byte) 125, long.class));
        serializers.put(Object[].class, new ObjectArraySerializer((byte) 126));
        new ObjectSerializer((byte) 127);
    }

    protected DataSerialization() {
        super((byte) -1);
    }

    /**
     * Look up the accordant serializer for the given class type
     * @param clazz class type
     * @return the serializer
     */
    public static DataType<Object> lookupType(Class<?> clazz){
        if(clazz.isArray()){
            return serializers.getOrDefault(clazz, typeLookupById[126]);
        }
        if(clazz.isEnum()){
            return typeLookupById[11];
        }
        for(Map.Entry<Class<?>, DataType> clasz : typeLookupByClass.entrySet()) {
            if(clasz.getKey().isAssignableFrom(clazz)) {
                return clasz.getValue();
            }
        }
        return typeLookupById[127];
    }

    /**
     * Look up the serializer from its ID
     * @param id the ID
     * @return the serializer
     */
    public static DataType<Object> lookupType(byte id){
        return typeLookupById[id];
    }

    /**
     * Deserialize the given data into a specific object
     * @param clazz the class type of the object
     * @param byteArray array of bytes
     * @return the object
     */
    public static <T> T deserialize(Class<T> clazz, byte[] byteArray) {
        return deserialize(clazz, new DataInputStream(new ByteArrayInputStream(byteArray)));
    }

    /**
     * Deserialize the given data into a specific object
     * @param clazz the class type of the object
     * @param inputStream an input stream
     * @return the object
     */
    public static <T> T deserialize(Class<T> clazz, InputStream inputStream) {
        T obj = null;
        try {
            DataInputStream in = new DataInputStream(inputStream);
            int v = in.readInt(); // version
            ExceptionThrower.ifFalse(v == VERSION, new Exception("Incorrect version. The process can't be started."));
            if(clazz.isAnnotationPresent(Serializable.class)) {
                Constructor<T> cons = clazz.getConstructor();
                cons.setAccessible(true);
                obj = cons.newInstance();
                int size = in.readInt();
                m:
                for(int i = 0; i < size; i++){
                    String name = in.readUTF();
                    DataType<Object> type = lookupType(in.readByte());
                    if(Arrays.stream(clazz.getDeclaredFields())
                            .anyMatch(f -> f.getName().equals(name))){
                        Field field = clazz.getDeclaredField(name);
                        field.setAccessible(true);
                        Object val = field.get(obj);
                        Class<?> fieldType = val == null ? field.getType() : val.getClass();
                        // if the deserialized object is a collection, to apply it on the field, we must convert it into its real object
                        if(Collection.class.isAssignableFrom(fieldType)
                                && fieldType.getConstructors().length > 0){
                            for(Constructor c : fieldType.getConstructors()) {
                                if(c.getParameterCount() == 1 &&
                                        c.getParameterTypes()[0].equals(Collection.class)) {
                                    field.set(obj, ReflectionUtils.getConstructor(fieldType,
                                            new Group<>(new Class<?>[]{Collection.class},
                                                    new Object[]{type.read(in)}
                                            )));
                                    continue m;
                                }
                            }
                        }
                        // same as above, if the deserialized object is a map, to apply it on the field, we must convert it into its real object
                        else if(Map.class.isAssignableFrom(fieldType)
                                && fieldType.getConstructors().length > 0) {
                            for(Constructor c : fieldType.getConstructors()) {
                                if(c.getParameterCount() == 1 &&
                                        c.getParameterTypes()[0].equals(Map.class)) {
                                    field.set(obj, ReflectionUtils.getConstructor(fieldType,
                                            new Group<>(new Class<?>[]{Map.class},
                                                    new Object[]{type.read(in)}
                                            )));
                                    continue m;
                                }
                            }
                        } else {
                            field.set(obj, type.read(in));
                        }
                    } else {
                        // if the field wasn't existed, we still have to read the data to against conflict
                        type.read(in);
                    }
                }
            }
            in.close();
        } catch(IOException | InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Serialize the given object
     * @param clazz the class type of the object
     * @param obj the object
     * @return a group of data and the working log
     */
    public static Group<byte[], String> serialize(Class<?> clazz, Object obj) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            DataSerializerStream out = serialize(clazz, obj, new DataSerializerStream(byteStream));
            out.flush();
            byteStream.flush();
            byteStream.close();
            return new Group<>(byteStream.toByteArray(), out.getLog());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new Group<>(byteStream.toByteArray(), null);
    }

    /**
     * Serialize the given object, then append the data into the existing data stream
     * @param clazz the class type of the object
     * @param obj the object
     * @param out data stream
     * @return current data stream
     */
    public static DataSerializerStream serialize(Class<?> clazz, Object obj, DataSerializerStream out) {
        try {
            out.writeInt(VERSION);
            if(clazz.isAnnotationPresent(Serializable.class)) {
                List<Field> fields = CommonUtils.toList(clazz.getDeclaredFields()).stream().filter(field -> !Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(DataField.class)).collect(Collectors.toList());
                out.writeInt(fields.size());
                for(Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    DataType<Object> type = value == null ? lookupType(
                            field.getType()) : lookupType(value.getClass());
                    out.writeUTF(field.getName());
                    out.writeByte(type.getIdentifier());
                    if(type instanceof ObjectSerializer){
                        out.writeUTF(value == null ? field.getType()
                                .getName() : value.getClass().getName());
                    }
                    type.write(out, field.get(obj));
                }
            }
        } catch(IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return out;
    }
}
