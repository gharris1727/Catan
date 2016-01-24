package com.gregswebserver.catan.client.ui.primary;

import com.gregswebserver.catan.common.resources.ConfigFile;

import java.io.IOException;
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
    private final ConfigFile file;

    public ServerPool() {
        list = new LinkedList<>();
        file = new ConfigFile("config/client/servers.properties", "Login details for servers");
        try {
            file.open();
            int i = 0;
            String hostname;
            String port;
            String username;
            do {
                hostname = file.get("servers." + i + ".hostname");
                port = file.get("servers." + i + ".port");
                username = file.get("servers." + i + ".username");
                if (hostname != null)
                    list.add(new ConnectionInfo(hostname,port,username));
                i++;
            } while (hostname != null);
        } catch (Exception e) {
            //If the open() function threw an exception, we merely failed to load a pool file.
        }
    }

    public void save() throws IOException {
        file.clear();
        int i = 0;
        for (ConnectionInfo elt : list) {
            file.set("servers." + i + ".hostname", elt.getRemote());
            file.set("servers." + i + ".port", elt.getPort());
            file.set("servers." + i + ".username", elt.getUsername());
            i++;
        }
        file.close();
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
