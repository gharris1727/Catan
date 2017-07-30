package catan.common.game.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by greg on 6/26/16.
 * An EnumCounter that serves as a wrapper for another instance to prevent modification.
 */
public class UnmodifiableEnumCounter<T extends Enum<T>> implements Serializable, EnumCounter<T> {

    private final EnumCounter<T> backingCounter;

    public UnmodifiableEnumCounter(EnumCounter<T> backingCounter) {
        this.backingCounter = backingCounter;
    }

    @Override
    public int get(T e) {
        return backingCounter.get(e);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        backingCounter.forEach(consumer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        UnmodifiableEnumCounter<?> other = (UnmodifiableEnumCounter<?>) o;

        return this.backingCounter.equals(other.backingCounter);
    }

    @Override
    public int hashCode() {
        return backingCounter.hashCode();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private final Iterator<T> otherIterator = backingCounter.iterator();

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
