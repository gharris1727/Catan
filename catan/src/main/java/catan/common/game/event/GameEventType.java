package catan.common.game.event;

import catan.common.event.EventType;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;
import catan.common.structure.game.GameSettings;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores data related to managing a game
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType implements EventType {

    Start(GameSettings.class),
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

    private final Class type;

    GameEventType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return type;
    }

}
