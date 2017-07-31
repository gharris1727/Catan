package catan.common.game;

import catan.common.crypto.Username;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.event.GameEvent;
import catan.common.structure.game.GameSettings;
import catan.junit.UnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;

import static catan.common.game.event.GameEventType.*;


/**
 * Created by greg on 3/11/16.
 * Testing class to ensure the CatanGame class is deterministic.
 */
@Category(UnitTests.class)
public class CatanGameTest {

    private final Username greg = new Username("Greg");
    private final Username bob = new Username("Bob");
    private final GameSettings gameSettings = GameTestUtils.createSettings(
        317239L, Arrays.asList(greg, bob));
    private final CatanGameEqualityTester tester = new CatanGameEqualityTester();

    private CatanGame startTwoPlayerGame() {
        CatanGame game = new CatanGame(gameSettings);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(18, 6)), true);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        return game;
    }

    @Test
    public void testCreateNewGame() {
        new CatanGame(gameSettings);
    }

    @Test
    public void testGameBeginning() {
        CatanGame game = new CatanGame(gameSettings);
        CatanGame game2 = new CatanGame(gameSettings);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
    }

    @Test
    public void testWrongBeginning() {
        CatanGame game = new CatanGame(gameSettings);
        CatanGame game2 = new CatanGame(gameSettings);
        //GREG'S TURN
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        //BOB'S TURN
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        //BOB'S TURN 2
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(20,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(20,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        //GREG'S TURN 2
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
    }

    @Test
    public void testWrongTurns() {
        CatanGame game = new CatanGame(gameSettings);
        CatanGame game2 = new CatanGame(gameSettings);

        //GREG'S TURN
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(26,6)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);

        //BOB'S TURN
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,5)), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(18,6)), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        tester.assertEquals(game, game2);
    }

    @Test
    public void testUndo() {
        CatanGame game = startTwoPlayerGame();
        CatanGame game2 = startTwoPlayerGame();
        //undo into the starting states.
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(14,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(20,6)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,7)), true);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        //Turn advance then undo
        GameTestUtils.assertUndo(game2);
        GameTestUtils.assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        tester.assertEquals(game, game2);
        //TODO: test more undo actions.
    }

}