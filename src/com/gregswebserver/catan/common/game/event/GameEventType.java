package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores data related to managing a game
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType implements EventType {

    Start(null),
    Turn_Advance(null),
    Player_Move_Robber(Coordinate.class),
    Build_Settlement(Coordinate.class),
    Build_City(Coordinate.class),
    Build_Road(Coordinate.class),
    Buy_Development(null),
    Offer_Trade(Trade.class),
    Cancel_Trade(null),
    Make_Trade(Trade.class),
    Discard_Resources(EnumCounter.class),
    Steal_Resources(Coordinate.class),
    Play_RoadBuilding(null),
    Play_YearOfPlenty(EnumCounter.class),
    Play_Monopoly(GameResource.class);

    private final Class payloadType;

    GameEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}
