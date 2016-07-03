package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
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
import com.gregswebserver.catan.common.game.event.*;
import com.gregswebserver.catan.common.game.gameplay.allocator.TeamAllocation;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gameplay.trade.TradingPostType;
import com.gregswebserver.catan.common.game.gamestate.*;
import com.gregswebserver.catan.common.game.listeners.GameListener;
import com.gregswebserver.catan.common.game.players.*;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreEventType;
import com.gregswebserver.catan.common.game.scoring.ScoreState;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreException;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.team.NullTeamScore;
import com.gregswebserver.catan.common.game.scoring.reporting.team.TeamScoreReport;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.teams.TeamEvent;
import com.gregswebserver.catan.common.game.teams.TeamEventType;
import com.gregswebserver.catan.common.game.teams.TeamPool;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.test.common.game.AssertEqualsTestable;
import com.gregswebserver.catan.test.common.game.EqualityException;

import java.util.*;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of Catan that contains the game board, game state, and player information.
 */
public class CatanGame implements ReversibleEventConsumer<GameEvent>, AssertEqualsTestable<CatanGame> {

    //Permanent data
    private final GameRules rules;
    private final TeamAllocation teamAllocation;

    //Game state storage.
    private final GameBoard board;
    private final PlayerPool players;
    private final TeamPool teams;
    private final RandomizerState state;

    //Score event listening
    private final ScoreState scoring;
    private final List<GameListener> listeners;

    //Historical event data
    private final Stack<GameHistory> history;

