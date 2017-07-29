package catan.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 3/26/16.
 * A configuration source to provide dummy config data.
 */
public class DummyConfigSource extends RootConfigSource implements EditableConfigSource {

    private final Map<String, String> entries;

    public DummyConfigSource() {
        entries = new HashMap<>();
    }

    @Override
    protected String getEntry(String key) {
        return entries.get(key);
    }

    @Override
    public void setEntry(String key, String value) {
        if (key != null && value != null)
            entries.put(key, value);
    }

    @Override
    public void save() {
    }

    @Override
    public void clearEntries() {
        entries.clear();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return entries.entrySet().iterator();
    }
}
