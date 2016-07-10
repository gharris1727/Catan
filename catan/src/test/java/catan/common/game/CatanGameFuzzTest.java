package catan.common.game;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameHistory;
import catan.common.structure.game.GameSettings;
import catan.junit.FuzzTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Set;

/**
 * Created by greg on 7/9/16.
 * A set of fuzz tests for the CatanGame class.
 */
@Category(FuzzTests.class)
public class CatanGameFuzzTest {

    private static final int players = 3;
    private static final int rounds = 1000;
    private static final long seed = System.nanoTime();

    @Test
    public void testRandomGame() {
        Set<Username> users = GameTestUtils.generatePlayerUsernames(seed, players);
        GameSettings settings = GameTestUtils.createSettings(seed, users);
        CatanGame game = new CatanGame(settings);
        GameEventGenerator generator = new GameEventGenerator(settings.seed, game);
        CatanGame game2 = new CatanGame(settings);
        for (int i = 0; i < rounds;) {
            GameEvent event = generator.next();
            if (game.test(event) != null)
                continue;
            try {
                //Undo Consistency
                game.execute(event);
                game.undo();
                CatanGameEqualityTester.INSTANCE.assertEquals(game, game2);
                //Forward Consistency
                game.execute(event);
                game2.execute(event);
                CatanGameEqualityTester.INSTANCE.assertEquals(game, game2);
                i++;
            } catch (EventConsumerException e) {
                Assert.fail();
            }
        }
        for (GameHistory gameHistory : game.getHistory()) {
            System.out.println(gameHistory.getGameEvent());
        }
    }
}
