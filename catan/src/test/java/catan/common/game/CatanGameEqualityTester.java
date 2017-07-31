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
public final class CatanGameEqualityTester implements EqualityTester<CatanGame> {

    private RandomizerStateEqualityTester randomizerTester = new RandomizerStateEqualityTester();
    private GameRulesEqualityTester rulesTester = new GameRulesEqualityTester();
    private TeamAllocationEqualityTester allocationTester = new TeamAllocationEqualityTester();
    private GameBoardEqualityTester boardTester = new GameBoardEqualityTester();
    private PlayerPoolEqualityTester playerTester = new PlayerPoolEqualityTester();
    private TeamPoolEqualityTester teamTester = new TeamPoolEqualityTester();
    private ScoreStateEqualityTester scoreTester = new ScoreStateEqualityTester();

    @Override
    public void assertEquals(CatanGame expected, CatanGame actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.history, actual.history);
        randomizerTester.assertEquals(expected.state, actual.state);
        rulesTester.assertEquals(expected.rules, actual.rules);
        allocationTester.assertEquals(expected.teamAllocation, actual.teamAllocation);
        boardTester.assertEquals(expected.board, actual.board);
        playerTester.assertEquals(expected.players, actual.players);
        teamTester.assertEquals(expected.teams, actual.teams);
        scoreTester.assertEquals(expected.scoring, actual.scoring);

    }
}
