package com.gregswebserver.catan.common.config;

import com.gregswebserver.catan.ExternalResource;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by greg on 1/18/16.
 * Class to access configuration details stored in an external file.
 */
public class PropertiesFile extends RootConfigSource implements EditableConfigSource, Iterable<Map.Entry<String,String>>{

    private final String path;
    private final String comment;
    private final Properties config;
    private boolean needsSaving;

    public PropertiesFile(String path, String comment) {
        this.path = path;
        this.comment = comment;
        config = new Properties();
        needsSaving = false;
        //Load defaults that come packaged with the program
        try {
            config.load(ExternalResource.getStaticResource(path));
        } catch (IOException e) {
            throw new ConfigurationException("Unable to load default config", e);
        }
        //Override the defaults with user configurable versions
        try {
            config.load(new BufferedReader(new FileReader(ExternalResource.getUserResource(path))));
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
                config.store(new BufferedWriter(new FileWriter(file)), comment);
                needsSaving = false;
            } catch (IOException e) {
                throw new ConfigurationException("Unable to save configuration data.");
            }
        }
    }

    @Override
    protected String getEntry(String key) {
        return config.getProperty(key);
    }

    @Override
    public void setEntry(String key, String value) {
        if (key != null && value != null) {
            config.setProperty(key, value);
            needsSaving = true;
        }
    }

    @Override
    public void clearEntries() {
        config.clear();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        //Get the iterator from the internal storage
        Iterator<Map.Entry<Object, Object>> objIterator = config.entrySet().iterator();
        //We cant cast, so we need to cast everything individually.
        return new Iterator<Map.Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return objIterator.hasNext();
            }

            @Override
            public Map.Entry<String, String> next() {
                Map.Entry<Object, Object> objEntry = objIterator.next();
                return new Map.Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return (String) objEntry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return (String) objEntry.getValue();
                    }

                    @Override
                    public String setValue(String s) {
                        return (String) objEntry.setValue(s);
                    }
                };
            }
        };
    }
}
