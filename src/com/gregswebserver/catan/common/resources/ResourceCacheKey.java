package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 3/21/16.
 * The superclass of any object to be used to access the ResourceCache.
 */
public abstract class ResourceCacheKey {

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
