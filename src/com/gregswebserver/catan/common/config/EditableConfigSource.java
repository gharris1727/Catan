package com.gregswebserver.catan.common.config;

/**
 * Created by greg on 4/1/16.
 * A ConfigSource that can be changed and saved.
 */
public interface EditableConfigSource extends ConfigSource {

    void save();

    void clearEntries();

    void setEntry(String key, String value);
}
