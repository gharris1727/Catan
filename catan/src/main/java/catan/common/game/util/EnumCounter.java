package catan.common.game.util;

/**
 * Created by greg on 6/11/16.
 * A Read-only interface for interacting with an EnumCounter
 */
public interface EnumCounter<T extends Enum<T>> extends Iterable<T> {

    int get(T e);

    default boolean contains(T e, int c) {
        return get(e) >= c;
    }

}
