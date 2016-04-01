package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.gameplay.TeamTurnManager;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.game.gameplay.random.DevelopmentCardRandomizer;
import com.gregswebserver.catan.common.game.gameplay.random.DiceRollRandomizer;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.game.gameplay.trade.PermanentTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.game.Player;
import com.gregswebserver.catan.common.structure.game.PlayerPool;
import org.junit.ComparisonFailure;

import java.util.*;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements ReversibleEventConsumer<GameEvent> {

    private final GameRules rules;
    private final GameBoard board;
    private final PlayerPool players;
    private final DiceRollRandomizer diceRolls;
    private final DevelopmentCardRandomizer cards;
    private final TeamTurnManager teamTurns;
    private final Stack<GameEvent> eventStack;
    private final Set<Trade> bankTrades;

    public CatanGame(GameSettings settings) {
        rules = settings.gameRules;
        board = settings.generate();
        players = new PlayerPool(settings.playerTeams);
        diceRolls = new DiceRollRandomizer(settings.seed);
        cards = new DevelopmentCardRandomizer(settings.gameRules, settings.seed);
        teamTurns = new TeamTurnManager(settings.seed, players.getTeamSet());
        eventStack = new Stack<>();
        eventStack.push(new GameEvent(null, GameEventType.Start, null));
        bankTrades = new HashSet<>();
        for (GameResource target : GameResource.values())
            for (GameResource source : GameResource.values())
                if (target != source)
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
        switch (event.getType()) {
            case Turn_Advance:
                return player.canAdvanceTurn();
            case Player_Move_Robber:
                return (teamTurns.isRobberActive() || player.canMoveRobber()) && board.canRobTile(c);
            case Build_Settlement:
                if (teamTurns.starting())
                    return player.canBuildSettlement() && board.canBuildInitialSettlement(c);
                else
                    return player.canBuildSettlement() && board.canBuildSettlement(c, player.getTeam());
            case Build_City:
                return player.canBuildCity() && board.canBuildCity(c, player.getTeam());
            case Build_Road:
                return player.canBuildRoad() && board.canBuildRoad(c, player.getTeam());
            case Buy_Development:
                return player.canBuyDevelopmentCard() && cards.hasNext();
            case Offer_Trade:
                return player.canFulfillTrade((TemporaryTrade) event.getPayload());
            case Make_Trade:
                if (event.getPayload() instanceof TemporaryTrade) {
                    TemporaryTrade trade = (TemporaryTrade) event.getPayload();
                    return player.canMakeTrade(trade) && players.getPlayer(trade.seller).canFulfillTrade(trade);
                } else
                    return player.canMakeTrade((Trade) event.getPayload());
        }
        return false;
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException("Cannot execute game event", event);
        eventStack.push(event);
        Player player = players.getPlayer(event.getOrigin());
        Team team = player.getTeam();
        Coordinate coordinate = null;
        if (event.getPayload() instanceof Coordinate)
            coordinate = (Coordinate) event.getPayload();
        switch (event.getType()) {
            case Turn_Advance:
                player.advanceTurn();
                teamTurns.next();
                if (!teamTurns.starting()) {
                    List<Coordinate> spaces = board.getActiveTiles(diceRolls.next());
                    if (spaces != null && !spaces.isEmpty()) {
                        for (Coordinate space : spaces) {
                            for (Town town : board.getAdjacentTowns(space)) {
                                if (town != null && town.getTeam() != Team.None) {
                                    for (Player p : players.getTeamPlayers(town.getTeam())) {
                                        if (town instanceof Settlement)
                                            p.addResource(board.getTile(space), rules.getSettlementResources());
                                        if (town instanceof City)
                                            p.addResource(board.getTile(space), rules.getCityResources());
                                    }
                                }
                            }
                        }
                    } else {
                        teamTurns.setRobberActive(true);
                    }
                }
                break;
            case Player_Move_Robber:
                player.moveRobber();
                board.moveRobber(coordinate);
                break;
            case Build_Settlement:
                player.buildSettlement(board.buildSettlement(coordinate, team));
                break;
            case Build_City:
                player.buildCity(board.buildCity(coordinate, team));
                break;
            case Build_Road:
                player.buildRoad(board.buildRoad(coordinate, team));
                break;
            case Buy_Development:
                player.buyDevelopmentCard(cards.next());
                break;
            case Offer_Trade:
                player.offerTrade((TemporaryTrade) event.getPayload());
                break;
            case Make_Trade:
                player.makeTrade((Trade) event.getPayload());
                if (event.getPayload() instanceof TemporaryTrade) {
                    Player seller = players.getPlayer(((TemporaryTrade) event.getPayload()).seller);
                    seller.fulfillTrade((TemporaryTrade) event.getPayload());
                    seller.offerTrade(null);
                }
                break;
        }
    }

    @Override
    public void undo(GameEvent event) throws EventConsumerException {
        if (event == null || !event.equals(eventStack.peek()))
            throw new EventConsumerException("Cannot undo game event", event);
        eventStack.pop();
        Username origin = event.getOrigin();
        Player player = players.getPlayer(origin);
        Team team = player.getTeam();
        Coordinate coordinate = null;
        if (event.getPayload() instanceof Coordinate)
            coordinate = (Coordinate) event.getPayload();
        switch(event.getType()) {
            case Turn_Advance:
                if (!teamTurns.starting()) {
                    List<Coordinate> spaces = board.getActiveTiles(diceRolls.prev());
                    if (spaces != null && !spaces.isEmpty()) {
                        for (Coordinate space : spaces) {
                            for (Town town : board.getAdjacentTowns(space)) {
                                if (town != null && town.getTeam() != Team.None) {
                                    for (Player p : players.getTeamPlayers(town.getTeam())) {
                                        if (town instanceof Settlement)
                                            p.removeResource(board.getTile(space), rules.getSettlementResources());
                                        if (town instanceof City)
                                            p.removeResource(board.getTile(space), rules.getCityResources());
                                    }
                                }
                            }
                        }
                    } else {
                        teamTurns.setRobberActive(false);
                    }
                }
                player.undoAdvanceTurn();
                teamTurns.prev();
                break;
            case Player_Move_Robber:
                board.undoRobber();
                player.undoMoveRobber();
                cards.prev();
                break;
            case Build_Settlement:
                board.destroyTown(coordinate);
                player.undoBuildSettlement();
                break;
            case Build_City:
                board.buildSettlement(coordinate, team);
                player.undoBuildCity();
                break;
            case Build_Road:
                board.destroyRoad(coordinate);
                player.undoBuildRoad();
                break;
            case Buy_Development:
                player.undoBuyDevelopmentCard(cards.prev());
                break;
            case Offer_Trade:
                player.undoOfferTrade();
                break;
            case Make_Trade:
                Trade trade = (Trade) event.getPayload();
                player.undoTrade(trade);
                if (trade instanceof TemporaryTrade) {
                    Player seller = players.getPlayer(((TemporaryTrade) trade).seller);
                    seller.undoFulfillTrade((TemporaryTrade) trade);
                    seller.undoOfferTrade();
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

    public List<GameEvent> getEventList() {
        return new ArrayList<>(eventStack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatanGame)) return false;

        CatanGame catanGame = (CatanGame) o;

        if (!eventStack.equals(catanGame.eventStack)) return false;
        if (!rules.equals(catanGame.rules)) return false;
        if (!players.equals(catanGame.players)) return false;
        if (!board.equals(catanGame.board)) return false;
        if (!diceRolls.equals(catanGame.diceRolls)) return false;
        if (!cards.equals(catanGame.cards)) return false;
        if (!teamTurns.equals(catanGame.teamTurns)) return false;
        return bankTrades.equals(catanGame.bankTrades);

    }

    public void assertEquals(CatanGame o) {
        if (this == o) return;

        if (!eventStack.equals(o.eventStack))
            throw new ComparisonFailure("EventStack", eventStack.toString(), o.eventStack.toString());
        if (!rules.equals(o.rules))
            throw new ComparisonFailure("Rules", rules.toString(), o.rules.toString());
        if (!players.equals(o.players))
            throw new ComparisonFailure("Players", players.toString(), o.players.toString());
        if (!board.equals(o.board))
            throw new ComparisonFailure("Board", board.toString(), o.board.toString());
        if (!diceRolls.equals(o.diceRolls))
            throw new ComparisonFailure("DiceRolls", diceRolls.toString(), o.diceRolls.toString());
        if (!cards.equals(o.cards))
            throw new ComparisonFailure("Cards", cards.toString(), o.cards.toString());
        if (!teamTurns.equals(o.teamTurns))
            throw new ComparisonFailure("Turns", teamTurns.toString(), o.teamTurns.toString());
        if (!bankTrades.equals(o.bankTrades))
            throw new ComparisonFailure("BankTrades", bankTrades.toString(), o.bankTrades.toString());

    }

    @Override
    public String toString() {
        return "CatanGame{" +
                "rules=" + rules +
                ", board=" + board +
                ", players=" + players +
                ", diceRolls=" + diceRolls +
                ", cards=" + cards +
                ", teamTurns=" + teamTurns +
                ", bankTrades=" + bankTrades +
                ", eventStack=" + eventStack +
                '}';
    }

    public List<Trade> getTrades(Username user) {
        List<Trade> trades = new ArrayList<>();
        Player player = players.getPlayer(user);
        for (Username u : players.getAllUsers()) {
            Trade trade = players.getPlayer(u).getTrade();
            if (trade != null && player.canMakeTrade(trade))
                trades.add(trade);
        }
        for (TradingPostType t1 : board.getTrades(player.getTeam())) {
            for (Trade trade : t1.getTrades())
                if (player.canMakeTrade(trade))
                    trades.add(trade);
        }
        for (Trade trade : bankTrades)
            if (player.canMakeTrade(trade))
                trades.add(trade);
        Collections.sort(trades);
        return trades;
    }

    public boolean isPlayerActive(Username user) {
        return players.getPlayer(user).getTeam() == teamTurns.getCurrentTeam();
    }
}
