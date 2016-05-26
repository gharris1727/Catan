package com.gregswebserver.catan.common.structure.game;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 2/27/16.
 * An enum counting map that can be incremented and decremented.
 */
public class EnumCounter<T extends Enum<T>> implements Serializable, Iterable<Map.Entry<T, Integer>> {

    private final Class<T> tClass;
    private final EnumMap<T, Integer> map;

    public EnumCounter(Class<T> tClass) {
        this.tClass = tClass;
        this.map = new EnumMap<>(tClass);
        for (T e : tClass.getEnumConstants())
            map.put(e, 0);
    }

    public EnumCounter(EnumCounter<T> other) {
        this.tClass = other.tClass;
        this.map = new EnumMap<>(other.map);
    }

    public int clear(T e) {
        int c = map.get(e);
        map.put(e, 0);
        return c;
    }

    public int get(T e) {
        return map.get(e);
    }

    public void increment(T e, int c) {
        map.put(e, map.get(e) + c);
    }

    public void decrement(T e, int c) {
        map.put(e, map.get(e) - c);
    }

    public boolean contains(T e, int c) {
        return map.get(e) >= c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumCounter<?> that = (EnumCounter<?>) o;

        if (!tClass.equals(that.tClass)) return false;
        return map.equals(that.map);

    }

    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return "EnumCounter" + map;
    }

    @Override
    public Iterator<Map.Entry<T, Integer>> iterator() {
        return map.entrySet().iterator();
    }
}
