package com.gregswebserver.catan.game.board.locations;

/**
 * Created by Greg on 8/8/2014.
 * Location on the game board that represents the edge between two tiles. Paths are placeable here.
 */
public class Edge extends Location {

    public Edge(Space a, Space b) {
        super(a, b);
    }
}
