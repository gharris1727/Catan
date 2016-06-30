package com.gregswebserver.catan.test.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.util.Random;
import java.util.Set;

/**
 * Created by greg on 6/19/16.
 * A generator of random GameEvents for testing.
 */
public class GameEventGenerator {

    private final Random random;
    private final int nEvents;
    private final Username[] players;
    private final int nPlayers;
    private final int nResources;

    public GameEventGenerator(long seed, Set<Username> players) {
        random = new Random(seed);
        nEvents = GameEventType.values().length;
        this.players = players.toArray(new Username[players.size()]);
        nPlayers = this.players.length;
        nResources = GameResource.values().length;
    }

    public GameEvent next() {
        GameEventType type = GameEventType.values()[random.nextInt(nEvents)];
        Username origin = generateUsername();
        Object payload = null;
        switch (type) {
            case Start:
            case Turn_Advance:
            case Buy_Development:
            case Cancel_Trade:
            case Play_RoadBuilding:
                break;
            case Player_Move_Robber:
            case Build_Settlement:
            case Build_City:
            case Build_Road:
            case Steal_Resources:
                payload = generateCoordinate();
                break;
            case Offer_Trade:
                payload = generateTrade(origin);
                break;
            case Make_Trade:
                payload = generateTrade();
                break;
            case Discard_Resources:
            case Play_YearOfPlenty:
                payload = generateEnumCounter();
                break;
            case Play_Monopoly:
                payload = generateGameResource();
                break;
        }
        return new GameEvent(origin, type, payload);
    }

    private GameResource generateGameResource() {
        return GameResource.values()[random.nextInt(nResources)];
    }

    private Username generateUsername() {
        return players[random.nextInt(nPlayers)];
    }

    private EnumCounter<GameResource> generateEnumCounter() {
        EnumAccumulator<GameResource> counter = new EnumAccumulator<>(GameResource.class);
        for (GameResource r : GameResource.values())
            counter.increment(r, random.nextInt(5));
        return counter;
    }

    private Trade generateTrade() {
       return generateTrade(generateUsername());
    }
    private Trade generateTrade(Username username) {
        return new Trade(username,
            new EnumAccumulator<>(GameResource.class,generateGameResource(), random.nextInt(5)),
            new EnumAccumulator<>(GameResource.class,generateGameResource(), random.nextInt(5)));
    }

    private Coordinate generateCoordinate() {
        return new Coordinate(random.nextInt(50), random.nextInt(10));
    }
}
