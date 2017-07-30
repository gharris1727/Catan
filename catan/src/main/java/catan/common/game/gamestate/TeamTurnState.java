package catan.common.game.gamestate;

import catan.common.game.teams.TeamColor;
import catan.common.util.ReversibleIterator;

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
        TeamColor next = get();
        turnNumber++;
        return next;
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
        int turnIndex = turnNumber % turnOrder.size();
        return turnOrder.get(isSecondRound() ? turnOrder.size() - turnIndex - 1 : turnIndex);
    }

    private boolean isGameStarting() {
        return turnNumber < (turnOrder.size() << 1);
    }

    private boolean isFirstRound() { return turnNumber < turnOrder.size(); }

    private boolean isSecondRound() { return isGameStarting() && !isFirstRound(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        TeamTurnState other = (TeamTurnState) o;

        if (turnNumber != other.turnNumber) return false;
        return turnOrder.equals(other.turnOrder);

    }

    @Override
    public int hashCode() {
        int result = turnOrder.hashCode();
        result = 31 * result + turnNumber;
        return result;
    }

    @Override
    public String toString() {
        return "TeamTurnState{" +
                "turnOrder=" + turnOrder +
                ", turnNumber=" + turnNumber +
                '}';
    }
}
