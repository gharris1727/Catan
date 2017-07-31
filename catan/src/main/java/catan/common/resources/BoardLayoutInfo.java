package catan.common.resources;

/**
 * Created by Greg on 1/7/2015.
 * Information about how to load various game modes, that can be loaded by the ResourceLoader
 */
public final class BoardLayoutInfo extends ResourceCacheKey {

    private final String name;

    public BoardLayoutInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "games/" + name + ".properties";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        BoardLayoutInfo other = (BoardLayoutInfo) o;

        return (name != null) ? name.equals(other.name) : (other.name == null);
    }

    @Override
    public int hashCode() {
        return ((name != null) ? name.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "BoardLayoutInfo(" + getPath() + ")";
    }
}
