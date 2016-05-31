package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.ExternalResource;

/**
 * Created by Greg on 1/7/2015.
 * Information about how to load various game modes, that can be loaded by the ResourceLoader
 */
public final class BoardLayoutInfo extends ResourceCacheKey {

    private final boolean dynamic;
    private final String name;
    private final long seed;

    public BoardLayoutInfo(String name) {
        this.dynamic = false;
        this.name = name;
        this.seed = 0L;
    }

    public BoardLayoutInfo(long seed) {
        this.dynamic = true;
        this.name = "";
        this.seed = seed;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public String getPath() {
        return ExternalResource.getConfigDirectory() + "games/" + name + ".properties";
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardLayoutInfo that = (BoardLayoutInfo) o;

        if (dynamic != that.dynamic) return false;
        if (seed != that.seed) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = (dynamic ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (seed ^ (seed >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BoardLayoutInfo(" + (dynamic ? seed : getPath()) + ")";
    }
}
