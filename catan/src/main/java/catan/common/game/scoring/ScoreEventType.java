package catan.common.game.scoring;

import catan.common.event.EventType;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gamestate.DevelopmentCard;

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

    private final Class type;

    ScoreEventType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return type;
    }
}
