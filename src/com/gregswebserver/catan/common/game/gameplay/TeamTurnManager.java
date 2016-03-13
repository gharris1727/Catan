package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.util.ReversibleIterator;

import java.util.*;

/**
 * Created by greg on 2/21/16.
 * Generates the turn order for a game randomly, but then repeats the cycle indefinitely.
 */
public class TeamTurnManager implements ReversibleIterator<Team> {

    private final List<Team> turnOrder;
    private int turnNumber;
    private boolean robberActive;

    public TeamTurnManager(long seed, Set<Team> teams) {
        turnOrder = new ArrayList<>(teams);
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
    public Team next() {
        turnNumber++;
        return getCurrentTeam();
    }

    @Override
    public Team prev() {
        if (turnNumber > 0)
            turnNumber--;
        return getCurrentTeam();
    }

    public Team getCurrentTeam() {
        return (starting() && turnNumber >= turnOrder.size()) ?
                turnOrder.get((1 + turnOrder.size()-turnNumber) % turnOrder.size()) :
                turnOrder.get(turnNumber % turnOrder.size());
    }

    public boolean starting() {
        return turnNumber < turnOrder.size() * 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamTurnManager that = (TeamTurnManager) o;

        if (turnNumber != that.turnNumber) return false;
        return turnOrder.equals(that.turnOrder);

    }

    @Override
    public String toString() {
        return "TeamTurnManager{" +
                "turnOrder=" + turnOrder +
                ", turnNumber=" + turnNumber +
                '}';
    }

    public boolean isRobberActive() {
        return robberActive;
    }

    public void setRobberActive(boolean robberActive) {
        this.robberActive = robberActive;
    }
}
