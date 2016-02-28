package com.gregswebserver.catan.common.structure.game;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 2/27/16.
 * An enum counting map that can be incremented and decremented.
 */
public class EnumCounter<T extends Enum<T>> implements Serializable {

    private final Class<T> tClass;
    private final EnumMap<T, Integer> map;

    public EnumCounter(Class<T> tClass) {
        this.tClass = tClass;
        this.map = new EnumMap<>(tClass);
        for (T e : tClass.getEnumConstants())
            map.put(e, 0);
    }

    public int clear(T e) {
        int c = map.get(e);
        map.put(e, 0);
        return c;
    }

    public int get(T e) {
        return map.get(e);
    }

    public int increment(T e, int c) {
        map.put(e, map.get(e) + c);
        return map.get(e);
    }

    public int decrement(T e, int c) {
        map.put(e, map.get(e) - c);
        return map.get(e);
    }

    public boolean contains(T e, int c) {
        return map.get(e) >= c;
    }

    public Class<T> getEnumClass() {
        return tClass;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof EnumCounter<?>) {
            EnumCounter<?> other = (EnumCounter<?>) o;
            if (other.getEnumClass() == getEnumClass()) {
                if (map.size() != other.map.size())
                    return false;
                for (Map.Entry entry : map.entrySet()) {
                    if (!other.map.containsKey(entry.getKey()))
                        return false;
                    if (!other.map.get(entry.getKey()).equals(entry.getValue()))
                        return false;
                }
                for (Map.Entry<?, Integer> entry : other.map.entrySet()) {
                    if (!map.containsKey(entry.getKey()))
                        return false;
                    if (!map.get(entry.getKey()).equals(entry.getValue()))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return map.hashCode();
    }
}
