package catan.common.resources;

import catan.ExternalResource;

/**
 * Created by greg on 2/28/16.
 * Information on how to load a properties file from disk.
 */
public final class PropertiesFileInfo extends ResourceCacheKey {

    private final String path;
    private final String comment;

    public PropertiesFileInfo(String path, String comment) {
        this.path = path;
        this.comment = comment;
    }

    public String getPath() {
        return ExternalResource.getConfigDirectory() + path;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        PropertiesFileInfo other = (PropertiesFileInfo) o;

        if ((path != null) ? !path.equals(other.path) : (other.path != null)) return false;
        return (comment != null) ? comment.equals(other.comment) : (other.comment == null);

    }

    @Override
    public int hashCode() {
        int result = (path != null) ? path.hashCode() : 0;
        result = (31 * result) + ((comment != null) ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PropertiesFileInfo(" + path + "/" + comment + ")";
    }
}
