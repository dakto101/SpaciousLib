package org.anhcraft.spaciouslib.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    public static <X> X pickRandom(List<X> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <X> X pickRandom(X[] list) {
        return list[new Random().nextInt(list.length)];
    }
}
