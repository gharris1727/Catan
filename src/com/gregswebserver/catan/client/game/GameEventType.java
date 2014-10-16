package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.event.EventPayloadException;
import com.gregswebserver.catan.event.EventType;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.gameplay.trade.Trade;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores data related to managing a game
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType implements EventType {

    Build_Settlement(Coordinate.class),
    Build_City(Coordinate.class),
    Build_Road(Coordinate.class),
    Trade_Offer(Trade.class),
    Trade_Accept(Trade.class),
    Trade_Bank(Trade.class),
    Player_Select_Location(Coordinate.class),
    Player_Move_Robber(Coordinate.class),
    Player_Roll_Dice(Coordinate.class);

    private Class payloadType;

    GameEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public void checkPayload(Object o) {
        if (o != null && o.getClass().isAssignableFrom(payloadType))
            throw new EventPayloadException(o, payloadType);
    }

    public Class getType() {
        return payloadType;
    }

}
