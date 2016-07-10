package catan.common.resources;

import catan.ExternalResource;

/**
 * Created by Greg on 1/6/2015.
 * A list of GraphicSource resources that can be loaded.
 */
public final class GraphicSourceInfo extends ResourceCacheKey{

    private final String path;

    public GraphicSourceInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return ExternalResource.getGraphicsDirectory() + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphicSourceInfo that = (GraphicSourceInfo) o;

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GraphicSourceInfo(" + getPath() + ")";
    }
}
