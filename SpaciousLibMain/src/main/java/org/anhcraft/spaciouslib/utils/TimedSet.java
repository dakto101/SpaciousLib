package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;
import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.annotations.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * A TimedSet is a set which removes any expired element automatically.
 */
@Serializable
public class TimedSet<E> implements Iterable<E> {
    @DataField
    private LinkedHashMap<E, Long> data = new LinkedHashMap<>();

    private void clean(){
        data.keySet().removeIf(this::isExpired);
    }

    public TimedSet(){}

    public TimedSet(TimedSet<E> timedSet){
        this.data = new LinkedHashMap<>(timedSet.data);
    }

    /**
     * Checks was the given element expired<br>
     * By default, all elements which were expired will be removed automatically, so you don't need to use this method.
     * @param elem an element
     * @return true if it expired
     */
    public boolean isExpired(E elem){
        // don't use the "containsKey" method to prevent the StackOverflow error
        return !data.containsKey(elem) || System.currentTimeMillis() > data.get(elem);
    }

    /**
     * Gets the amount of element in this set.
     * @return the amount
     */
    public int size() {
        clean();
        return data.size();
    }

    /**
     * Checks does this set empty
     * @return true if yes
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks does this set contain the given element
     * @param elem an element
     * @return true if yes
     */
    public boolean contains(E elem) {
        clean();
        return data.containsKey(elem);
    }

    /**
     * Adds the given key and its value with the expired time
     * @param elem an element
     * @param seconds the expired time (in seconds)
     */
    public void add(E elem, long seconds) {
        clean();
        data.put(elem, System.currentTimeMillis() + (seconds * 1000L));
    }

    /**
     * Removes the given element
     * @param elem an element
     */
    public void remove(E elem) {
        data.remove(elem);
    }

    /**
     * Clears all elements in this set
     */
    public void clear() {
        data = new LinkedHashMap<>();
    }

    /**
     * Adds all elements of the given set to this set
     * @param set a timed set
     */
    public void addAll(TimedSet<E> set){
        this.data.putAll(set.data);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            TimedSet m = (TimedSet) o;
            return new EqualsBuilder()
                    .append(m.data, this.data)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(21, 17)
                .append(this.data).build();
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
            current = new ArrayList<>(data.keySet()).get(next);
            next++;
            return current;
        }

        @Override
        public void remove() {
            next--;
            data.remove(new ArrayList<>(data.keySet()).get(next));
        }
    }
}