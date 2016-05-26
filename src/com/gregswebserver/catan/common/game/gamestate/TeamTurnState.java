package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;
import com.gregswebserver.catan.common.util.ReversibleIterator;

import java.util.*;

/**
 * Created by greg on 2/21/16.
 * Generates the turn order for a game randomly, but then repeats the cycle indefinitely.
 */
public class TeamTurnState implements ReversibleIterator<TeamColor> {

    private final List<TeamColor> turnOrder;
    private int turnNumber;

    public TeamTurnState(long seed, Set<TeamColor> teamColors) {
        turnOrder = new ArrayList<>(teamColors);
        Collections.shuffle(turnOrder, new Random(seed));
        turnNumber = 0;
    }

    @Override
    public boolean hasPrev() {
        return turnNumber > 0;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public TeamColor next() {
        turnNumber++;
        return get();
    }

    @Override
    public TeamColor prev() {
        if (hasPrev())
            turnNumber--;
        else
            throw new NoSuchElementException();
        return get();
    }

    @Override
    public TeamColor get() {
        return secondRound() ?
                turnOrder.get((1 + turnOrder.size()-turnNumber) % turnOrder.size()) :
                turnOrder.get(turnNumber % turnOrder.size());
    }

    private boolean starting() {
        return turnNumber < turnOrder.size() * 2;
    }

    private boolean firstRound() { return turnNumber < turnOrder.size(); }

    private boolean secondRound() { return starting() && !firstRound(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamTurnState that = (TeamTurnState) o;

        if (turnNumber != that.turnNumber) return false;
        return turnOrder.equals(that.turnOrder);

    }

    @Override
    public String toString() {
        return "TeamTurnState{" +
                "turnOrder=" + turnOrder +
                ", turnNumber=" + turnNumber +
                '}';
    }
}
