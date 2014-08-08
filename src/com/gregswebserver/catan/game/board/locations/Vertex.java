package com.gregswebserver.catan.game.board.locations;

/**
 * Created by Greg on 8/8/2014.
 * Location to store the intersections of three tiles and three edges.
 */
public class Vertex extends Location {

    public Vertex(Space a, Space b, Space c) {
        super(a, b, c);
    }
}
