package catan.common.config;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Created by greg on 3/26/16.
 * A ConfigSource that is responsible for actually storing config details.
 */
public abstract class RootConfigSource implements ConfigSource {

    @Override
    public String get(String key) {
        String found = search(key);
        if (found == null)
            throw new ConfigurationException("Unable to read value from config file key " + key);
        return found;
    }

    private String search(String key) {
        //Bottom out on null/empty strings
        if ((key == null) || key.isEmpty())
            throw new ConfigurationException("Unable to read null key");
        //Split the key by the periods in its length
        String[] keyParts = key.split("[.]");
        //Find the key we should be looking for
        String target;
        //If there is more than one part, check the linear redirects.
        if (keyParts.length > 1) {
            String partial = tryRedirect(keyParts[0]);
            for (int i = 1; i < (keyParts.length - 1); i++)
                partial = tryRedirect(partial + "." + keyParts[i]);
            partial += "." + keyParts[keyParts.length - 1];
            target = getEntry(partial);
        } else {
            target = getEntry(key);
        }
        if (target == null)
            return null;
        String recurse = search(target);
        return (recurse == null) ? target : recurse;
    }

    private String tryRedirect(String key) {
        String redirect = getEntry(key);
        return (redirect == null) ? key : redirect;
    }

    @Override
    public ConfigSource narrow(String prefix) {
        return new NarrowedConfigSource(prefix);
    }

    private class NarrowedConfigSource implements ConfigSource {
        private final String prefix;

        private NarrowedConfigSource(String root) {
            prefix = root + ".";
        }

        @Override
        public ConfigSource narrow(String prefix) {
            return new NarrowedConfigSource(this.prefix + prefix);
        }

        @Override
        public String get(String key) {
            return RootConfigSource.this.get(prefix + key);
        }

        @Override
        public Iterator<Entry<String, String>> iterator() {
            return new Iterator<Entry<String, String>>() {
                private final Iterator<Entry<String, String>> parent = RootConfigSource.this.iterator();
                private Entry<String, String> next = findNext();

                private Entry<String, String> findNext() {
                    while (parent.hasNext()) {
                        Entry<String, String> next = parent.next();
                        if (next.getKey().startsWith(prefix)) {
                            return next;
                        }
                    }
                    return null;
                }

                @Override
                public boolean hasNext() {
                    return next != null;
                }

                @Override
                public Entry<String, String> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Entry<String, String> next = this.next;
                    this.next = findNext();
                    return next;
                }
            };
        }
    }

    protected abstract String getEntry(String key);
}
