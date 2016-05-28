package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.board.BoardEvent;
import com.gregswebserver.catan.common.game.board.BoardEventType;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.CoordTransforms;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.trade.PermanentTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gameplay.trade.TradingPostType;
import com.gregswebserver.catan.common.game.gamestate.*;
import com.gregswebserver.catan.common.game.listeners.GameListener;
import com.gregswebserver.catan.common.game.players.*;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreEventType;
import com.gregswebserver.catan.common.game.scoring.ScoreState;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.teams.TeamEvent;
import com.gregswebserver.catan.common.game.teams.TeamEventType;
import com.gregswebserver.catan.common.game.teams.TeamPool;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import org.junit.ComparisonFailure;

import java.util.*;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements ReversibleEventConsumer<GameEvent> {

    //Permanent data
    private final GameRules rules;
    private final Set<Trade> bankTrades;

    //Game state storage.
    private final GameBoard board;
    private final PlayerPool players;
    private final TeamPool teams;
    private final SharedState state;

    //Score event listening
    private final ScoreState scoring;
    private final List<GameListener> listeners;

    //Historical event data
    private final Stack<GameHistory> history;

    public CatanGame(GameSettings settings) {
        rules = settings.rules;
        bankTrades = new HashSet<>();
        for (GameResource target : GameResource.values())
            for (GameResource source : GameResource.values())
                if (target != source)
                    bankTrades.add(new PermanentTrade(source, target, 4));
        board = settings.boardGenerator.generate(settings.boardLayout, settings.seed);
        players = new PlayerPool(settings);
        teams = new TeamPool(settings);
        state = new SharedState(settings);
        history = new Stack<>();
        history.push(new GameHistory(new GameEvent(null, GameEventType.Start, null),
                                     new LogicEvent(this, LogicEventType.NOP, null)));
        scoring = new ScoreState(board, players, teams);
        listeners = new ArrayList<>();
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameListener listener) {
        listeners.remove(listener);
    }

    public GameBoard getBoard() {
        return board;
    }

    public PlayerPool getPlayers() {
        return players;
    }

    public List<GameEvent> getEventList() {
        ArrayList<GameEvent> out = new ArrayList<>(history.size());
        for (GameHistory h : history)
            out.add(h.getGameEvent());
        return out;
    }

    public List<Trade> getTrades(Username user) {
        List<Trade> trades = new ArrayList<>();
        Player player = players.getPlayer(user);
        for (Username u : players) {
            Trade trade = players.getPlayer(u).getTrade();
            if (trade != null && player.canMakeTrade(trade))
                trades.add(trade);
        }
        for (TradingPostType t1 : board.getTrades(player.getTeamColor())) {
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
        Player player = players.getPlayer(user);
        return player != null && player.getTeamColor() == state.getActiveTeam();
    }

    private LogicEvent createTriggerEvent(GameTriggerEvent event) {
        return new LogicEvent(this, LogicEventType.Trigger, event);
    }

    private LogicEvent createPlayerEvent(Username origin, PlayerEventType type, Object payload) {
        return createTriggerEvent(new PlayerEvent(origin, type, payload));
    }

    private LogicEvent createBoardEvent(TeamColor origin, BoardEventType type, Object payload) {
        return createTriggerEvent(new BoardEvent(origin, type, payload));
    }

    private LogicEvent createTeamEvent(TeamColor origin, TeamEventType type, Object payload) {
        return createTriggerEvent(new TeamEvent(origin, type, payload));
    }

    private LogicEvent createStateEvent(Object origin, GameStateEventType type, Object payload) {
        return createTriggerEvent(new GameStateEvent(origin, type, payload));
    }

    private LogicEvent createScoreEvent(Username origin, ScoreEventType type, Object payload) {
        return createTriggerEvent(new ScoreEvent(origin, type, payload));
    }

    private LogicEvent getLogicEvent(GameEvent event) {
        Username origin = event.getOrigin();
        TeamColor teamColor = players.getPlayer(origin).getTeamColor();
        switch (event.getType()) {
            case Start:
                break;
            case Turn_Advance:
                //Advancing the turn requires all of these events to fire.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    //Need to pass the turn to the next team
                    createStateEvent(this, GameStateEventType.Advance_Turn, null),
                    //Roll the dice to get the next income round.
                    createStateEvent(this, GameStateEventType.Roll_Dice, null),
                    //Choose whether this is a starter turn or a real turn.
                    new LogicEvent(this, LogicEventType.OR, Arrays.asList(
                        //If its a starter turn, signal we are ready to advance.
                        createTeamEvent(teamColor, TeamEventType.Finish_Setup_Turn, null),
                        //Otherwise it might be a regular turn.
                        new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                            //Advance the turn as regular
                            createTeamEvent(teamColor, TeamEventType.Finish_Turn, null),
                            //ive everyone their income
                            getIncomeEvents(state.getDiceRoll())
                        ))
                    )),
                    //Mature all of the cards that a user bought this turn.
                    createTriggerEvent(players.getPlayer(origin).getMatureEvent())
                ));
            case Player_Move_Robber:
                //Moving the robber requires checking if the move can be made, and the player is allowed to do it.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    //Choose whether to use up the free robber from the start of turn or use a development card.
                    new LogicEvent(this, LogicEventType.OR, Arrays.asList(
                        createTeamEvent(teamColor, TeamEventType.Use_Robber, event.getPayload()),
                        createPlayerEvent(origin, PlayerEventType.Use_DevelopmentCard, DevelopmentCard.Knight),
                        createScoreEvent(origin, ScoreEventType.Play_Development, DevelopmentCard.Knight)
                    )),
                    //Check the board to see if the robber is in a valid location.
                    createBoardEvent(teamColor, BoardEventType.Place_Robber, event.getPayload())
                ));
            case Build_Settlement:
                //A settlement build can happen as an outpost or as a normal settlement.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    new LogicEvent(this, LogicEventType.OR, Arrays.asList(
                        //If we make a regular settlement, the purchase and placement must be valid
                        new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                            createPlayerEvent(origin, PlayerEventType.Make_Purchase, Purchase.Settlement),
                            createBoardEvent(teamColor, BoardEventType.Place_Settlement, event.getPayload())
                        )),
                        //If we make a first outpost, we need to check for outpost placement
                        new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                            createTeamEvent(teamColor, TeamEventType.Build_First_Outpost, event.getPayload()),
                            createBoardEvent(teamColor, BoardEventType.Place_Outpost, event.getPayload())
                        )),
                        //If we make a second outpost, we need to allocate the correct resources.
                        new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                            createTeamEvent(teamColor, TeamEventType.Build_Second_Outpost, event.getPayload()),
                            createBoardEvent(teamColor, BoardEventType.Place_Outpost, event.getPayload()),
                            createPlayerEvent(origin, PlayerEventType.Gain_Resources, getTownIncome((Coordinate) event.getPayload()))
                        ))
                    )),
                    createScoreEvent(origin, ScoreEventType.Build_Settlement, event.getPayload())
                ));
            case Build_City:
                //All cities are purchased manually.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    createPlayerEvent(origin, PlayerEventType.Make_Purchase, Purchase.City),
                    createBoardEvent(teamColor, BoardEventType.Place_City, event.getPayload()),
                    createScoreEvent(origin, ScoreEventType.Build_City, event.getPayload())
                ));
            case Build_Road:
                //A road can be built either using the free outpost or as a purchase.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    new LogicEvent(this, LogicEventType.OR, Arrays.asList(
                        createTeamEvent(teamColor, TeamEventType.Build_Free_Road, event.getPayload()),
                        createPlayerEvent(origin, PlayerEventType.Make_Purchase, Purchase.Road)
                    )),
                    createBoardEvent(teamColor, BoardEventType.Place_Road, event.getPayload()),
                    createScoreEvent(origin, ScoreEventType.Build_Road, event.getPayload())
                ));
            case Buy_Development:
                //In order to gain a development card, one must exist and the user must have enough resources.
                return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                    createStateEvent(this, GameStateEventType.Draw_DevelopmentCard, null),
                    createPlayerEvent(origin, PlayerEventType.Make_Purchase, Purchase.DevelopmentCard),
                    createPlayerEvent(origin, PlayerEventType.Gain_DevelopmentCard, state.getDevelopmentCard()),
                    createScoreEvent(origin, ScoreEventType.Buy_Development, state.getDevelopmentCard())
                ));
            case Offer_Trade:
                return createPlayerEvent(origin, PlayerEventType.Offer_Trade, event.getPayload());
            case Make_Trade:
                if (event.getPayload() instanceof TemporaryTrade) {
                    TemporaryTrade t = (TemporaryTrade) event.getPayload();
                    return new LogicEvent(this, LogicEventType.AND, Arrays.asList(
                        createPlayerEvent(origin, PlayerEventType.Make_Trade, t),
                        createPlayerEvent(t.seller, PlayerEventType.Fill_Trade, t)
                    ));
                } else {
                    return createPlayerEvent(origin, PlayerEventType.Make_Trade, event.getPayload());
                }
        }
        return new LogicEvent(this, LogicEventType.NOP, null);
    }

    private LogicEvent getIncomeEvents(DiceRoll roll) {
        List<Coordinate> spaces = board.getActiveTiles(roll);
        if (spaces == null || spaces.isEmpty())
            return new LogicEvent(this, LogicEventType.NOP, null);
        Map<TeamColor, EnumCounter<GameResource>> income = new EnumMap<>(TeamColor.class);
        for (Coordinate space : spaces) {
            GameResource resource = ((ResourceTile) board.getTile(space)).getResource();
            if (resource != null) {
                for (Town town : board.getAdjacentTowns(space)) {
                    TeamColor teamColor = (town != null ? town.getTeam() : TeamColor.None);
                    if (teamColor != TeamColor.None) {
                        EnumCounter<GameResource> teamIncome = income.get(teamColor);
                        if (teamIncome == null)
                            income.put(teamColor, teamIncome = new EnumCounter<>(GameResource.class));
                        if (town instanceof Settlement)
                            teamIncome.increment(resource, rules.getSettlementResources());
                        if (town instanceof City)
                            teamIncome.increment(resource, rules.getCityResources());
                    }
                }
            }
        }
        ArrayList<LogicEvent> events = new ArrayList<>();
        for (Map.Entry<TeamColor, EnumCounter<GameResource>> entry : income.entrySet())
            for (Username name : teams.getTeam(entry.getKey()).getPlayers())
                events.add(createPlayerEvent(name, PlayerEventType.Gain_Resources, entry.getValue()));
        return new LogicEvent(this, LogicEventType.AND, events);
    }

    private EnumCounter<GameResource> getTownIncome(Coordinate vertex) {
        EnumCounter<GameResource> counter = new EnumCounter<>(GameResource.class);
        for (Coordinate space : CoordTransforms.getAdjacentSpacesFromVertex(vertex).values()) {
            if (board.getTile(space) instanceof ResourceTile) {
                ResourceTile tile = (ResourceTile) board.getTile(space);
                GameResource resource = tile.getResource();
                if (resource != null)
                    counter.increment(resource, 1);
            }
        }
        return counter;
    }

    private void test(GameTriggerEvent event) throws EventConsumerException {
        if (event instanceof PlayerEvent)
            players.test((PlayerEvent) event);
        else if (event instanceof BoardEvent)
            board.test((BoardEvent) event);
        else if (event instanceof TeamEvent)
            teams.test((TeamEvent) event);
        else if (event instanceof GameStateEvent)
            state.test((GameStateEvent) event);
        else if (event instanceof ScoreEvent)
            scoring.test((ScoreEvent) event);
        else
            throw new EventConsumerException("Unknown event type!");
    }

    @SuppressWarnings("unchecked")
    private void test(LogicEvent event) throws EventConsumerException {
        Object payload = event.getPayload();
        switch (event.getType()) {
            case AND:
                for (LogicEvent child : (List<LogicEvent>) payload)
                    test(child);
                break;
            case OR:
                EventConsumerException fail = new EventConsumerException("No successful case", event);
                for (LogicEvent child : (List<LogicEvent>) payload)
                    try {
                        test(child);
                        return;
                    } catch (EventConsumerException e) {
                        fail.addSuppressed(e);
                    }
                throw fail;
            case NOT:
                boolean success = true;
                try {
                    test((LogicEvent) payload);
                } catch (EventConsumerException ignored) {
                    success = false;
                }
                if (success)
                    throw new EventConsumerException("Event successful", (LogicEvent) payload);
            case NOP:
                break;
            case Trigger:
                test((GameTriggerEvent) payload);
                break;
        }
    }

    @Override
    public void test(GameEvent event) throws EventConsumerException {
        test(getLogicEvent(event));
    }

    private void execute(GameTriggerEvent event) throws EventConsumerException {
        if (event instanceof PlayerEvent)
            players.execute((PlayerEvent) event);
        else if (event instanceof BoardEvent)
            board.execute((BoardEvent) event);
        else if (event instanceof TeamEvent)
            teams.execute((TeamEvent) event);
        else if (event instanceof GameStateEvent)
            state.execute((GameStateEvent) event);
        else if (event instanceof ScoreEvent)
            scoring.execute((ScoreEvent) event);
        else
            throw new EventConsumerException("Unknown event type!");
        for (GameListener listener : listeners) {
            boolean valid = true;
            try {
                listener.test(event);
            } catch (Exception e) {
                valid = false;
                if (e instanceof EventConsumerException)
                    listener.reportTestError((EventConsumerException) e);
            }
            if (valid) {
                try {
                    listener.test(event);
                } catch (Exception e) {
                    if (e instanceof EventConsumerException)
                        listener.reportExecuteError((EventConsumerException) e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private LogicEvent execute(LogicEvent event) throws EventConsumerException {
        test(event);
        Object payload = event.getPayload();
        switch (event.getType()) {
            case AND:
                ArrayList<LogicEvent> newChildren = new ArrayList<>();
                for (LogicEvent child : (List<LogicEvent>) payload)
                    newChildren.add(execute(child));
                return new LogicEvent(this, LogicEventType.AND, newChildren);
            case OR:
                EventConsumerException inconsistent = new EventConsumerException("Inconsistent", event);
                for (LogicEvent child : (List<LogicEvent>) payload) {
                    try {
                        test(child);
                        return execute(child);
                    } catch (EventConsumerException e) {
                        inconsistent.addSuppressed(e);
                    }
                }
                throw inconsistent;
            case NOT:
                try {
                    test((LogicEvent) payload);
                } catch (EventConsumerException e) {
                    return new LogicEvent(this, LogicEventType.NOP, null);
                }
                throw new EventConsumerException("Inconsistent", event);
            case NOP:
                break;
            case Trigger:
                execute((GameTriggerEvent) payload);
                break;
        }
        return event;
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        test(event);
        try {
            LogicEvent logic = getLogicEvent(event);
            LogicEvent actions = execute(logic);
            history.push(new GameHistory(event, actions));
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    private void undo(GameTriggerEvent event) throws EventConsumerException {
        if (event instanceof PlayerEvent)
            players.undo();
        else if (event instanceof BoardEvent)
            board.undo();
        else if (event instanceof TeamEvent)
            teams.undo();
        else if (event instanceof GameStateEvent)
            state.undo();
        else if (event instanceof ScoreEvent)
            scoring.undo();
        else
            throw new EventConsumerException("Unknown event type!");
    }

    @SuppressWarnings("unchecked")
    private void undo(LogicEvent event) throws EventConsumerException {
        try {
            Object payload = event.getPayload();
            switch (event.getType()) {
                case AND:
                case OR:
                    for (LogicEvent child : (List<LogicEvent>) payload)
                        undo(child);
                    break;
                case NOT:
                    undo((LogicEvent) payload);
                    break;
                case NOP:
                    break;
                case Trigger:
                    undo((GameTriggerEvent) payload);
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        undo(history.pop().getLogicEvent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatanGame catanGame = (CatanGame) o;

        if (!rules.equals(catanGame.rules)) return false;
        if (!bankTrades.equals(catanGame.bankTrades)) return false;
        if (!board.equals(catanGame.board)) return false;
        if (!players.equals(catanGame.players)) return false;
        if (!teams.equals(catanGame.teams)) return false;
        if (!state.equals(catanGame.state)) return false;
        return history.equals(catanGame.history);

    }

    public void assertEquals(CatanGame o) {
        if (this == o) return;

        if (!rules.equals(o.rules))
            throw new ComparisonFailure("GameRules", rules.toString(), o.rules.toString());
        if (!bankTrades.equals(o.bankTrades))
            throw new ComparisonFailure("BankTrades", bankTrades.toString(), o.bankTrades.toString());
        if (!board.equals(o.board))
            throw new ComparisonFailure("Board", board.toString(), o.board.toString());
        if (!players.equals(o.players))
            throw new ComparisonFailure("Players", players.toString(), o.players.toString());
        if (!teams.equals(o.teams))
            throw new ComparisonFailure("DiceRolls", teams.toString(), o.teams.toString());
        if (!state.equals(o.state))
            throw new ComparisonFailure("Cards", state.toString(), o.state.toString());
        if (!history.equals(o.history))
            throw new ComparisonFailure("EventStack", history.toString(), o.history.toString());

    }

    @Override
    public String toString() {
        return "CatanGame";
    }
}
