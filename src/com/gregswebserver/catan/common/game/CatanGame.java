package com.gregswebserver.catan.common.game;

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
import com.gregswebserver.catan.common.network.Identity;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private GameBoard board;
    private HashMap<Identity, Player> players;
    private Player localPlayer;

    public CatanGame() {
    }

    public boolean test(GameEvent event) {
        switch (event.getType()) {
            //TODO: turn checks
            case Build_Settlement:
                return board.getBuilding((Coordinate) event.getPayload()) == null;
            case Build_City:
                Town b = board.getBuilding((Coordinate) event.getPayload());
                return (b instanceof Settlement && b.getTeam() == players.get(event.getOrigin()).getTeam());
            case Build_Road:
                return board.getPath((Coordinate) event.getPayload()) == null;
            case Player_Select_Location:
                return true;
            case Player_Move_Robber:
                Tile tile = board.getTile((Coordinate) event.getPayload());
                return (tile instanceof ResourceTile);
            case Player_Roll_Dice:
                //TODO: turn checks
                return true;
        }
        return false;
    }

    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Identity origin = event.getOrigin();
        Player player = players.get(origin);
        Team team = player.getTeam();
        Coordinate coordinate;
        switch (event.getType()) {
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
            case Player_Select_Location:
                coordinate = (Coordinate) event.getPayload();
                player.setSelected(coordinate);
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

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public Dimension getBoardSize() {
        return board.getSize();
    }
}
