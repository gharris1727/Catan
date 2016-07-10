package catan.common.util;

import java.util.Iterator;

/**
 * Created by greg on 3/12/16.
 * A class that can have its state reverted safely.
 * This class has the following contracts:
 * get() == next() && prev() == get() && next() == prev()
 * next() -> hasPrev()
 * get() is not necessarily valid before the first next()
 */
public interface ReversibleIterator<T> extends Iterator<T> {

    T get();

    boolean hasPrev();

    T prev();
}
