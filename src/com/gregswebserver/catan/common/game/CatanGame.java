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
import com.gregswebserver.catan.common.game.gameplay.DevelopmentCardRandomizer;
import com.gregswebserver.catan.common.game.gameplay.DiceRollRandomizer;
import com.gregswebserver.catan.common.game.gameplay.TeamTurnManager;
import com.gregswebserver.catan.common.game.gameplay.enums.*;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.game.gameplay.trade.PermanentTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.game.Player;
import com.gregswebserver.catan.common.structure.game.PlayerPool;

import java.util.*;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private final GameRules rules;
    private final PlayerPool players;
    private final GameBoard board;
    private final DiceRollRandomizer diceRolls;
    private final DevelopmentCardRandomizer cards;
    private final TeamTurnManager teamTurns;
    private final Set<Trade> bankTrades;

    public CatanGame(GameSettings settings) {
        rules = settings.gameRules;
        players = settings.playerPool;
        board = settings.generate();
        diceRolls = new DiceRollRandomizer(settings.seed);
        cards = new DevelopmentCardRandomizer(settings.gameRules, settings.seed);
        teamTurns = new TeamTurnManager(settings.seed, players.getTeamSet());
        bankTrades = new HashSet<>();
        for (GameResource target : GameResource.values())
            for (GameResource source : GameResource.values())
                bankTrades.add(new PermanentTrade(source, target, 4));
    }

    @Override
    public boolean test(GameEvent event) {
        Player player = players.getPlayer(event.getOrigin());
        Coordinate c = null;
        if (event.getPayload() instanceof Coordinate)
            c = (Coordinate) event.getPayload();
        if (player.getTeam() != teamTurns.getCurrentTeam())
            return false;
        if (teamTurns.starting())
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
                case Offer_Trade:
                case Make_Trade:
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
                    return player.canMakePurchase(Purchase.DevelopmentCard) && cards.hasNext();
                case Offer_Trade:
                    return player.canFulfillTrade((Trade) event.getPayload());
                case Make_Trade:
                    return player.canMakeTrade((Trade) event.getPayload());
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
                if (!teamTurns.starting())
                    player.makePurchase(Purchase.Settlement);
                break;
            case Build_City:
                coordinate = (Coordinate) event.getPayload();
                board.buildCity(coordinate, team);
                if (!teamTurns.starting())
                    player.makePurchase(Purchase.City);
                break;
            case Build_Road:
                coordinate = (Coordinate) event.getPayload();
                board.buildRoad(coordinate, team);
                if (player.state == Player.PlayerState.Road_1)
                    player.state = Player.PlayerState.Waiting;
                if (player.state == Player.PlayerState.Road_2)
                    player.state = Player.PlayerState.Playing;
                if (!teamTurns.starting())
                    player.makePurchase(Purchase.Road);
                break;
            case Player_Move_Robber:
                coordinate = (Coordinate) event.getPayload();
                player.playDevelopmentCard(DevelopmentCard.Knight);
                board.moveRobber(coordinate);
                break;
            case Turn_Advance:
                player.endTurn();
                if (teamTurns.starting()) {
                    if (player.state == Player.PlayerState.Waiting)
                        player.state = Player.PlayerState.Settlement_2;
                    if (player.state == Player.PlayerState.Playing) {
                        for (Tile t : board.getAdjacentTiles(player.lastSettlement.getPosition()))
                            player.addResource(t,1);
                    }
                }
                teamTurns.advanceTurn();
                if (!teamTurns.starting()) {
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
            case Buy_Development:
                player.addDevelopmentCard(cards.next());
                player.makePurchase(Purchase.DevelopmentCard);
                break;
            case Offer_Trade:
                player.setTrade((TemporaryTrade) event.getPayload());
                break;
            case Make_Trade:
                Trade trade = (Trade) event.getPayload();
                player.makeTrade(trade);
                if (trade instanceof TemporaryTrade) {
                    Player seller = players.getPlayer(((TemporaryTrade) trade).seller);
                    seller.fulfillTrade(trade);
                    seller.setTrade(null);
                }
                break;
        }
    }

    public GameBoard getBoard() {
        return board;
    }

    public PlayerPool getTeams() {
        return players;
    }

    public boolean isLocalPlayerActive() {
        return players.getLocalPlayer().getTeam() == teamTurns.getCurrentTeam();
    }

    public List<Trade> getTrades() {
        Player local = players.getLocalPlayer();
        List<Trade> trades = new ArrayList<>();
        for (Username u : players.getAllUsers()) {
            Trade trade = players.getPlayer(u).getTrade();
            if (trade != null && local.canMakeTrade(trade))
                trades.add(trade);
        }
        for (TradingPostType t : board.getTrades(local.getTeam())) {
            for (Trade trade : t.getTrades())
                if (local.canMakeTrade(trade))
                    trades.add(trade);
        }
        for (Trade trade : bankTrades)
            if (local.canMakeTrade(trade))
                trades.add(trade);
        Collections.sort(trades);
        return trades;
    }
}
