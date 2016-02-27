package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.DiceRollRandomizer;
import com.gregswebserver.catan.common.game.gameplay.TeamTurnManager;
import com.gregswebserver.catan.common.game.gameplay.enums.DevelopmentCard;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Purchase;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.game.Player;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private final GameRules rules;
    private final PlayerPool players;
    private final GameBoard board;
    private final DiceRollRandomizer diceRolls;
    private final TeamTurnManager teamTurns;
    private final Iterator<Team> firstRound;
    private final Iterator<Team> secondRound;
    private Team activeTeam;
    private boolean starting;

    public CatanGame(GameSettings settings) {
        rules = settings.gameRules;
        players = settings.playerPool;
        board = settings.generate();
        diceRolls = new DiceRollRandomizer(settings.seed);
        teamTurns = new TeamTurnManager(settings.seed, players.getTeamSet());
        firstRound = teamTurns.forward();
        secondRound = teamTurns.reverse();
        activeTeam = firstRound.next();
        starting = true;
    }

    @Override
    public boolean test(GameEvent event) {
        Player player = players.getPlayer(event.getOrigin());
        Coordinate c = null;
        if (event.getPayload() instanceof Coordinate)
            c = (Coordinate) event.getPayload();
        if (player.getTeam() != activeTeam)
            return false;
        if (starting)
            switch (event.getType()) {
                case Turn_Advance:
                    return player.state == Player.PlayerState.Waiting ||
                            player.state == Player.PlayerState.Playing;
                case Player_Move_Robber:
                    return false;
                case Build_Settlement:
                    return
                        (player.state == Player.PlayerState.Settlement_1 ||
                        player.state == Player.PlayerState.Settlement_2) &&
                        board.canBuildInitialSettlement(c);
                case Build_City:
                    return false;
                case Build_Road:
                    return
                        (player.state == Player.PlayerState.Road_1 ||
                        player.state == Player.PlayerState.Road_2) &&
                        board.canBuildRoad(c, player.getTeam());
                case Buy_Development:
                    return false;
            }
        else
            switch (event.getType()) {
                case Turn_Advance:
                    return true;
                case Player_Move_Robber:
                    return board.canRobTile(c) && player.canPlayDevelopmentCard(DevelopmentCard.Knight);
                case Build_Settlement:
                    return board.canBuildSettlement(c, player.getTeam()) && player.canMakePurchase(Purchase.Settlement);
                case Build_City:
                    return board.canBuildCity(c, player.getTeam()) && player.canMakePurchase(Purchase.City);
                case Build_Road:
                    return board.canBuildRoad(c, player.getTeam()) && player.canMakePurchase(Purchase.Road);
                case Buy_Development:
                    return player.canMakePurchase(Purchase.DevelopmentCard);
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
                player.lastSettlement = board.buildSettlement(coordinate, team);
                if (player.state == Player.PlayerState.Settlement_1)
                    player.state = Player.PlayerState.Road_1;
                if (player.state == Player.PlayerState.Settlement_2)
                    player.state = Player.PlayerState.Road_2;
                if (!starting)
                    player.makePurchase(Purchase.Settlement);
                break;
            case Build_City:
                coordinate = (Coordinate) event.getPayload();
                board.buildCity(coordinate, team);
                if (!starting)
                    player.makePurchase(Purchase.City);
                break;
            case Build_Road:
                coordinate = (Coordinate) event.getPayload();
                board.buildRoad(coordinate, team);
                if (player.state == Player.PlayerState.Road_1)
                    player.state = Player.PlayerState.Waiting;
                if (player.state == Player.PlayerState.Road_2)
                    player.state = Player.PlayerState.Playing;
                if (!starting)
                    player.makePurchase(Purchase.Road);
                break;
            case Player_Move_Robber:
                coordinate = (Coordinate) event.getPayload();
                board.moveRobber(coordinate);
                break;
            case Turn_Advance:
                player.endTurn();
                if (starting) {
                    if (player.state == Player.PlayerState.Waiting)
                        player.state = Player.PlayerState.Settlement_2;
                    if (player.state == Player.PlayerState.Playing) {
                        for (Tile t : board.getAdjacentTiles(player.lastSettlement.getPosition()))
                            player.addResource(t,1);
                    }
                    if (firstRound.hasNext())
                        activeTeam = firstRound.next();
                    else if (secondRound.hasNext())
                        activeTeam = secondRound.next();
                    else
                        starting = false;
                }
                if (!starting) {
                    activeTeam = teamTurns.advanceTurn();
                    DiceRoll roll = diceRolls.next();
                    List<Coordinate> spaces = board.getActiveTiles(roll);
                    if (spaces != null && !spaces.isEmpty()) {
                        for (Coordinate space : spaces) {
                            for (Town town : board.getAdjacentTowns(space)) {
                                if (town != null && town.getTeam() != Team.None) {
                                    List<Player> players = this.players.getTeamPlayers(town.getTeam());
                                    for (Player p : players) {
                                        if (town instanceof Settlement)
                                            p.addResource(board.getTile(space), rules.getSettlementResources());
                                        if (town instanceof City)
                                            p.addResource(board.getTile(space), rules.getCityResources());
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
        return players.getLocalPlayer().getTeam() == activeTeam;
    }

    public enum GameState {
        IntialPlacement, ReversePlacement, Regular
    }
}
