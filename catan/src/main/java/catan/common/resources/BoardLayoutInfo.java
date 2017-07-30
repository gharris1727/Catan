package catan.common.resources;

/**
 * Created by Greg on 1/7/2015.
 * Information about how to load various game modes, that can be loaded by the ResourceLoader
 */
public final class BoardLayoutInfo extends ResourceCacheKey {

    private final boolean dynamic;
    private final String name;
    private final long seed;

    public BoardLayoutInfo(String name) {
        dynamic = false;
        this.name = name;
        seed = 0L;
    }

    public BoardLayoutInfo(long seed) {
        dynamic = true;
        name = "";
        this.seed = seed;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public String getPath() {
        return "games/" + name + ".properties";
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        BoardLayoutInfo other = (BoardLayoutInfo) o;

        if (dynamic != other.dynamic) return false;
        if (seed != other.seed) return false;
        return (name != null) ? name.equals(other.name) : (other.name == null);
    }

    @Override
    public int hashCode() {
        int result = (dynamic ? 1 : 0);
        result = (31 * result) + ((name != null) ? name.hashCode() : 0);
        result = (31 * result) + (int) (seed ^ (seed >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BoardLayoutInfo(" + (dynamic ? seed : getPath()) + ")";
    }
}
