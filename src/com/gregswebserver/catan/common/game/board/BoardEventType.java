package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

/**
 * Created by greg on 5/24/16.
 * Types of events that can be applied to a GameBoard.
 */
public enum BoardEventType implements EventType {

    Place_Robber(Coordinate.class),
    Place_Outpost(Coordinate.class),
    Place_Settlement(Coordinate.class),
    Place_City(Coordinate.class),
    Place_Road(Coordinate.class);

    private final Class payloadType;

    BoardEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
