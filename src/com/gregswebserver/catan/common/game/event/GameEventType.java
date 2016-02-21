package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.GameSettings;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores data related to managing a game
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType implements EventType {

    Game_Create(GameSettings.class),
    Turn_Advance(null),
    Player_Roll_Dice(Coordinate.class),
    Player_Move_Robber(Coordinate.class),
    Build_Settlement(Coordinate.class),
    Build_City(Coordinate.class),
    Build_Road(Coordinate.class);

    private final Class payloadType;

    GameEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}
