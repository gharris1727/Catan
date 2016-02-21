package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.structure.PlayerPool;

import java.awt.*;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private final GameSettings settings;
    private final GameBoard board;

    public CatanGame(GameSettings settings) {
        this.settings = settings;
        board = settings.generate();
    }

    @Override
    public boolean test(GameEvent event) {
        switch (event.getType()) {
            //TODO: turn checks
            case Game_Create:
                return false;
            case Build_Settlement:
                return board.getBuilding((Coordinate) event.getPayload()).getTeam() == Team.None;
            case Build_City:
                Town b = board.getBuilding((Coordinate) event.getPayload());
                return (b instanceof Settlement && b.getTeam() == settings.teams.getPlayer(event.getOrigin()).getTeam());
            case Build_Road:
                return board.getPath((Coordinate) event.getPayload()).getTeam() == Team.None;
            case Player_Move_Robber:
                Tile tile = board.getTile((Coordinate) event.getPayload());
                return (tile instanceof ResourceTile);
            case Turn_Advance:
                break;
            case Player_Roll_Dice:
                //TODO: turn checks
                return true;
        }
        return false;
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        Player player = settings.teams.getPlayer(origin);
        Team team = player.getTeam();
        Coordinate coordinate;
        switch (event.getType()) {
            case Game_Create:
                throw new IllegalStateException();
            case Build_Settlement:
                coordinate = (Coordinate) event.getPayload();
                Settlement settlement = new Settlement(team);
                board.setBuilding(coordinate, settlement);
                break;
            case Build_City:
                coordinate = (Coordinate) event.getPayload();
                City city = new City(team);
                board.setBuilding(coordinate, city);
                break;
            case Build_Road:
                coordinate = (Coordinate) event.getPayload();
                Road road = new Road(team);
                board.setPath(coordinate, road);
                break;
            case Player_Move_Robber:
                coordinate = (Coordinate) event.getPayload();
                board.moveRobber(coordinate);
                break;
            case Player_Roll_Dice:
                break;
            case Turn_Advance:
                break;
        }
    }

    public GameBoard getBoard() {
        return board;
    }

    public Dimension getBoardSize() {
        return board.getSize();
    }

    public PlayerPool getTeams() {
        return settings.teams;
    }
}
