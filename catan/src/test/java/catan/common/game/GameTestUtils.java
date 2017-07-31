package catan.common.game;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.event.GameEvent;
import catan.common.game.gameplay.allocator.RandomTeamAllocator;
import catan.common.game.gameplay.allocator.TeamAllocator;
import catan.common.game.gameplay.generator.RandomBoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.scoring.rules.GameRules;
import catan.common.resources.BoardLayoutInfo;
import catan.common.resources.GameRulesInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.game.GameSettings;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by greg on 6/29/16.
 * A set of utility functions for testing elements of gameplay.
 */
public final class GameTestUtils {

    private GameTestUtils() {
    }

    public static List<Username> generatePlayerUsernames(long seed, int count) {
        Random random = new Random(seed);
        List<Username> users = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int length = random.nextInt(10)+4;
            builder.setLength(0);
            builder.ensureCapacity(length);
            for (int j = 0; j < length; j++)
                builder.append((char) (random.nextInt('z'-'a') + 'a'));
            users.add(new Username(builder.toString()));
        }
        return Collections.unmodifiableList(users);
    }

    public static GameSettings createSettings(long seed, List<Username> usernames) {
        BoardLayout baseLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        GameRules baseRules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator players = new RandomTeamAllocator(usernames);
        return new GameSettings(seed, baseLayout, new RandomBoardGenerator(), baseRules, players);
    }

    public static void assertExecute(CatanGame game, GameEvent event, boolean expectSuccess) {
        EventConsumerProblem problem = game.test(event);
        if ((problem == null) && !expectSuccess) {
            Assert.fail("Expected test to fail for " + event);
        } else if ((problem != null) && expectSuccess) {
            Assert.fail("Expected test to succeed for " + event + " but problem occurred: " + problem);
        }
        try {
            game.execute(event);
            if (!expectSuccess)
                Assert.fail();
        } catch (EventConsumerException e) {
            if (expectSuccess) {
                throw new AssertionError(e);
            }
        }
    }

    public static void assertUndo(CatanGame game) {
        try {
            game.undo();
        } catch (EventConsumerException ignored) {
            Assert.fail();
        }
    }

}
