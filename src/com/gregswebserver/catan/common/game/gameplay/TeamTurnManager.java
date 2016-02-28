package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.util.*;

/**
 * Created by greg on 2/21/16.
 * Generates the turn order for a game randomly, but then repeats the cycle indefinitely.
 */
public class TeamTurnManager {
    private final List<Team> forward;
    private final List<Team> reverse;
    private Iterator<Team> currentTurn;
    private Team currentTeam;
    private GameState state;

    public TeamTurnManager(long seed, Set<Team> teams) {
        forward = new ArrayList<>(teams);
        Collections.shuffle(forward, new Random(seed));
        reverse = new ArrayList<>(forward);
        Collections.reverse(reverse);
        currentTurn = forward.iterator();
        currentTeam = currentTurn.next();
        state = GameState.IntialPlacement;
    }

    public Team advanceTurn() {
        if (!currentTurn.hasNext())
            switch (state) {
                case IntialPlacement:
                    currentTurn = reverse.iterator();
                    state = GameState.ReversePlacement;
                    break;
                case ReversePlacement:
                    currentTurn = forward.iterator();
                    state = GameState.Regular;
                    break;
                case Regular:
                    currentTurn = forward.iterator();
                    break;
            }
        currentTeam = currentTurn.next();
        return currentTeam;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public boolean starting() {
        return state == GameState.IntialPlacement || state == GameState.ReversePlacement;
    }

    public enum GameState {
        IntialPlacement, ReversePlacement, Regular
    }
}
