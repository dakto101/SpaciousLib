package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTLoader;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A manage helps you to manage books.<br>
 * (<a href="https://minecraft.gamepedia.com/Written_Book">https://minecraft.gamepedia.com/Written_Book</a>)
 */
public class BookManager extends ItemManager {

    /**
     * Creates BookManager instance
     * @param book the item stack (must be a written book) which represents for that book
     */
    public BookManager(ItemStack book){
        super(book);
        if(book == null || !book.getType().equals(Material.WRITTEN_BOOK)){
            try {
                throw new Exception("The item must be a written book");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(getName() != null){
            setTitle(getName());
        }
    }

    /**
     * Creates a new book
     * @param name the name of this book
     * @param amount the amount of book
     */
    public BookManager(String name, int amount){
        super(name, Material.WRITTEN_BOOK, amount);
        if(getName() != null){
            setTitle(getName());
        }
    }

    /**
     * Sets the copy tier of this book
     * @param generation the copy tier
     * @return this object
     */
    public BookManager setBookGeneration(BookGeneration generation){
        this.item = NBTLoader.fromItem(this.item).setInt("generation",
                generation.getID()).toItem(this.item);
        return this;
    }

    /**
     * Gets the copy tier of this book
     * @return the copy tier
     */
    public BookGeneration getBookGeneration(){
        return BookGeneration.getByID(NBTLoader.fromItem(this.item).getInt("generation"));
    }

    /**
     * Sets a new author for this book
     * @param author author's name
     * @return this object
     */
    public BookManager setAuthor(String author){
        author = Chat.color(author);
        this.item = NBTLoader.fromItem(this.item).setString("author", author).toItem(this.item);
        return this;
    }

    /**
     * Gets the author of this book
     * @return the author
     */
    public String getAuthor(){
        return NBTLoader.fromItem(this.item).getString("author");
    }

    /**
     * Sets the title for this book
     * @param title the title
     * @return this object
     */
    public BookManager setTitle(String title){
        title = Chat.color(title);
        this.item = NBTLoader.fromItem(this.item).setString("title", title).toItem(this.item);
        return this;
    }

    /**
     * Gets the title of this book
     * @return the title
     */
    public String getTitle(){
        return NBTLoader.fromItem(this.item).getString("title");
    }

    /**
     * Sets the resolved status for this book
     * @param resolve true if this book have resolved
     * @return this object
     */
    public BookManager setResolve(boolean resolve){
        this.item = NBTLoader.fromItem(this.item).setByte("resolved", (byte) (resolve ? 1 : 0)).toItem(this.item);
        return this;
    }

    /**
     * Gets the resolved status of this book
     * @return true if this book have resolved
     */
    public boolean isResolved(){
        return NBTLoader.fromItem(this.item).getByte("resolved") == 1;
    }

    /**
     * Sets new pages for this book
     * @param contents list of page
     * @return this object
     */
    public BookManager setPages(List<String> contents){
        List<String> cont = new ArrayList<>();
        for(String content : contents){
            if(CommonUtils.isValidJSON(content)){
                cont.add(Chat.color(content));
            } else {
                cont.add("{\"text\": \"" + Chat.color(content) + "\"}");
            }
        }
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        nbt.setList("pages", cont);
        this.item = nbt.toItem(this.item);
        return this;
    }

    /**
     * Gets all page of this book
     * @return the pages
     */
    public List<String> getPages(){
        NBTCompound nbt = NBTLoader.fromItem(this.item);
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

    /**
     * Gets the page with given index
     * @param index the index of that page
     * @return the content of that page
     */
    public String getPage(int index){
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        return pages.get(index);
    }

    /**
     * Adds a new page into this book
     * @param content the content of that page
     * @return this object
     */
    public BookManager addPage(String content){
        if(CommonUtils.isValidJSON(content)){
            content = Chat.color(content);
        } else {
            content = "{\"text\": \"" + Chat.color(content) + "\"}";
        }
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.add(content);
        nbt.setList("pages", pages);
        this.item = nbt.toItem(this.item);
        return this;
    }

    /**
     * Sets the new content for the page with given index
     * @param index the index of that page
     * @param content the content of that page
     * @return this object
     */
    public BookManager setPage(int index, String content){
        if(CommonUtils.isValidJSON(content)){
            content = Chat.color(content);
        } else {
            content = "{\"text\": \"" + Chat.color(content) + "\"}";
        }
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.set(index, content);
        nbt.setList("pages", pages);
        this.item = nbt.toItem(this.item);
        return this;
    }

    /**
     * Removes a specific page with given page
     * @param index the index of that page
     * @return this object
     */
    public BookManager removePage(int index){
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        List<String> pages = nbt.getList("pages");
        if(pages == null){
            pages = new ArrayList<>();
        }
        pages.remove(index);
        nbt.setList("pages", pages);
        this.item = nbt.toItem(this.item);
        return this;
    }

    /**
     * Removes all pages
     * @return this object
     */
    public BookManager removePages(){
        NBTCompound nbt = NBTLoader.fromItem(this.item);
        nbt.setList("pages", new ArrayList<>());
        this.item = nbt.toItem(this.item);
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

        public static BookGeneration getByID(int generation) {
            for(BookGeneration bg : values()){
                if(bg.getID() == generation){
                    return bg;
                }
            }
            return null;
        }
    }
}