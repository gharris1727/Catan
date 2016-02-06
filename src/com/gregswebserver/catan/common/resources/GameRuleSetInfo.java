package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 1/28/16.
 * A cache lookup object for different GameRuleSet objects.
 */
public class GameRuleSetInfo {

    private final String name;

    public GameRuleSetInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "config/rules/" + name + ".properties";
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof GameRuleSetInfo) {
            GameRuleSetInfo info = (GameRuleSetInfo) o;
            return name.equals(info.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "BoardLayoutInfo(" + name + ")";
    }
}
