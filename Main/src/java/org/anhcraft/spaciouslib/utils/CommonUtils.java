package org.anhcraft.spaciouslib.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
    /**
     * Divides objects to each page based on the index
     *
     * @param all list of all objects
     * @param index the index of page (start from 0)
     * @param max maximum amount of objects in each page
     * @return all objects in that page
     */
    public static <X> List<X> getPage(List<X> all, int index, int max){
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
}
