package catan.common.game.board.hexarray;

import catan.common.util.Direction;

/**
 * Created by Greg on 8/17/2014.
 * Exception thrown when a direction is invalid in a given context.
 */
public class IllegalDirectionException extends Exception {

    public IllegalDirectionException(Direction direction) {
        super("Direction " + direction + " is invalid in this context.");
    }
}
