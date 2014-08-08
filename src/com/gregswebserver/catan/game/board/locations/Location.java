package com.gregswebserver.catan.game.board.locations;

import java.util.Random;

/**
 * Created by Greg on 8/8/2014.
 * Generic Location class to meet HashMap compatibility.
 */
public abstract class Location {

    private int uid;

    public Location() {
        this((new Random()).nextInt());
    }

    public Location(int uid) {
        this.uid = uid;
    }

    public Location(Location a, Location b) {
        this(a.hashCode() + b.hashCode());
    }

    public Location(Location a, Location b, Location c) {
        this(a.hashCode() + b.hashCode() + c.hashCode());
    }

    public boolean equals(Object o) {
        return o.hashCode() == hashCode();
    }

    public int hashCode() {
        return uid;
    }
}
