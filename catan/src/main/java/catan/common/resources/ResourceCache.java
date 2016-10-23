package catan.common.resources;

import java.util.HashMap;

/**
 * Created by Greg on 1/15/2015.
 * A cache of resources that can be loaded and unloaded dynamically.
 */
public abstract class ResourceCache<X extends ResourceCacheKey, Y> {

    private HashMap<X, Y> cache;
    private int stored;

    public synchronized Y get(X x) throws ResourceLoadException {
        if (!cache.containsKey(x)) {
            Y y = load(x);
            store(x, y);
        }
        return cache.get(x);
    }

    public synchronized void save(X x) throws ResourceLoadException {
        throw new UnsupportedOperationException("Unable to save this resource");
    }

    private synchronized void store(X x, Y y) {
        cache.put(x, y);
        stored++;
    }

    public synchronized void clear() {
        cache = new HashMap<>();
        stored = 0;
    }

    public synchronized int size() {
        return stored;
    }

    protected abstract Y load(X x) throws ResourceLoadException;
}
