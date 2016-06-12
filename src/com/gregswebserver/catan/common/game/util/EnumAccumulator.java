package com.gregswebserver.catan.common.game.util;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * Created by greg on 2/27/16.
 * An enum counting map that can be incremented and decremented.
 */
public class EnumAccumulator<T extends Enum<T>> implements Serializable, EnumCounter<T> {

    private final Class<T> tClass;
    private final EnumMap<T, Integer> map;

    public EnumAccumulator(Class<T> tClass) {
        this.tClass = tClass;
        this.map = new EnumMap<>(tClass);
        for (T e : tClass.getEnumConstants())
            map.put(e, 0);
    }

    @SafeVarargs
    public EnumAccumulator(Class<T> tClass, T... array) {
        this(tClass);
        for (T element : array)
            increment(element, 1);
    }

    public EnumAccumulator(Class<T> tClass, EnumCounter<T> other) {
        this(tClass);
        for (T e : other)
            increment(e, other.get(e));
    }

    public EnumAccumulator(Class<T> tClass, T target, int count) {
        this(tClass);
        increment(target, count);
    }

    public void clear(T e) {
        map.put(e, 0);
    }

    @Override
    public int get(T e) {
        return map.get(e);
    }

    public void increment(T e, int c) {
        map.put(e, map.get(e) + c);
    }

    public void decrement(T e, int c) {
        map.put(e, map.get(e) - c);
    }

    @Override
    public String toString() {
        return "EnumAccumulator" + map;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < tClass.getEnumConstants().length;
            }
            @Override
            public T next() {
                return tClass.getEnumConstants()[index++];
            }
        };
    }
}