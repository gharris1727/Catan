package catan.common.game.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by greg on 6/26/16.
 * An EnumCounter that serves as a wrapper for another instance to prevent modification.
 */
public class UnmodifiableEnumCounter<T extends Enum<T>> implements Serializable, EnumCounter<T> {

    private final EnumCounter<T> other;

    public UnmodifiableEnumCounter(EnumCounter<T> other) {
        this.other = other;
    }

    @Override
    public int get(T e) {
        return other.get(e);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        other.forEach(consumer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnmodifiableEnumCounter<?> that = (UnmodifiableEnumCounter<?>) o;

        return other.equals(that.other);
    }

    @Override
    public int hashCode() {
        return other.hashCode();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private final Iterator<T> otherIterator = other.iterator();

            @Override
            public boolean hasNext() {
                return otherIterator.hasNext();
            }

            @Override
            public T next() {
                return otherIterator.next();
            }
        };
    }
}
