package com.gregswebserver.catan.client.ui.primary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/2/2015.
 * A list of server connection details.
 */
public class ServerPool implements Iterable<ConnectionInfo> {

    //TODO: rework this to allow drag-swapping of ServerLogin objects.

    private List<ConnectionInfo> list;

    public ServerPool() {
        list = new LinkedList<>();
    }

    public void add(ConnectionInfo login) {
        list.add(0, login);
    }

    public void remove(ConnectionInfo login) { list.remove(login); }

    @Override
    public Iterator<ConnectionInfo> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }
}
