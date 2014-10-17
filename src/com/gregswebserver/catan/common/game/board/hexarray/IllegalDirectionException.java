package com.gregswebserver.catan.common.game.board.hexarray;

/**
 * Created by Greg on 8/17/2014.
 * Exception thrown when a direction is invalid in a given context.
 */
public class IllegalDirectionException extends Exception {

    private final Direction direction;

    public IllegalDirectionException(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String getMessage() {
        return "Direction " + direction + " is invalid in this context.";
    }
}
