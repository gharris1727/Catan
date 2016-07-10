package catan.common.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 3/26/16.
 * A configuration source to provide dummy config data.
 */
public class DummyConfigSource extends RootConfigSource {

    private final Map<String, String> entries;

    public DummyConfigSource() {
        entries = new HashMap<>();
    }

    @Override
    protected String getEntry(String key) {
        return entries.get(key);
    }

    public void setEntry(String key, String value) {
        if (key != null && value != null)
            entries.put(key, value);
    }

    public void clearEntries() {
        entries.clear();
    }
}
