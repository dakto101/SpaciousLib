package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class SBook extends SItem {
    public SBook(String name, int amount){
        super(name, Material.WRITTEN_BOOK, amount);
    }

    public SBook setBookGeneration(BookGeneration generation){
        this.item = new NBTManager(this.item).setInt("generation",
                generation.getID()).toItemStack(this.item);
        return this;
    }

    public BookGeneration getBookGeneration(){
        return BookGeneration.getById(new NBTManager(this.item).getInt("generation"));
    }

    public SBook setAuthor(String author){
        author = Strings.color(author);
        this.item = new NBTManager(this.item).setString("author", author).toItemStack(this.item);
        return this;
    }

    public String getAuthor(){
        return new NBTManager(this.item).getString("author");
    }

    public SBook setTitle(String title){
        title = Strings.color(title);
        this.item = new NBTManager(this.item).setString("title", title).toItemStack(this.item);
        return this;
    }

    public String getTitle(){
        return new NBTManager(this.item).getString("title");
    }

    public SBook setResolve(boolean resolved){
        this.item = new NBTManager(this.item).setByte("resolved", (byte) (resolved ? 1 : 0)).toItemStack(this.item);
        return this;
    }

    public boolean isResolved(){
        return new NBTManager(this.item).getByte("resolved") == 1;
    }

    public SBook setPages(List<String> contents){
        List<String> cont = new ArrayList<>();
        for(String content : contents){
            if(JSONUtils.isValid(content)){
                cont.add(Strings.color(content));
            } else {
                cont.add("{\"text\": \"" + Strings.color(content) + "\"}");
            }
        }
        NBTManager nbt = new NBTManager(this.item);
        nbt.setList("pages", cont);
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public List<String> getPages(){
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        List<String> x = new ArrayList<>();
        for(Object o : pages){
            if(o instanceof String){
                x.add((String) o);
            }
        }
        return x;
    }

    public String getPage(int index){
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        return (String) pages.get(index);
    }

    public SBook addPage(String content){
        if(JSONUtils.isValid(content)){
            content = Strings.color(content);
        } else {
            content = "{\"text\": \"" + Strings.color(content) + "\"}";
        }
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.add(content);
        nbt.setList("pages", pages);
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public SBook addPage(int index, String content){
        if(JSONUtils.isValid(content)){
            content = Strings.color(content);
        } else {
            content = "{\"text\": \"" + Strings.color(content) + "\"}";
        }
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.add(index, content);
        nbt.setList("pages", pages);
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public SBook setPage(int index, String content){
        if(JSONUtils.isValid(content)){
            content = Strings.color(content);
        } else {
            content = "{\"text\": \"" + Strings.color(content) + "\"}";
        }
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.set(index, content);
        nbt.setList("pages", pages);
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public SBook removePage(int index){
        NBTManager nbt = new NBTManager(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.remove(index);
        nbt.setList("pages", pages);
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public SBook removePages(){
        NBTManager nbt = new NBTManager(this.item);
        nbt.setList("pages", new ArrayList<>());
        this.item = nbt.toItemStack(this.item);
        return this;
    }

    public enum BookGeneration {
        ORIGINAL(0),
        COPY_ORIGINAL(1),
        COPY_COPY(2),
        TATTERED(3);

        private int id;

        BookGeneration(int id){
            this.id = id;
        }

        public int getID(){
            return id;
        }

        public static BookGeneration getById(int generation) {
            for(BookGeneration bg : values()){
                if(bg.getID() == generation){
                    return bg;
                }
            }
            return null;
        }
    }
}