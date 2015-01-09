package com.gregswebserver.catan.client.resources;

/**
 * Created by Greg on 1/7/2015.
 * Information about how to load various game modes, that can be loaded by the ResourceLoader
 */
public enum GameInfo {

    Default("default");

    private String name;

    GameInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "/games/" + name + ".txt";
    }

}
