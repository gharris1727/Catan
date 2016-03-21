package com.gregswebserver.catan.client.structure;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.resources.PropertiesFile;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/2/2015.
 * A list of server connection details.
 */
public class ServerPool implements Iterable<ConnectionInfo> {

    //TODO: rework this to allow drag-swapping of ServerLogin objects.

    private final List<ConnectionInfo> list;
    private final PropertiesFile file;

    public ServerPool() {
        list = new LinkedList<>();
        file = ResourceLoader.getPropertiesFile(Client.serverFile);
        int i = 0;
        String hostname;
        do {
            hostname = file.get("servers." + i + ".hostname");
            String port = file.get("servers." + i + ".port");
            String username = file.get("servers." + i + ".username");
            if (hostname != null)
                list.add(new ConnectionInfo(hostname,port,username));
            i++;
        } while (hostname != null);
    }

    public void save() {
        file.clear();
        int i = 0;
        for (ConnectionInfo elt : list) {
            file.set("servers." + i + ".hostname", elt.getRemote());
            file.set("servers." + i + ".port", elt.getPort());
            file.set("servers." + i + ".username", elt.getUsername());
            i++;
        }
        ResourceLoader.savePropertiesFile(Client.serverFile);
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
