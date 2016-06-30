package com.gregswebserver.catan.test.common.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameHistory;
import com.gregswebserver.catan.common.game.gameplay.allocator.RandomTeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.allocator.TeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.resources.BoardLayoutInfo;
import com.gregswebserver.catan.common.resources.GameRulesInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static com.gregswebserver.catan.common.game.event.GameEventType.*;
import static org.junit.Assert.fail;

/**
 * Created by greg on 3/11/16.
 * Testing class to ensure the CatanGame class is deterministic.
 */
public class CatanGameTest {

    private final Username greg = new Username("Greg");
    private final Username bob = new Username("Bob");
    private final Username jeff = new Username("Jeff");
    private final Username steve = new Username("Steve");
    private GameSettings twoPlayers;
    private GameSettings fourPlayers;

    @Before
    public void setupTwoPlayers() {
        BoardLayout baseLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        GameRules baseRules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator players = new RandomTeamAllocator(new HashSet<>(Arrays.asList(greg, bob)));
        twoPlayers = new GameSettings(0L, baseLayout, RandomBoardGenerator.instance, baseRules, players);
    }

    @Before
    public void setupFourPlayers() {
        BoardLayout baseLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        GameRules baseRules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator players = new RandomTeamAllocator(new HashSet<>(Arrays.asList(greg, bob, jeff, steve)));
        fourPlayers = new GameSettings(0L, baseLayout, RandomBoardGenerator.instance, baseRules, players);
    }

    private CatanGame startTwoPlayerGame() {
        CatanGame game = new CatanGame(twoPlayers);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(18, 6)), true);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        return game;
    }

    private void assertExecute(CatanGame game, GameEvent event, boolean expectSuccess) {
        try {
            game.execute(event);
            if (!expectSuccess)
                fail();
        } catch (EventConsumerException ignored) {
            if (expectSuccess) {
                ignored.printStackTrace();
                fail();
            }
        }
    }

    private void assertUndo(CatanGame game) {
        try {
            game.undo();
        } catch (EventConsumerException ignored) {
            fail();
        }
    }

    @Test
    public void testCreateNewGame() {
        new CatanGame(twoPlayers);
    }

    @Test
    public void testGameBeginning() throws EqualityException {
        CatanGame game = new CatanGame(twoPlayers);
        CatanGame game2 = new CatanGame(twoPlayers);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
    }

    @Test
    public void testWrongBeginning() throws EqualityException {
        CatanGame game = new CatanGame(twoPlayers);
        CatanGame game2 = new CatanGame(twoPlayers);
        //GREG'S TURN
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        //BOB'S TURN
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,5)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,5)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
        //BOB'S TURN 2
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(20,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(20,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
        //GREG'S TURN 2
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
    }

    @Test
    public void testWrongTurns() throws EqualityException {
        CatanGame game = new CatanGame(twoPlayers);
        CatanGame game2 = new CatanGame(twoPlayers);

        //GREG'S TURN
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(26,6)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(greg, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);

        //BOB'S TURN
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(16,5)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(23,5)), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(18,6)), false);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), false);
        game.assertEquals(game2);
        assertExecute(game, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        game.assertEquals(game2);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), false);
        game.assertEquals(game2);
    }

    @Test
    public void testUndo() throws EqualityException {
        CatanGame game = startTwoPlayerGame();
        CatanGame game2 = startTwoPlayerGame();
        //undo into the starting states.
        assertUndo(game2);
        assertUndo(game2);
        assertUndo(game2);
        assertUndo(game2);
        assertUndo(game2);
        assertUndo(game2);
        assertExecute(game2, new GameEvent(bob, Build_Settlement, new Coordinate(14,6)), true);
        assertExecute(game2, new GameEvent(bob, Build_Road, new Coordinate(20,6)), true);
        assertExecute(game2, new GameEvent(bob, Turn_Advance, null), true);
        assertExecute(game2, new GameEvent(greg, Build_Settlement, new Coordinate(16,7)), true);
        assertExecute(game2, new GameEvent(greg, Build_Road, new Coordinate(23,7)), true);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        //Turn advance then undo
        assertUndo(game2);
        assertExecute(game2, new GameEvent(greg, Turn_Advance, null), true);
        game.assertEquals(game2);
        //TODO: test more undo actions.
    }

    @Test
    public void testRandomGame() throws EqualityException {
        int rounds = 100;
        GameEventGenerator generator = new GameEventGenerator(0L, twoPlayers.playerTeams.getUsers());
        CatanGame game = startTwoPlayerGame();
        CatanGame game2 = startTwoPlayerGame();
        for (int i = 0; i < rounds;) {
            GameEvent event = generator.next();
            try {
                game.test(event);
                //Undo Consistency
                game.execute(event);
                game.undo();
                game.assertEquals(game2);
                //Forward Consistency
                game.execute(event);
                game2.execute(event);
                game.assertEquals(game2);
                i++;
            } catch (EventConsumerException e) {
                try {
                    game2.test(event);
                    fail();
                } catch (EventConsumerException ignored) {
                }
            } catch (EqualityException e) {
                throw new RuntimeException("Consistency error on: " + event, e);
            }
        }
        for (GameHistory gameHistory : game.getHistory()) {
            System.out.println(gameHistory.getGameEvent());
        }
    }

}