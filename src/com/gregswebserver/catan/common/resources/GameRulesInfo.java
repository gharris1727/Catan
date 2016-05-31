package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 1/28/16.
 * A cache lookup object for different StaticGameRules objects.
 */
public final class GameRulesInfo extends ResourceCacheKey {

    private final String name;

    public GameRulesInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "rules/" + name + ".properties";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRulesInfo that = (GameRulesInfo) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GameRulesInfo(" + getPath() + ")";
    }
}
