package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.util.*;

/**
 * Created by greg on 2/21/16.
 * Generates the turn order for a game randomly, but then repeats the cycle indefinitely.
 */
public class TeamTurnRandomizer implements Iterator<Team> {
    private final List<Team> turnOrder;
    private Iterator<Team> currentTurn;
    private Team activeTeam;

    public TeamTurnRandomizer(long seed, Set<Team> teams) {
        turnOrder = new ArrayList<>(teams);
        Collections.shuffle(turnOrder, new Random(seed));
        currentTurn = turnOrder.iterator();
        advanceTurn();
    }

    public void advanceTurn() {
        activeTeam = currentTurn.next();
        if (!currentTurn.hasNext())
            currentTurn = turnOrder.iterator();
    }

    public Team getActiveTeam() {
        return activeTeam;
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
