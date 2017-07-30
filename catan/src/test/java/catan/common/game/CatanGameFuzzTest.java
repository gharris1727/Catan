package catan.common.game;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.event.GameEvent;
import catan.common.structure.game.GameSettings;
import catan.junit.FuzzTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

/**
 * Created by greg on 7/9/16.
 * A set of fuzz tests for the CatanGame class.
 */
@Category(FuzzTests.class)
public class CatanGameFuzzTest {

    private static final int PLAYERS = 3;
    private static final int ROUNDS = 1000;
    private static final long SEED = System.nanoTime();

    @Test
    public void testRandomGame() {
        List<Username> users = GameTestUtils.generatePlayerUsernames(SEED, PLAYERS);
        GameSettings settings = GameTestUtils.createSettings(SEED, users);
        CatanGame game = new CatanGame(settings);
        GameEventGenerator generator = new GameEventGenerator(settings.seed, game);
        CatanGame game2 = new CatanGame(settings);
        for (int i = 0; i < ROUNDS;) {
            GameEvent event = generator.next();
            if (game.test(event) != null)
                continue;
            try {
                //Undo Consistency
                game.execute(event);
                game.undo();
                CatanGameEqualityTester.INSTANCE.assertEquals(game2, game);
                //Forward Consistency
                game.execute(event);
                game2.execute(event);
                CatanGameEqualityTester.INSTANCE.assertEquals(game2, game);
                i++;
            } catch (EventConsumerException ignored) {
                Assert.fail();
            }
        }
        //game.getObserver().readHistory(gameHistory -> System.out.println(gameHistory.getGameEvent()));
    }
}
