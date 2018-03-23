package org.anhcraft.spaciouslib.nbt;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NBTCompound_1_12_R1 extends NBTCompoundWrapper {
    private LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    private NBTCompoundWrapper decode(NBTTagCompound c){
        NBTCompoundWrapper w = new NBTCompound_1_12_R1();
        if (c != null) {
            for(Object o : c.c()){
                String s = o.toString();
                if(c.get(s) instanceof NBTTagByte){
                    byte b = c.getByte(s);
                    if(b ==  ((byte) 0)){
                        map.put(s, false);
                    } else if(b == ((byte) 1)){
                        map.put(s, true);
                    } else {
                        map.put(s, b);
                    }
                }
                else if(c.get(s) instanceof NBTTagShort) {
                    map.put(s, c.getShort(s));
                }
                else if(c.get(s) instanceof NBTTagInt) {
                    map.put(s, c.getInt(s));
                }
                else if(c.get(s) instanceof NBTTagLong) {
                    map.put(s, c.getLong(s));
                }
                else if(c.get(s) instanceof NBTTagFloat) {
                    map.put(s, c.getFloat(s));
                }
                else if(c.get(s) instanceof NBTTagDouble) {
                    map.put(s, c.getDouble(s));
                }
                else if(c.get(s) instanceof NBTTagByteArray) {
                    map.put(s, c.getByteArray(s));
                }
                else if(c.get(s) instanceof NBTTagIntArray) {
                    map.put(s, c.getIntArray(s));
                }
                else if(c.get(s) instanceof NBTTagString) {
                    map.put(s, c.getString(s));
                }
                else if(c.get(s) instanceof NBTTagList) {
                    List<Object> es = new ArrayList<>();
                    NBTTagList l = (NBTTagList) c.get(s);
                    int i = 0;
                    while(i < l.size()) {
                        NBTBase compound = l.i(i);
                        Object bs = decodeList(compound);
                        if(bs != null){
                            es.add(bs);
                        }
                        i++;
                    }
                    map.put(s, es);
                }
                else if(c.get(s) instanceof NBTTagCompound) {
                    map.put(s, decode(c.getCompound(s)));
                }
            }
        }
        return w;
    }

    private Object decodeList(NBTBase compound) {
        if(compound instanceof NBTTagByte){
            return ((NBTTagByte) compound).f();
        }
        else if(compound instanceof NBTTagShort) {
            return ((NBTTagShort) compound).e();
        }
        else if(compound instanceof NBTTagInt) {
            return ((NBTTagInt) compound).d();
        }
        else if(compound instanceof NBTTagLong) {
            return ((NBTTagLong) compound).c();
        }
        else if(compound instanceof NBTTagFloat) {
            return ((NBTTagFloat) compound).i();
        }
        else if(compound instanceof NBTTagDouble) {
            return ((NBTTagDouble) compound).g();
        }
        else if(compound instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) compound).c();
        }
        else if(compound instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) compound).c();
        }
        else if(compound instanceof NBTTagString) {
            return ((NBTTagString) compound).c_();
        }
        else if(compound instanceof NBTTagList) {
            List<Object> es = new ArrayList<>();
            NBTTagList l = (NBTTagList) compound;
            int i = 0;
            while(i < l.size()) {
                NBTBase c = l.i(i);
                Object bs = decodeList(c);
                if(bs != null){
                    es.add(bs);
                }
                i++;
            }
            return es;
        }
        else if(compound instanceof NBTTagCompound) {
            return decode((NBTTagCompound) compound);
        }
        return null;
    }

    private NBTTagCompound encode(NBTCompoundWrapper c){
        NBTTagCompound w = new NBTTagCompound();
        if (c != null) {
            for(String o : map.keySet()){
                Object v = map.get(o);
                if(v instanceof Byte){
                    w.set(o, new NBTTagByte((byte) v));
                } else if(v instanceof Short){
                    w.set(o, new NBTTagShort((short) v));
                } else if(v instanceof Boolean){
                    w.set(o, new NBTTagByte((byte)(((Boolean) v) ? 1 : 0)));
                } else if(v instanceof Integer){
                    w.set(o, new NBTTagInt((int) v));
                } else if(v instanceof Long){
                    w.set(o, new NBTTagLong((long) v));
                } else if(v instanceof Float){
                    w.set(o, new NBTTagFloat((float) v));
                } else if(v instanceof Double){
                    w.set(o, new NBTTagDouble((double) v));
                } else if(v instanceof byte[]){
                    w.set(o, new NBTTagByteArray((byte[]) v));
                } else if(v instanceof int[]){
                    w.set(o, new NBTTagIntArray((int[]) v));
                } else if(v instanceof String) {
                    w.set(o, new NBTTagString((String) v));
                } else if(v instanceof ArrayList &&
                        0 < ((ArrayList) v).size()){
                    NBTTagList ntl = new NBTTagList();
                    ArrayList list = (ArrayList) v;
                    int i = 0;
                    while(i < list.size()) {
                        Object compound = list.get(i);
                        NBTBase bs = encodeList(compound);
                        if(bs != null){
                            ntl.add(bs);
                        }
                        i++;
                    }
                    w.set(o, ntl);
                } else if(v instanceof NBTCompoundWrapper){
                    w.set(o, encode((NBTCompoundWrapper) v));
                }
            }
        }
        return w;
    }

    private NBTBase encodeList(Object compound) {
        if(compound instanceof Byte){
            return new NBTTagByte((byte) compound);
        } else if(compound instanceof Short){
            return new NBTTagShort((short) compound);
        } else if(compound instanceof Boolean){
            return new NBTTagByte((byte)(((Boolean) compound) ? 1 : 0));
        } else if(compound instanceof Integer){
            return new NBTTagInt((int) compound);
        } else if(compound instanceof Long){
            return new NBTTagLong((long) compound);
        } else if(compound instanceof Float){
            return new NBTTagFloat((float) compound);
        } else if(compound instanceof Double){
            return new NBTTagDouble((double) compound);
        } else if(compound instanceof byte[]){
            return new NBTTagByteArray((byte[]) compound);
        } else if(compound instanceof int[]){
            return new NBTTagIntArray((int[]) compound);
        } else if(compound instanceof String) {
            return new NBTTagString((String) compound);
        } else if(compound instanceof ArrayList &&
                0 < ((ArrayList) compound).size()){
            NBTTagList ntl = new NBTTagList();
            ArrayList list = (ArrayList) compound;
            int i = 0;
            while(i < list.size()) {
                Object c = list.get(i);
                if(c instanceof NBTCompoundWrapper) {
                    ntl.add(encode((NBTCompoundWrapper) c));
                } else {
                    ntl.add(encodeList(c));
                }
                i++;
            }
            return ntl;
        } else if(compound instanceof NBTCompoundWrapper){
            return encode((NBTCompoundWrapper) compound);
        }
        return null;
    }

    public NBTCompound_1_12_R1() {
        super();
    }

    public NBTCompound_1_12_R1(NBTTagCompound c){
        map.putAll(decode(c).map);
    }

    @Override
    public void fromItem(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = nmsItem.getTag();
        map.putAll(decode(c).map);
    }

    @Override
    public ItemStack toItem(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.setTag(encode(this));
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public void fromFile(File file) {
        FileInputStream s = null;
        try {
            s = new FileInputStream(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            map.putAll(decode(NBTCompressedStreamTools.a(s)).map);
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toFile(File file) {
        FileOutputStream s = null;
        try {
            s = new FileOutputStream(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            NBTCompressedStreamTools.a(encode(this), s);
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromEntity(Entity entity) {
        NBTTagCompound tag = new NBTTagCompound();
        ((CraftEntity) entity).getHandle().c(tag);
        map.putAll(decode(tag).map);
    }

    @Override
    public void toEntity(Entity entity) {
        ((EntityLiving) ((CraftEntity) entity).getHandle()).a(encode(this));
    }
}
