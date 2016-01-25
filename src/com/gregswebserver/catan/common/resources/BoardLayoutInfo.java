package com.gregswebserver.catan.common.resources;

/**
 * Created by Greg on 1/7/2015.
 * Information about how to load various game modes, that can be loaded by the ResourceLoader
 */
public class BoardLayoutInfo {

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

    public String getPath() {
        return "config/games/" + name + ".properties";
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + 31 * (int) seed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof BoardLayoutInfo) {
            BoardLayoutInfo info = (BoardLayoutInfo) o;
            return name.equals(info.name) && seed == info.seed;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BoardLayoutInfo(" + name + ")";
    }

    public boolean isDynamic() {
        return dynamic;
    }
}
