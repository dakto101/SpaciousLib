package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.ArrayBuilder;

import java.util.function.Consumer;

/**
 * Represents an array splitter which splits the array into pages
 * @param <T> the type of the array
 */
public class Paginator<T> {
    private T[] data;
    private int length;
    private int current;
    private int slot;

    /**
     * Creates a new instance
     * @param data an array
     * @param slot the number of elements in each page
     */
    public Paginator(T[] data, int slot){
        this.data = data;
        this.length = data.length;
        this.slot = slot;
        this.current = 0;
    }

    /**
     * Creates a new instance
     * @param data an iterator
     * @param slot the number of elements in each page
     */
    public Paginator(Iterable<T> data, int slot){
        ArrayBuilder builder = new ArrayBuilder(Object.class);
        for(T e : data){
            builder.append(e);
        }
        this.data = (T[]) builder.build();
        this.length = this.data.length;
        this.slot = slot;
        this.current = 0;
    }

    /**
     * Goes to a page.<br>
     * Warning: Accessing a page outside the allowed range is not possible
     * @param page the index of the page (from zero)
     * @return this object
     */
    public Paginator<T> go(int page){
        ExceptionThrower.ifTrue(page < 0, new Exception("Page index must from 0"));
        current = page;
        if(empty() && page > 0){
            current = length/slot;
        }
        return this;
    }

    /**
     * Goes to the next page.<br>
     * Warning: Accessing a page outside the allowed range is not possible
     * @return this object
     */
    public Paginator<T> next(){
        return go(current+1);
    }

    /**
     * Goes to the previous page.<br>
     * Warning: Accessing a page outside the allowed range is not possible
     * @return this object
     */
    public Paginator<T> prev(){
        return go(current-1);
    }

    /**
     * Check whether the current page is empty
     * @return true if yes
     */
    public boolean empty(){
        return current > length/slot;
    }

    /**
     * Get all elements in the current page
     * @return an array of elements
     */
    public T[] get(){
        return CommonUtils.getPageItems(data, current, slot);
    }

    /**
     * Executes operation for each element in the current page
     * @param consumer consumer
     * @return this object
     */
    public Paginator<T> each(Consumer<T> consumer){
        for(T e : get()){
            consumer.accept(e);
        }
        return this;
    }

    /**
     * Get the maximum number of pages
     * @return maximum number of pages
     */
    public int max(){
        return length/slot;
    }

    /**
     * Get the minimum number of pages
     * @return minimum number of pages
     */
    public int min(){
        return 0;
    }
}
