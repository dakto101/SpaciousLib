package org.anhcraft.spaciouslib.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUtils {
    /**
     * Divides objects to each "page" based on the index
     *
     * @param all list of all objects
     * @param index the index of "page" (start from 0)
     * @param max maximum amount of objects in each "page"
     * @return all objects in that "page"
     */
    public static <X> List<X> getPageItems(List<X> all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        List<X> filter = new ArrayList<>();
        int i = 0;
        for(X v : all){
            if(first <= i && i <= end){
                filter.add(v);
            }
            i++;
        }
        return filter;
    }

    /**
     * Converts all elements of a list to a new array
     * @param list list of elements
     * @param class_ the class of the elements
     * @return the new array
     */
    public static <X> X[] toArray(List<X> list, Class class_){
        return list.toArray((X[]) Array.newInstance(class_, list.size()));
    }

    /**
     * Converts all elements of an array to a new list
     * @param array the array which contains these elements
     * @return the new list
     */
    public static <X> List<X> toList(X[] array){
        return new ArrayList<>(Arrays.asList(array));
    }
}
