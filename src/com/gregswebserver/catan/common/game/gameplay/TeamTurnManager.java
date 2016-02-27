package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.util.*;

/**
 * Created by greg on 2/21/16.
 * Generates the turn order for a game randomly, but then repeats the cycle indefinitely.
 */
public class TeamTurnManager implements Iterator<Team> {
    private final List<Team> turnOrder;
    private final List<Team> reverse;
    private Iterator<Team> currentTurn;

    public TeamTurnManager(long seed, Set<Team> teams) {
        turnOrder = new ArrayList<>(teams);
        Collections.shuffle(turnOrder, new Random(seed));
        reverse = new ArrayList<>(turnOrder);
        Collections.reverse(reverse);
        currentTurn = turnOrder.iterator();
    }

    public Team advanceTurn() {
        Team activeTeam = currentTurn.next();
        if (!currentTurn.hasNext())
            currentTurn = turnOrder.iterator();
        return activeTeam;
    }

    public Iterator<Team> forward() {
        return turnOrder.iterator();
    }

    public Iterator<Team> reverse() {
        return reverse.iterator();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Team next() {
        return null;
    }
}
