package org.anhcraft.spaciouslib.nbt;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NBTCompound_1_13_R2 extends NBTCompound {
    private NBTCompound decode(NBTTagCompound c){
        NBTCompound w = new NBTCompound_1_13_R2();
        if (c != null) {
            for(String s : c.getKeys()){
                if(c.get(s) instanceof NBTTagByte){
                    byte b = c.getByte(s);
                    if(b ==  ((byte) 0)){
                        w.tags.put(s, false);
                    } else if(b == ((byte) 1)){
                        w.tags.put(s, true);
                    } else {
                        w.tags.put(s, b);
                    }
                }
                else if(c.get(s) instanceof NBTTagShort) {
                    w.tags.put(s, c.getShort(s));
                }
                else if(c.get(s) instanceof NBTTagInt) {
                    w.tags.put(s, c.getInt(s));
                }
                else if(c.get(s) instanceof NBTTagLong) {
                    w.tags.put(s, c.getLong(s));
                }
                else if(c.get(s) instanceof NBTTagFloat) {
                    w.tags.put(s, c.getFloat(s));
                }
                else if(c.get(s) instanceof NBTTagDouble) {
                    w.tags.put(s, c.getDouble(s));
                }
                else if(c.get(s) instanceof NBTTagByteArray) {
                    w.tags.put(s, c.getByteArray(s));
                }
                else if(c.get(s) instanceof NBTTagIntArray) {
                    w.tags.put(s, c.getIntArray(s));
                }
                else if(c.get(s) instanceof NBTTagString) {
                    w.tags.put(s, c.getString(s));
                }
                else if(c.get(s) instanceof NBTTagList) {
                    List<Object> es = new ArrayList<>();
                    NBTTagList l = (NBTTagList) c.get(s);
                    int i = 0;
                    while(i < l.size()) {
                        NBTBase compound = l.c(i);
                        Object bs = decodeList(compound);
                        if(bs != null){
                            es.add(bs);
                        }
                        i++;
                    }
                    w.tags.put(s, es);
                }
                else if(c.get(s) instanceof NBTTagCompound) {
                    w.tags.put(s, decode(c.getCompound(s)));
                }
            }
        }
        return w;
    }

    private Object decodeList(NBTBase compound) {
        if(compound instanceof NBTTagByte){
            return ((NBTTagByte) compound).g();
        }
        else if(compound instanceof NBTTagShort) {
            return ((NBTTagShort) compound).f();
        }
        else if(compound instanceof NBTTagInt) {
            return ((NBTTagInt) compound).e();
        }
        else if(compound instanceof NBTTagLong) {
            return ((NBTTagLong) compound).d();
        }
        else if(compound instanceof NBTTagFloat) {
            return ((NBTTagFloat) compound).i();
        }
        else if(compound instanceof NBTTagDouble) {
            return ((NBTTagDouble) compound).asDouble();
        }
        else if(compound instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) compound).c();
        }
        else if(compound instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) compound).d();
        }
        else if(compound instanceof NBTTagString) {
            return compound.b_();
        }
        else if(compound instanceof NBTTagList) {
            List<Object> es = new ArrayList<>();
            NBTTagList l = (NBTTagList) compound;
            int i = 0;
            while(i < l.size()) {
                NBTBase c = l.c(i);
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

    private NBTTagCompound encode(NBTCompound c){
        NBTTagCompound w = new NBTTagCompound();
        if (c != null) {
            for(String o : c.tags.keySet()){
                Object v = c.tags.get(o);
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
                } else if(v instanceof NBTCompound){
                    w.set(o, encode((NBTCompound) v));
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
                if(c instanceof NBTCompound) {
                    ntl.add(encode((NBTCompound) c));
                } else {
                    ntl.add(encodeList(c));
                }
                i++;
            }
            return ntl;
        } else if(compound instanceof NBTCompound){
            return encode((NBTCompound) compound);
        }
        return null;
    }

    public NBTCompound_1_13_R2() {
        super();
    }

    public NBTCompound_1_13_R2(NBTTagCompound c){
        tags.putAll(decode(c).tags);
    }

    @Override
    protected void fromItem(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound c = nmsItem.getTag();
        tags.putAll(decode(c).tags);
    }

    @Override
    public ItemStack toItem(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.setTag(encode(this));
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    protected void fromFile(File file) {
        FileInputStream s = null;
        try {
            s = new FileInputStream(file);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            tags.putAll(decode(NBTCompressedStreamTools.a(s)).tags);
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
    protected void fromEntity(Entity entity) {
        net.minecraft.server.v1_13_R2.Entity ce = ((CraftEntity) entity).getHandle();
        MinecraftKey minecraftkey = EntityTypes.getName(ce.P());
        String k = minecraftkey == null ? null : minecraftkey.toString();
        NBTTagCompound tag = new NBTTagCompound();
        tags.put("id", k);
        tags.putAll(decode(ce.save(tag)).tags);
    }

    @Override
    public void toEntity(Entity entity) {
        ((EntityLiving) ((CraftEntity) entity).getHandle()).a(encode(this));
    }
}
