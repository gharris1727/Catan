package com.gregswebserver.catan.common.crypto;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/2/2015.
 * A list of ServerLogin objects to use to connect to remote servers.
 */
public class ServerList implements Iterable<ConnectionInfo> {

    //TODO: rework this to allow drag-swapping of ServerLogin objects.

    private List<ConnectionInfo> list;

    public ServerList() {
        list = new LinkedList<>();
    }

    public void add(ConnectionInfo login) {
        list.add(0, login);
    }

    public ConnectionInfo get(int index) {
        return list.get(index);
    }

    public void edit(int index, ConnectionInfo login) {
        list.remove(index);
        add(login);
    }

    public ConnectionInfo use(int index) {
        ConnectionInfo login = list.remove(index);
        add(login);
        return login;
    }

    public Iterator<ConnectionInfo> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }
}
