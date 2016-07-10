package catan.common.game;

import catan.common.crypto.Username;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameEventType;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

import java.util.Random;
import java.util.Set;

/**
 * Created by greg on 6/19/16.
 * A generator of random GameEvents for testing.
 */
public class GameEventGenerator {

    private final Random random;
    private final CatanGame game;
    private final Username[] usernames;
    private final Coordinate[] spaces;
    private final Coordinate[] vertices;
    private final Coordinate[] edges;

    public GameEventGenerator(long seed, CatanGame game) {
        random = new Random(seed);
        this.game = game;
        Set<Username> users = game.getTeams().getUsers();
        usernames = users.toArray(new Username[users.size()]);
        Set<Coordinate> spaceCoordinates = game.getBoard().getTileMap().keySet();
        spaces = spaceCoordinates.toArray(new Coordinate[spaceCoordinates.size()]);
        Set<Coordinate> vertexCoordinates = game.getBoard().getTownMap().keySet();
        vertices = vertexCoordinates.toArray(new Coordinate[vertexCoordinates.size()]);
        Set<Coordinate> edgeCoordinates = game.getBoard().getPathMap().keySet();
        edges = edgeCoordinates.toArray(new Coordinate[edgeCoordinates.size()]);
    }

    public GameEvent next() {
        GameEventType type = GameEventType.values()[random.nextInt(GameEventType.values().length)];
        Username origin = choose(usernames);
        Object payload = null;
        switch (type) {
            case Start:
            case Turn_Advance:
            case Buy_Development:
            case Cancel_Trade:
            case Play_RoadBuilding:
                break;
            case Player_Move_Robber:
                payload = choose(spaces);
                break;
            case Build_Settlement:
            case Build_City:
            case Steal_Resources:
                payload = choose(vertices);
                break;
            case Build_Road:
                payload = choose(edges);
                break;
            case Offer_Trade:
                payload = generateTrade(origin);
                break;
            case Make_Trade:
                payload = generateTrade();
                break;
            case Discard_Resources:
            case Play_YearOfPlenty:
                payload = generatePlayerDiscards(game.getPlayer(origin).getInventory());
                break;
            case Play_Monopoly:
                payload = generateGameResource();
                break;
        }
        return new GameEvent(origin, type, payload);
    }

    private <T> T choose(T[] arr) {
        return arr[random.nextInt(arr.length)];
    }

    private GameResource generateGameResource() {
        return choose(GameResource.values());
    }

    private EnumCounter<GameResource> generatePlayerDiscards(EnumCounter<GameResource> inventory) {
        EnumAccumulator<GameResource> counter = new EnumAccumulator<>(GameResource.class);
        for (GameResource r : GameResource.values())
            if (inventory.contains(r,1))
                counter.increment(r, random.nextInt(inventory.get(r) + 1));
        return counter;
    }

    private Trade generateTrade() {
        return generateTrade(choose(usernames));
    }

    private Trade generateTrade(Username username) {
        return new Trade(username,
            new EnumAccumulator<>(GameResource.class,generateGameResource(), random.nextInt(5)),
            new EnumAccumulator<>(GameResource.class,generateGameResource(), random.nextInt(5)));
    }

}
