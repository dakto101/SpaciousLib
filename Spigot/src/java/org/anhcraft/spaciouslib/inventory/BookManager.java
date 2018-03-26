package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;
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
                throw new Exception("Item must be a written book");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new book
     * @param name the name of this book
     * @param amount the amount of book
     */
    public BookManager(String name, int amount){
        super(name, Material.WRITTEN_BOOK, amount);
    }

    /**
     * Sets the copy tier of this book
     * @param generation the copy tier
     * @return this object
     */
    public BookManager setBookGeneration(BookGeneration generation){
        this.item = NBTManager.fromItem(this.item).setInt("generation",
                generation.getID()).toItem(this.item);
        return this;
    }

    /**
     * Gets the copy tier of this book
     * @return the copy tier
     */
    public BookGeneration getBookGeneration(){
        return BookGeneration.getByID(NBTManager.fromItem(this.item).getInt("generation"));
    }

    /**
     * Sets a new author for this book
     * @param author author's name
     * @return this object
     */
    public BookManager setAuthor(String author){
        author = Strings.color(author);
        this.item = NBTManager.fromItem(this.item).setString("author", author).toItem(this.item);
        return this;
    }

    /**
     * Gets the author of this book
     * @return the author
     */
    public String getAuthor(){
        return NBTManager.fromItem(this.item).getString("author");
    }

    /**
     * Sets the title for this book
     * @param title the title
     * @return this object
     */
    public BookManager setTitle(String title){
        title = Strings.color(title);
        this.item = NBTManager.fromItem(this.item).setString("title", title).toItem(this.item);
        return this;
    }

    /**
     * Gets the title of this book
     * @return the title
     */
    public String getTitle(){
        return NBTManager.fromItem(this.item).getString("title");
    }

    /**
     * Sets the resolved status for this book
     * @param resolve true if this book have resolved
     * @return this object
     */
    public BookManager setResolve(boolean resolve){
        this.item = NBTManager.fromItem(this.item).setByte("resolved", (byte) (resolve ? 1 : 0)).toItem(this.item);
        return this;
    }

    /**
     * Gets the resolved status of this book
     * @return true if this book have resolved
     */
    public boolean isResolved(){
        return NBTManager.fromItem(this.item).getByte("resolved") == 1;
    }

    /**
     * Sets new pages for this book
     * @param contents list of page
     * @return this object
     */
    public BookManager setPages(List<String> contents){
        List<String> cont = new ArrayList<>();
        for(String content : contents){
            if(JSONUtils.isValid(content)){
                cont.add(Strings.color(content));
            } else {
                cont.add("{\"text\": \"" + Strings.color(content) + "\"}");
            }
        }
        NBTCompound nbt = NBTManager.fromItem(this.item);
        nbt.setList("pages", cont);
        this.item = nbt.toItem(this.item);
        return this;
    }

    /**
     * Gets all page of this book
     * @return the pages
     */
    public List<String> getPages(){
        NBTCompound nbt = NBTManager.fromItem(this.item);
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
        NBTCompound nbt = NBTManager.fromItem(this.item);
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
        if(JSONUtils.isValid(content)){
            content = Strings.color(content);
        } else {
            content = "{\"text\": \"" + Strings.color(content) + "\"}";
        }
        NBTCompound nbt = NBTManager.fromItem(this.item);
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
        if(JSONUtils.isValid(content)){
            content = Strings.color(content);
        } else {
            content = "{\"text\": \"" + Strings.color(content) + "\"}";
        }
        NBTCompound nbt = NBTManager.fromItem(this.item);
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
        NBTCompound nbt = NBTManager.fromItem(this.item);
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
        NBTCompound nbt = NBTManager.fromItem(this.item);
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