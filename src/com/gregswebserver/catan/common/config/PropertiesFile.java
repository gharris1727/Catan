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
    private Source source;

    public PropertiesFile(String path, String comment) {
        this.path = path;
        this.comment = comment;
        config = new Properties();
        needsSaving = false;
        source = Source.UNLOADED;
        try {
            config.load(new BufferedReader(new FileReader(
                    ExternalResource.getCurrentDirectory() +
                            ExternalResource.getConfigDirectory() +
                            path)));
            source = Source.CURRENTDIR;
        } catch (IOException a) {
            try {
                config.load(new BufferedReader(new FileReader(
                        ExternalResource.getUserDataDirectory() +
                                ExternalResource.getConfigDirectory() +
                                path)));
                source = Source.USERDIR;
            } catch (IOException b) {
                try {
                    config.load(ExternalResource.class.getResourceAsStream(
                            ExternalResource.getResourceDataDirectory() +
                                    ExternalResource.getConfigDirectory() +
                                    path));
                    source = Source.DEFAULT;
                } catch (IOException c) {
                    throw new ConfigurationException("Unable to save configuration data.");
                }
            }
        }
    }

    @Override
    public void save() {
        if (needsSaving) {
            try {
                File file;
                switch (source) {
                    case CURRENTDIR:
                        file = new File(ExternalResource.getCurrentDirectory() + path);
                        break;
                    default:
                        file = new File(ExternalResource.getUserDataDirectory() + path);
                        break;
                }
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

    private enum Source {
        UNLOADED, CURRENTDIR, USERDIR, DEFAULT
    }
}
