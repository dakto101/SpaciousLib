package org.anhcraft.spaciouslib.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public class TimedMap<K, V> {
    private LinkedHashMap<K, V> a = new LinkedHashMap<>();
    private LinkedHashMap<K, Long> b = new LinkedHashMap<>();

    private void clean(){
        for(K key : keySet()){
            if(isExpired(key)){
                remove(key);
            }
        }
    }

    public boolean isExpired(K key){
        return System.currentTimeMillis() > b.get(key);
    }

    public int size() {
        clean();
        return a.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(K key) {
        clean();
        return a.containsKey(key) && b.containsKey(key);
    }

    public boolean containsValue(V value) {
        clean();
        return a.containsValue(value);
    }

    public V get(K key) {
        return (containsKey(key) && !isExpired(key)) ? a.get(key) : null;
    }

    public V put(K key, V value, long seconds) {
        clean();
        a.put(key, value);
        b.put(key, System.currentTimeMillis() + (seconds * 1000L));
        return value;
    }

    public void remove(K key) {
        a.remove(key);
        b.remove(key);
    }

    public void clear() {
        a = new LinkedHashMap<>();
        b = new LinkedHashMap<>();
    }

    public Set<K> keySet() {
        clean();
        return a.keySet();
    }

    public Collection<V> values() {
        clean();
        return a.values();
    }
}