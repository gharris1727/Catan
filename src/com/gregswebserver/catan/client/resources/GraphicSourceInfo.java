package com.gregswebserver.catan.client.resources;

import com.gregswebserver.catan.ExternalResource;

/**
 * Created by Greg on 1/6/2015.
 * A list of GraphicSource resources that can be loaded.
 */
public class GraphicSourceInfo {

    private final String path;

    public GraphicSourceInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return ExternalResource.getResourceDataDirectory() + path;
    }

    @Override
    public String toString() {
        return "GraphicSourceInfo(" + path + ")";
    }
}
