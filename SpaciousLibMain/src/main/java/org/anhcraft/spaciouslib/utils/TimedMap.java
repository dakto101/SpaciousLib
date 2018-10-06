package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;
import org.anhcraft.spaciouslib.serialization.DataField;
import org.anhcraft.spaciouslib.serialization.Serializable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * A TimedMap is a map which removes any expired key automatically.
 */
@Serializable
public class TimedMap<K, V> {
    @DataField
    private LinkedHashMap<K, V> a = new LinkedHashMap<>();
    @DataField
    private LinkedHashMap<K, Long> b = new LinkedHashMap<>();

    private void clean(){
        // don't use the "keySet" method to prevent the StackOverflow error
        for(K key : a.keySet()){
            if(isExpired(key)){
                remove(key);
            }
        }
    }

    /**
     * Checks was the given key expired<br>
     * By default, all keys and values which were expired will be removed automatically, so you don't need to use this method.
     * @param key a key
     * @return true if it expired
     */
    public boolean isExpired(K key){
        // don't use the "containsKey" method to prevent the StackOverflow error
        return !(a.containsKey(key) && b.containsKey(key)) || System.currentTimeMillis() > b.get(key);
    }

    /**
     * Gets the amount of key-value mappings in this map.
     * @return the amount
     */
    public int size() {
        clean();
        return a.size();
    }

    /**
     * Checks does this map empty
     * @return true if yes
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks does this map contain the given key
     * @param key a key
     * @return true if yes
     */
    public boolean containsKey(K key) {
        clean();
        return a.containsKey(key) && b.containsKey(key);
    }

    /**
     * Checks does this map contain the given value
     * @param value a value
     * @return true if yes
     */
    public boolean containsValue(V value) {
        clean();
        return a.containsValue(value);
    }

    /**
     * Gets the value of the given key in this map
     * @param key a key
     * @return its value
     */
    public V get(K key) {
        return (containsKey(key) && !isExpired(key)) ? a.get(key) : null;
    }

    /**
     * Sets the given key and its value with the expired time
     * @param key a key
     * @param value the value of the key
     * @param seconds an expired time (in seconds)
     */
    public void put(K key, V value, long seconds) {
        clean();
        a.put(key, value);
        b.put(key, System.currentTimeMillis() + (seconds * 1000L));
    }

    /**
     * Removes the given key
     * @param key a key
     */
    public void remove(K key) {
        a.remove(key);
        b.remove(key);
    }

    /**
     * Clears all keys and values in this map
     */
    public void clear() {
        a = new LinkedHashMap<>();
        b = new LinkedHashMap<>();
    }

    /**
     * Gets all keys in this map as a key set
     * @return a set of key
     */
    public Set<K> keySet() {
        clean();
        return a.keySet();
    }

    /**
     * Gets all values in this map as a collection
     * @return a collection of values
     */
    public Collection<V> values() {
        clean();
        return a.values();
    }

    /**
     * Puts all keys and values of the given map to this map
     * @param map a timed map
     */
    public void putAll(TimedMap<K, V> map){
        this.a.putAll(map.a);
        this.b.putAll(map.b);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            TimedMap m = (TimedMap) o;
            return new EqualsBuilder()
                    .append(m.a, this.a)
                    .append(m.b, this.b)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(21, 35)
                .append(this.a).append(this.b).build();
    }
}