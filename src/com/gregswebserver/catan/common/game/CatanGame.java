package com.gregswebserver.catan.common.game;

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
import com.gregswebserver.catan.common.game.gameplay.DiceRollRandomizer;
import com.gregswebserver.catan.common.game.gameplay.TeamTurnRandomizer;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.game.Player;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private final GameRules rules;
    private final PlayerPool players;
    private final GameBoard board;
    private final DiceRollRandomizer diceRolls;
    private final TeamTurnRandomizer teamTurns;

    public CatanGame(GameSettings settings) {
        rules = settings.gameRules;
        players = settings.playerPool;
        board = settings.generate();
        diceRolls = new DiceRollRandomizer(settings.seed);
        teamTurns = new TeamTurnRandomizer(settings.seed, players.getTeamSet());
    }

    @Override
    public boolean test(GameEvent event) {
        boolean turnActive = players.getPlayer(event.getOrigin()).getTeam() == teamTurns.getActiveTeam();
        switch (event.getType()) {
            case Build_Settlement:
                return turnActive && board.getTown((Coordinate) event.getPayload()).getTeam() == Team.None;
            case Build_City:
                Town b = board.getTown((Coordinate) event.getPayload());
                return turnActive && (b instanceof Settlement && b.getTeam() == players.getPlayer(event.getOrigin()).getTeam());
            case Build_Road:
                return turnActive && board.getPath((Coordinate) event.getPayload()).getTeam() == Team.None;
            case Player_Move_Robber:
                Tile tile = board.getTile((Coordinate) event.getPayload());
                return turnActive && (tile instanceof ResourceTile);
            case Turn_Advance:
                return turnActive;
        }
        return false;
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        Player player = players.getPlayer(origin);
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
            case Player_Move_Robber:
                coordinate = (Coordinate) event.getPayload();
                board.moveRobber(coordinate);
                break;
            case Turn_Advance:
                teamTurns.advanceTurn();
                DiceRoll roll = diceRolls.next();
                List<Coordinate> tileCoords = board.getActiveTiles(roll);
                if (tileCoords != null && !tileCoords.isEmpty()) {
                    for (Coordinate t : tileCoords) {
                        GameResource resource = ((ResourceTile) board.getTile(t)).getResource();
                        if (resource != null) {
                            for (Coordinate v : board.getAdjacentVertices(t)) {
                                Town town = board.getTown(v);
                                if (town != null && town.getTeam() != Team.None) {
                                    List<Player> players = this.players.getTeamPlayers(town.getTeam());
                                    for (Player p : players) {
                                        Map<GameResource, Integer> inventory = p.getInventory();
                                        int resCount = inventory.get(resource);
                                        if (town instanceof Settlement)
                                            resCount += rules.getSettlementResources();
                                        if (town instanceof City)
                                            resCount += rules.getCityResources();
                                        inventory.put(resource, resCount);
                                    }
                                }
                            }
                        }
                    }
                }
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
        return players;
    }

    public boolean isLocalPlayerActive() {
        return players.getLocalPlayer().getTeam() == teamTurns.getActiveTeam();
    }
}