    public CatanGame(GameSettings settings) {
        rules = settings.rules;
        teamAllocation = settings.playerTeams.allocate(settings.seed);
        board = settings.boardGenerator.generate(settings.boardLayout, settings.seed);
        players = new PlayerPool(teamAllocation);
        teams = new TeamPool(teamAllocation);
        state = new RandomizerState(settings);
        history = new Stack<>();
        history.push(new GameHistory(new GameEvent(null, GameEventType.Start, null), Collections.emptyList()));
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

    public Player getPlayer(Username username) {
        return players.getPlayer(username);
    }

    public List<GameHistory> getHistory() {
        return history;
    }

    public boolean mustDiscard(Username username) {
        return state.getDiceRoll() == DiceRoll.Seven
            && players.getPlayer(username).getDiscardCount() > 0
            && !players.hasDiscarded(username);
    }

    public List<Trade> getTrades(Username user) {
        List<Trade> trades = new ArrayList<>();
        Player player = players.getPlayer(user);
        for (Username u : players) {
            for (Trade trade : players.getPlayer(u).getTrades())
                if (player.canMakeTrade(trade))
                    trades.add(trade);
        }
        for (TradingPostType type : board.getTrades(player.getTeamColor())) {
            for (Trade trade : type.getTrades())
                if (player.canMakeTrade(trade))
                    trades.add(trade);
        }
        Collections.sort(trades);
        return trades;
    }

    public TeamScoreReport getScore() {
        try {
            return scoring.score(rules);
        } catch (ScoreException e) {
            return NullTeamScore.INSTANCE;
        }
    }

    public TeamColor getWinner() {
        TeamColor winner = TeamColor.None;
        int points = 0;
        TeamScoreReport teamScoreReport = getScore();
        for (TeamColor color : teamScoreReport) {
            ScoreReport report = teamScoreReport.getScoreReport(color);
            if (points < report.getPoints()) {
                points = report.getPoints();
                winner = color;
            }
        }
        if (points < rules.getMinimumPoints())
            return TeamColor.None;
        return winner;
    }

    private LogicEvent trigger(GameTriggerEvent event) {
        return new LogicEvent(this, LogicEventType.Trigger, event);
    }

    private LogicEvent player(Username origin, PlayerEventType type, Object payload) {
        return trigger(new PlayerEvent(origin, type, payload));
    }

    private LogicEvent board(TeamColor origin, BoardEventType type, Object payload) {
        return trigger(new BoardEvent(origin, type, payload));
    }

    private LogicEvent team(TeamColor origin, TeamEventType type, Object payload) {
        return trigger(new TeamEvent(origin, type, payload));
    }

    private LogicEvent state(Username origin, GameStateEventType type, Object payload) {
        return trigger(new GameStateEvent(origin, type, payload));
    }

    private LogicEvent score(Username origin, ScoreEventType type, Object payload) {
        return trigger(new ScoreEvent(origin, type, payload));
    }

    private LogicEvent compound(LogicEventType type, LogicEvent... list) {
        return new LogicEvent(this, type, Arrays.asList(list));
    }

    private LogicEvent getLogicTree(GameEvent event) {
        Username origin = event.getOrigin();
        TeamColor teamColor = players.getPlayer(origin).getTeamColor();
        switch (event.getType()) {
            case Start:
                return new LogicEvent(this, LogicEventType.NOT, new LogicEvent(this, LogicEventType.NOP, null));
            case Turn_Advance:
                //Advancing the turn requires all of these events to fire.
                EnumCounter<DevelopmentCard> boughtCards = players.getPlayer(origin).getBoughtCards();
                boughtCards = new EnumAccumulator<>(DevelopmentCard.class, boughtCards);
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    //Need to pass the turn to the next team
                    state(origin, GameStateEventType.Advance_Turn, teamColor),
                    //Choose whether this is a starter turn or a real turn.
                    compound(LogicEventType.OR,
                        //If its a starter turn, signal we are ready to advance.
                        team(teamColor, TeamEventType.Finish_Setup_Turn, teamColor),
                        //Otherwise it might be a regular turn.
                        compound(LogicEventType.AND,
                            //Advance the turn as regular
                            team(teamColor, TeamEventType.Finish_Turn, teamColor),
                            //Give everyone their income
                            diceRollEvents(origin, state.getDiceRoll()),
                            //Roll the dice to get the next income round.
                            state(origin, GameStateEventType.Roll_Dice, state.getDiceRoll())
                        )
                    ),
                    //Mature all of the cards that a user bought this turn.
                    player(origin, PlayerEventType.Mature_DevelopmentCards, boughtCards)
                );
            case Player_Move_Robber:
                //Moving the robber requires checking if the move can be made, and the player is allowed to do it.
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    //Choose whether to use up the free robber from the start of turn or use a development card.
                    compound(LogicEventType.OR,
                        team(teamColor, TeamEventType.Use_Robber, event.getPayload()),
                        compound(LogicEventType.AND,
                            player(origin, PlayerEventType.Use_DevelopmentCard, DevelopmentCard.Knight),
                            score(origin, ScoreEventType.Play_Development, DevelopmentCard.Knight)
                        )
                    ),
                    //Check the board to see if the robber is in a valid location.
                    board(teamColor, BoardEventType.Place_Robber, event.getPayload())
                );
            case Build_Settlement:
                //A settlement build can happen as an outpost or as a normal settlement.
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    compound(LogicEventType.OR,
                        //If we make a regular settlement, the purchase and placement must be valid
                        compound(LogicEventType.AND,
                            player(origin, PlayerEventType.Lose_Resources, Purchase.Settlement),
                            board(teamColor, BoardEventType.Place_Settlement, event.getPayload())
                        ),
                        //If we make a first outpost, we need to check for outpost placement
                        compound(LogicEventType.AND,
                            team(teamColor, TeamEventType.Build_First_Outpost, event.getPayload()),
                            board(teamColor, BoardEventType.Place_Outpost, event.getPayload())
                        ),
                        //If we make a second outpost, we need to allocate the correct resources.
                        compound(LogicEventType.AND,
                            team(teamColor, TeamEventType.Build_Second_Outpost, event.getPayload()),
                            board(teamColor, BoardEventType.Place_Outpost, event.getPayload()),
                            player(origin, PlayerEventType.Gain_Resources, getTownIncome((Coordinate) event.getPayload()))
                        )
                    ),
                    score(origin, ScoreEventType.Build_Settlement, event.getPayload())
                );
            case Build_City:
                //All cities are purchased manually.
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    //Need to be able to purchase the city
                    player(origin, PlayerEventType.Lose_Resources, Purchase.City),
                    //Place the city on the board
                    board(teamColor, BoardEventType.Place_City, event.getPayload()),
                    //Need to notify the scorers.
                    score(origin, ScoreEventType.Build_City, event.getPayload())
                );
            case Build_Road:
                //A road can be built either using the free outpost or as a purchase.
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    //Decide between the free or purchased road
                   compound(LogicEventType.OR,
                        team(teamColor, TeamEventType.Build_Free_Road, event.getPayload()),
                        player(origin, PlayerEventType.Lose_Resources, Purchase.Road)
                    ),
                    //Place the road on the board.
                    board(teamColor, BoardEventType.Place_Road, event.getPayload()),
                    //Notify the scorers.
                    score(origin, ScoreEventType.Build_Road, event.getPayload())
                );
            case Buy_Development:
                //In order to gain a development card, one must exist and the user must have enough resources.
                return compound(LogicEventType.AND,
                    //Needs to be their turn.
                    state(origin, GameStateEventType.Active_Turn, teamColor),
                    //Need to remove a card from the deck
                    state(origin, GameStateEventType.Draw_DevelopmentCard, state.getDevelopmentCard()),
                    //Need to purchase a card
                    player(origin, PlayerEventType.Lose_Resources, Purchase.DevelopmentCard),
                    //Need to add the development card to their inventory
                    player(origin, PlayerEventType.Gain_DevelopmentCard, state.getDevelopmentCard()),
                    //Need to notify the scorers.
                    score(origin, ScoreEventType.Buy_Development, state.getDevelopmentCard())
                );
            case Offer_Trade:
                return player(origin, PlayerEventType.Offer_Trade, event.getPayload());
            case Cancel_Trade:
                return player(origin, PlayerEventType.Cancel_Trade, event.getPayload());
            case Make_Trade: {
                Trade t = (Trade) event.getPayload();
                TeamColor otherTeam = t.getSeller() == null ? null : players.getPlayer(t.getSeller()).getTeamColor();
                return compound(LogicEventType.AND,
                    compound(LogicEventType.OR,
                        //Needs to be our turn
                        state(origin, GameStateEventType.Active_Turn, teamColor),
                        //Needs to be the other players turn
                        state(origin, GameStateEventType.Active_Turn, otherTeam)
                    ),
                    player(origin, PlayerEventType.Gain_Resources, t.getOffer()),
                    player(origin, PlayerEventType.Lose_Resources, t.getRequest()),
                    player(t.getSeller(), PlayerEventType.Gain_Resources, t.getRequest()),
                    player(t.getSeller(), PlayerEventType.Lose_Resources, t.getOffer()),
                    player(t.getSeller(), PlayerEventType.Use_Trade, t)
                );
            }
            case Discard_Resources:
                return player(origin, PlayerEventType.Discard_Resources, event.getPayload());
            case Steal_Resources:
                return compound(LogicEventType.AND,
                    team(teamColor, TeamEventType.Steal_Resources, null),
                    getStolenResources(origin, (Coordinate) event.getPayload())
                );
            case Play_RoadBuilding:
                return compound(LogicEventType.AND,
                    team(teamColor, TeamEventType.Activate_RoadBuilding, null),
                    player(origin, PlayerEventType.Use_DevelopmentCard, DevelopmentCard.RoadBuilding)
                );
            case Play_YearOfPlenty:
                return compound(LogicEventType.AND,
                    player(origin, PlayerEventType.Gain_Resources, event.getPayload()),
                    player(origin, PlayerEventType.Use_DevelopmentCard, DevelopmentCard.YearOfPlenty)
                );
            case Play_Monopoly: {
                GameResource resource = (GameResource) event.getPayload();
                int total = 0;
                List<LogicEvent> playerEvents = new ArrayList<>();
                for (Username username : players) {
                    Player p = players.getPlayer(username);
                    int count = p.getInventory().get(resource);
                    if (count > 0) {
                        total += count;
                        EnumCounter<GameResource> loss = new EnumAccumulator<>(GameResource.class, resource, count);
                        playerEvents.add(player(username, PlayerEventType.Lose_Resources, loss));
                    }
                }
                EnumCounter<GameResource> gain = new EnumAccumulator<>(GameResource.class, resource, total);
                playerEvents.add(player(origin, PlayerEventType.Gain_Resources, gain));
                playerEvents.add(player(origin, PlayerEventType.Use_DevelopmentCard, DevelopmentCard.Monopoly));
                return compound(LogicEventType.AND, playerEvents.toArray(new LogicEvent[playerEvents.size()]));
            }
        }
        return new LogicEvent(this, LogicEventType.NOP, null);
    }

    private LogicEvent diceRollEvents(Username origin, DiceRoll diceRoll) {
        //Create a list to collect all of the events that occur based on the dice roll.
        ArrayList<LogicEvent> events = new ArrayList<>();
        //If this roll is a seven, activate the free robber.
        if (diceRoll == DiceRoll.Seven)
            events.add(team(state.getNextTeam(), TeamEventType.Activate_Robber, null));
        //If the last roll was a seven, make sure that everyone is done discarding.
        if (state.getPreviousDiceRoll() == DiceRoll.Seven)
            events.add(player(origin, PlayerEventType.Finish_Discarding, null));
        //Temporary storage for tallying the income.
        Map<TeamColor, EnumAccumulator<GameResource>> income = new EnumMap<>(TeamColor.class);
        //Traverse the board and collect all of the income.
        for (Coordinate space : board.getActiveTiles(diceRoll)) {
            GameResource resource = ((ResourceTile) board.getTile(space)).getResource();
            if (resource != null) {
                for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromSpace(space).values()) {
                    Town town = board.getTown(vertex);
                    TeamColor teamColor = (town != null ? town.getTeam() : TeamColor.None);
                    if (teamColor != TeamColor.None) {
                        EnumAccumulator<GameResource> teamIncome = income.get(teamColor);
                        if (teamIncome == null)
                            income.put(teamColor, teamIncome = new EnumAccumulator<>(GameResource.class));
                        if (town instanceof Settlement)
                            teamIncome.increment(resource, rules.getSettlementResources());
                        if (town instanceof City)
                            teamIncome.increment(resource, rules.getCityResources());
                    }
                }
            }
        }
        //Transform the team income to player income events.
        for (Map.Entry<TeamColor, EnumAccumulator<GameResource>> entry : income.entrySet())
            for (Username name : teams.getTeam(entry.getKey()).getPlayers())
                events.add(player(name, PlayerEventType.Gain_Resources, entry.getValue()));
        //Return all of the events ANDed together so they must all occur.
        return new LogicEvent(this, LogicEventType.AND, events);
    }

    private EnumAccumulator<GameResource> getTownIncome(Coordinate vertex) {
        EnumAccumulator<GameResource> accumulator = new EnumAccumulator<>(GameResource.class);
        for (Coordinate space : CoordTransforms.getAdjacentSpacesFromVertex(vertex).values()) {
            if (board.getTile(space) instanceof ResourceTile) {
                ResourceTile tile = (ResourceTile) board.getTile(space);
                GameResource resource = tile.getResource();
                if (resource != null)
                    accumulator.increment(resource, 1);
            }
        }
        return accumulator;
    }

    private LogicEvent getStolenResources(Username origin, Coordinate vertex) {
        TeamColor team = board.getTown(vertex).getTeam();
        if (team == TeamColor.None)
            return new LogicEvent(this, LogicEventType.NOP, null);
        int total = 0;
        for (Username username : teams.getTeam(team).getPlayers()) {
            Player p = players.getPlayer(username);
            EnumCounter<GameResource> inventory = p.getInventory();
            for (GameResource r : GameResource.values())
                total += inventory.get(r);
        }
        if (total == 0)
            return new LogicEvent(this, LogicEventType.NOP, null);
        int stolen = (total == 1) ? 0 : state.getTheftInt(total);
        total = 0;
        Username target = null;
        EnumAccumulator<GameResource> amount = new EnumAccumulator<>(GameResource.class);
        for (Username username : teams.getTeam(team).getPlayers()) {
            Player p = players.getPlayer(username);
            EnumCounter<GameResource> inventory = p.getInventory();
            for (GameResource r : GameResource.values()) {
                total += inventory.get(r);
                if (total >= stolen && target == null && inventory.contains(r, 1)) {
                    target = username;
                    amount.increment(r, 1);
                }
            }
        }
        return compound(LogicEventType.AND,
            player(target, PlayerEventType.Lose_Resources, amount),
            player(origin, PlayerEventType.Gain_Resources, amount),
            state(origin, GameStateEventType.Advance_Theft, null)
        );
    }

    private EventConsumerProblem test(GameTriggerEvent event) {
        if (event instanceof PlayerEvent)
            return players.test((PlayerEvent) event);
        else if (event instanceof BoardEvent)
            return board.test((BoardEvent) event);
        else if (event instanceof TeamEvent)
            return teams.test((TeamEvent) event);
        else if (event instanceof GameStateEvent)
            return state.test((GameStateEvent) event);
        else if (event instanceof ScoreEvent)
            return scoring.test((ScoreEvent) event);
        else
            return new EventConsumerProblem("Unknown event type!");
    }

    @SuppressWarnings("unchecked")
    private EventConsumerProblem test(LogicEvent event) {
        Object payload = event.getPayload();
        EventConsumerProblem problem = null;
        switch (event.getType()) {
            case AND:
                for (LogicEvent child : (List<LogicEvent>) payload)
                    problem = problem != null ? problem : test(child);
                break;
            case OR:
                problem = new EventConsumerProblem("No successful case");
                for (LogicEvent child : (List<LogicEvent>) payload) {
                    EventConsumerProblem childProblem = test(child);
                    if (problem != null && childProblem != null)
                        problem.addCause(childProblem);
                    else
                        problem = null;
                }
                break;
            case NOT:
                if (test((LogicEvent) payload) == null)
                    problem = new EventConsumerProblem(payload + " was successful");
                break;
            case NOP:
                break;
            case Trigger:
                problem = test((GameTriggerEvent) payload);
                break;
        }
        return problem;
    }

    @Override
    public EventConsumerProblem test(GameEvent event) {
        try {
            return test(getLogicTree(event));
        } catch (Exception e) {
            return new EventConsumerProblem(e.getMessage());
        }
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
            EventConsumerProblem problem = listener.test(event);
            if (problem != null) {
                listener.reportTestProblem(problem);
            } else {
                try {
                    listener.execute(event);
                } catch (Exception e) {
                    if (e instanceof EventConsumerException)
                        listener.reportExecuteError((EventConsumerException) e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void execute(LogicEvent event, List<GameTriggerEvent> actions) throws EventConsumerException {
        test(event);
        Object payload = event.getPayload();
        switch (event.getType()) {
            case AND:
                for (LogicEvent child : (List<LogicEvent>) payload)
                    execute(child, actions);
                break;
            case OR:
                for (LogicEvent child : (List<LogicEvent>) payload) {
                    if (test(child) == null) {
                        execute(child, actions);
                        return;
                    }
                }
                throw new EventConsumerException("Inconsistent OR", event);
            case NOT:
                break;
            case NOP:
                break;
            case Trigger:
                execute((GameTriggerEvent) payload);
                actions.add((GameTriggerEvent) payload);
                break;
        }
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(problem);
        try {
            LogicEvent logic = getLogicTree(event);
            List<GameTriggerEvent> actions = new ArrayList<>();
            execute(logic, actions);
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

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        for (GameTriggerEvent event : history.pop().getTriggeredEvents())
            undo(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatanGame catanGame = (CatanGame) o;

        if (!state.equals(catanGame.state)) return false;
        if (!rules.equals(catanGame.rules)) return false;
        if (!board.equals(catanGame.board)) return false;
        if (!players.equals(catanGame.players)) return false;
        if (!teams.equals(catanGame.teams)) return false;
        return history.equals(catanGame.history);

    }

    @Override
    public void assertEquals(CatanGame other) throws EqualityException {
        if (this == other) return;

        if (!history.equals(other.history))
            throw new EqualityException("GameHistory", history, other.history);
        state.assertEquals(other.state);
        rules.assertEquals(other.rules);
        board.assertEquals(other.board);
        players.assertEquals(other.players);
        teams.assertEquals(other.teams);
        scoring.assertEquals(other.scoring);
        if (!listeners.equals(other.listeners))
            throw new EqualityException("GameListeners", listeners, other.listeners);
    }

    @Override
    public String toString() {
        return "CatanGame";
    }

    public TeamAllocation getTeams() {
        return teamAllocation;
    }
}
