package catan.common.resources;

import catan.ExternalResource;
import catan.common.config.ConfigurationException;
import catan.common.config.EditableConfigSource;
import catan.common.config.RootConfigSource;

import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Created by greg on 1/18/16.
 * Class to access configuration details stored in an external file.
 */
public class PropertiesFile extends RootConfigSource implements EditableConfigSource {

    private final String path;
    private final String comment;
    private final Properties file;
    private boolean needsSaving;

    public PropertiesFile(String path, String comment) {
        this.path = path;
        this.comment = comment;
        file = new Properties();
        needsSaving = false;
        //Load defaults that come packaged with the program
        try {
            file.load(ExternalResource.getStaticResource(path));
        } catch (IOException e) {
            throw new ConfigurationException("Unable to load default config", e);
        }
        //Override the defaults with user configurable versions
        try {
            file.load(new BufferedReader(new FileReader(ExternalResource.getUserResource(path))));
        } catch (FileNotFoundException ignored) {
            //We ignore errors when the file does not exist
        } catch (IOException e) {
            throw new ConfigurationException("Unable to read overridden settings", e);
        }
    }

    @Override
    public void save() {
        if (needsSaving) {
            try {
                File file = ExternalResource.getUserResource(path);
                if (!file.exists() && file.getParentFile().mkdirs()) {
                    if (!file.createNewFile())
                        throw new IOException("Unable to create new file to save.");
                }
                this.file.store(new BufferedWriter(new FileWriter(file)), comment);
                needsSaving = false;
            } catch (IOException e) {
                throw new ConfigurationException("Unable to save configuration data.", e);
            }
        }
    }

    @Override
    protected String getEntry(String key) {
        return file.getProperty(key);
    }

    @Override
    public void setEntry(String key, String value) {
        if ((key != null) && (value != null)) {
            file.setProperty(key, value);
            needsSaving = true;
        }
    }

    @Override
    public void clearEntries() {
        file.clear();
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        //Get the iterator from the internal storage
        Iterator<Entry<Object, Object>> objIterator = file.entrySet().iterator();
        //We cant cast, so we need to cast everything individually.
        return new Iterator<Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return objIterator.hasNext();
            }

            @Override
            public Entry<String, String> next() {
                Entry<Object, Object> objEntry = objIterator.next();
                return new Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return (String) objEntry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return (String) objEntry.getValue();
                    }

                    @Override
                    public String setValue(String v) {
                        return (String) objEntry.setValue(v);
                    }
                };
            }
        };
    }
}
