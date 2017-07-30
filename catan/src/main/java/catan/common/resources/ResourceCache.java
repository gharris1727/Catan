package catan.common.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 1/15/2015.
 * A cache of resources that can be loaded and unloaded dynamically.
 */
public abstract class ResourceCache<K extends ResourceCacheKey, V> {

    private Map<K, V> cache;
    private int stored;

    public synchronized V get(K k) {
        if (!cache.containsKey(k)) {
            V v = load(k);
            store(k, v);
        }
        return cache.get(k);
    }

    public synchronized void save(K k) {
        throw new UnsupportedOperationException("Unable to save this resource");
    }

    private synchronized void store(K k, V v) {
        cache.put(k, v);
        stored++;
    }

    public synchronized void clear() {
        cache = new HashMap<>();
        stored = 0;
    }

    public synchronized int size() {
        return stored;
    }

    protected abstract V load(K k);
}
