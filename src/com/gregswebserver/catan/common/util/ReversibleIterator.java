package com.gregswebserver.catan.common.util;

import java.util.Iterator;

/**
 * Created by greg on 3/12/16.
 * A class that can have its state reverted safely.
 */
public interface ReversibleIterator<T> extends Iterator<T> {

    T get();

    boolean hasPrev();

    T prev();
}
