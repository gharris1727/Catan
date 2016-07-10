package catan.common.game;

import catan.common.game.board.GameBoardEqualityTester;
import catan.common.game.gameplay.allocator.TeamAllocationEqualityTester;
import catan.common.game.gamestate.RandomizerStateEqualityTester;
import catan.common.game.players.PlayerPoolEqualityTester;
import catan.common.game.scoring.ScoreStateEqualityTester;
import catan.common.game.scoring.rules.GameRulesEqualityTester;
import catan.common.game.teams.TeamPoolEqualityTester;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * A tester to assert the equality of two CatanGames.
 */
public class CatanGameEqualityTester implements EqualityTester<CatanGame> {

    public static final CatanGameEqualityTester INSTANCE = new CatanGameEqualityTester();

    private CatanGameEqualityTester() {
    }

    @Override
    public void assertEquals(CatanGame a, CatanGame b) {
        if (a == b)
            return;

        Assert.assertEquals(a.history, b.history);
        RandomizerStateEqualityTester.INSTANCE.assertEquals(a.state, b.state);
        GameRulesEqualityTester.INSTANCE.assertEquals(a.rules, b.rules);
        TeamAllocationEqualityTester.INSTANCE.assertEquals(a.teamAllocation, b.teamAllocation);
        GameBoardEqualityTester.INSTANCE.assertEquals(a.board, b.board);
        PlayerPoolEqualityTester.INSTANCE.assertEquals(a.players, b.players);
        TeamPoolEqualityTester.INSTANCE.assertEquals(a.teams, b.teams);
        ScoreStateEqualityTester.INSTANCE.assertEquals(a.scoring, b.scoring);
        Assert.assertEquals(a.listeners, b.listeners);

    }
}
