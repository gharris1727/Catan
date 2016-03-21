package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 1/28/16.
 * A cache lookup object for different GameRules objects.
 */
public class GameRulesInfo {

    private final String name;

    public GameRulesInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "rules/" + name + ".properties";
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof GameRulesInfo) {
            GameRulesInfo info = (GameRulesInfo) o;
            return name.equals(info.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "GameRulesInfo(" + getPath() + ")";
    }
}
