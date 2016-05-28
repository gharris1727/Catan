package com.gregswebserver.catan.common.game.scoring;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;

/**
 * Created by greg on 5/26/16.
 * Type of event that can be scored.
 */
public enum ScoreEventType implements EventType {

    Build_Settlement(Coordinate.class),
    Build_City(Coordinate.class),
    Build_Road(Coordinate.class),
    Buy_Development(DevelopmentCard.class),
    Play_Development(DevelopmentCard.class);

    private final Class payloadType;

    ScoreEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
