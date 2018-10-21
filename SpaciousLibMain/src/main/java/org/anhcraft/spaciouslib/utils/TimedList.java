package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;
import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.annotations.Serializable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A TimedList is a list which removes any expired element automatically.
 */
@Serializable
public class TimedList<E> implements Iterable<E> {
    @DataField
    private LinkedList<E> a = new LinkedList<>();
    @DataField
    private LinkedList<Long> b = new LinkedList<>();

    /**
     * Not recommended constructor, only be used during serialization processes
     */
    public TimedList(){}

    public TimedList(TimedList<E> timedList){
        this.a = new LinkedList<>(timedList.a);
        this.b = new LinkedList<>(timedList.b);
    }

    private Long getValue(E key){
        int i = 0;
        for(E e : a){
            if(e.equals(key)){
                return b.get(i);
            }
            i++;
        }
        return 0L;
    }

    private void clean(){
        // don't use the "keySet" method to prevent the StackOverflow error
        for(E elem : a){
            if(isExpired(elem)){
                remove(elem);
            }
        }
    }

    /**
     * Checks was the given element expired<br>
     * By default, all elements which were expired will be removed automatically, so you don't need to use this method.
     * @param elem an element
     * @return true if it expired
     */
    public boolean isExpired(E elem){
        // don't use the "containsKey" method to prevent the StackOverflow error
        return !a.contains(elem) || System.currentTimeMillis() > getValue(elem);
    }

    /**
     * Gets the amount of element in this list.
     * @return the amount
     */
    public int size() {
        clean();
        return a.size();
    }

    /**
     * Checks does this list empty
     * @return true if yes
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks does this list contain the given element
     * @param elem an element
     * @return true if yes
     */
    public boolean contains(E elem) {
        clean();
        return a.contains(elem);
    }

    /**
     * Adds the given key and its value with the expired time
     * @param elem an element
     * @param seconds the expired time (in seconds)
     */
    public void add(E elem, long seconds) {
        clean();
        a.add(elem);
        b.add(System.currentTimeMillis() + (seconds * 1000L));
    }

    /**
     * Removes the given element
     * @param elem an element
     */
    public void remove(E elem) {
        int i = 0;
        for(E e : a){
            if(e.equals(elem)){
                b.remove(i);
                break;
            }
            i++;
        }
        a.remove(elem);
    }

    /**
     * Clears all elements in this list
     */
    public void clear() {
        a = new LinkedList<>();
        b = new LinkedList<>();
    }

    /**
     * Adds all elements of the given list to this list
     * @param list a timed list
     */
    public void addAll(TimedList<E> list){
        this.a.addAll(list.a);
        this.b.addAll(list.b);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            TimedList m = (TimedList) o;
            return new EqualsBuilder()
                    .append(m.a, this.a)
                    .append(m.b, this.b)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(21, 33)
                .append(this.a).append(this.b).build();
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private int next = 0;
        private E current;

        @Override
        public boolean hasNext(){
            return next < size();
        }

        @Override
        public E next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = a.get(next);
            next++;
            return current;
        }

        @Override
        public void remove() {
            next--;
            a.remove(next);
            b.remove(next);
        }
    }
}