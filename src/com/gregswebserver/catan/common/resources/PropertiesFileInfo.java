package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 2/28/16.
 * Information on how to load a properties file from disk.
 */
public class PropertiesFileInfo {

    private final String path;
    private final String comment;

    public PropertiesFileInfo(String path, String comment) {

        this.path = path;
        this.comment = comment;
    }

    public String getPath() {
        return path;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "PropertiesFileInfo(" + path + "/" + comment + ")";
    }
}
