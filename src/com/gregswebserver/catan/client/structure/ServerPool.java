package com.gregswebserver.catan.client.structure;

import com.gregswebserver.catan.common.resources.ConfigurationException;
import com.gregswebserver.catan.common.resources.PropertiesFile;
import com.gregswebserver.catan.common.resources.PropertiesFileInfo;
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

    private final PropertiesFileInfo serverListFile;
    private final List<ConnectionInfo> connectionInfoList;

    @SuppressWarnings("InfiniteLoopStatement")
    public ServerPool(PropertiesFileInfo serverListFile) {
        this.serverListFile = serverListFile;
        this.connectionInfoList = new LinkedList<>();
        PropertiesFile file = ResourceLoader.getPropertiesFile(serverListFile);
        try {
            int i = 0;
            while (true) {
                String hostname = file.get("servers." + i + ".hostname");
                String port = file.get("servers." + i + ".port");
                String username = file.get("servers." + i + ".username");
                connectionInfoList.add(new ConnectionInfo(hostname, port, username));
                i++;
            }
        } catch (ConfigurationException ignored) {
            //This loop is meant to error out once we run out of elements.
        }
    }

    public void save() {
        PropertiesFile serverList = ResourceLoader.getPropertiesFile(serverListFile);
        serverList.clear();
        int i = 0;
        for (ConnectionInfo elt : connectionInfoList) {
            serverList.set("servers." + i + ".hostname", elt.getRemote());
            serverList.set("servers." + i + ".port", elt.getPort());
            serverList.set("servers." + i + ".username", elt.getUsername());
            i++;
        }
        ResourceLoader.savePropertiesFile(serverListFile);
    }

    public void add(ConnectionInfo login) {
        connectionInfoList.add(0, login);
    }

    public void remove(ConnectionInfo login) { connectionInfoList.remove(login); }

    @Override
    public Iterator<ConnectionInfo> iterator() {
        return connectionInfoList.iterator();
    }

    public int size() {
        return connectionInfoList.size();
    }
}
