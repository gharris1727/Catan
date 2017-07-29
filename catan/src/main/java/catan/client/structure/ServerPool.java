package catan.client.structure;

import catan.common.config.ConfigurationException;
import catan.common.resources.PropertiesFile;
import catan.common.resources.PropertiesFileInfo;
import catan.common.resources.ResourceLoader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/2/2015.
 * A list of server connection details.
 */
public class ServerPool implements Iterable<ConnectionInfo> {

    private final PropertiesFileInfo fileInfo;
    private final List<ConnectionInfo> list;

    @SuppressWarnings("InfiniteLoopStatement")
    public ServerPool(PropertiesFileInfo fileInfo) {
        this.fileInfo = fileInfo;
        this.list = new LinkedList<>();
        PropertiesFile file = ResourceLoader.getPropertiesFile(fileInfo);
        try {
            int i = 0;
            while (true)
                list.add(new ConnectionInfo(file, i++));
        } catch (ConfigurationException ignored) {
            //This loop is meant to error out once we run out of elements.
        }
    }

    public void save() {
        PropertiesFile file = ResourceLoader.getPropertiesFile(fileInfo);
        file.clearEntries();
        int i = 0;
        for (ConnectionInfo elt : list)
            elt.save(file, i++);
        ResourceLoader.savePropertiesFile(fileInfo);
    }

    public void add(ConnectionInfo login) {
        if (login != null)
            list.add(0, login);
    }

    public void remove(ConnectionInfo login) { list.remove(login); }

    public void top(ConnectionInfo login) {
        remove(login);
        add(login);
    }

    @Override
    public Iterator<ConnectionInfo> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }
}
