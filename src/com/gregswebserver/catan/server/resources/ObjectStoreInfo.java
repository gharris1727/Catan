package com.gregswebserver.catan.server.resources;

/**
 * Created by Greg on 1/8/2015.
 * Offline object storage for the server.
 */
public enum ObjectStoreInfo {

    UserInfo("users");

    private final String name;

    ObjectStoreInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "/server/" + name + ".obj";
    }

}
