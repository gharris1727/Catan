package com.gregswebserver.catan.common.crypto;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/2/2015.
 * A list of ServerLogin objects to use to connect to remote servers.
 */
public class ServerList implements Iterable<ServerLogin> {

    //TODO: rework this to allow drag-swapping of ServerLogin objects.

    private List<ServerLogin> list;

    public ServerList() {
        list = new LinkedList<>();
    }

    public void add(ServerLogin login) {
        list.add(0, login);
    }

    public void edit(int index, ServerLogin login) {
        list.remove(index);
        add(login);
    }

    public ServerLogin use(int index) {
        ServerLogin login = list.remove(index);
        add(login);
        return login;
    }

    public Iterator<ServerLogin> iterator() {
        return list.iterator();
    }
}
