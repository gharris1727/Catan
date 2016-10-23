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
    public void assertEquals(CatanGame expected, CatanGame actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.history, actual.history);
        RandomizerStateEqualityTester.INSTANCE.assertEquals(expected.state, actual.state);
        GameRulesEqualityTester.INSTANCE.assertEquals(expected.rules, actual.rules);
        TeamAllocationEqualityTester.INSTANCE.assertEquals(expected.teamAllocation, actual.teamAllocation);
        GameBoardEqualityTester.INSTANCE.assertEquals(expected.board, actual.board);
        PlayerPoolEqualityTester.INSTANCE.assertEquals(expected.players, actual.players);
        TeamPoolEqualityTester.INSTANCE.assertEquals(expected.teams, actual.teams);
        ScoreStateEqualityTester.INSTANCE.assertEquals(expected.scoring, actual.scoring);
        Assert.assertEquals(expected.listeners, actual.listeners);

    }
}
