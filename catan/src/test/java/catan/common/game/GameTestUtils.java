package catan.common.game;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.event.GameEvent;
import catan.common.game.gameplay.allocator.RandomTeamAllocator;
import catan.common.game.gameplay.allocator.TeamAllocator;
import catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.scoring.rules.GameRules;
import catan.common.resources.BoardLayoutInfo;
import catan.common.resources.GameRulesInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.game.GameSettings;
import org.junit.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by greg on 6/29/16.
 * A set of utility functions for testing elements of gameplay.
 */
public class GameTestUtils {

    public static Set<Username> generatePlayerUsernames(long seed, int count) {
        Random random = new Random(seed);
        Set<Username> users = new HashSet<>();
        int length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            length = random.nextInt(10)+4;
            builder.setLength(0);
            builder.ensureCapacity(length);
            for (int j = 0; j < length; j++)
                builder.append((char) (random.nextInt('z'-'a') + 'a'));
            users.add(new Username(builder.toString()));
        }
        return Collections.unmodifiableSet(users);
    }

    public static GameSettings createSettings(long seed, Set<Username> usernames) {
        BoardLayout baseLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        GameRules baseRules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator players = new RandomTeamAllocator(usernames);
        return new GameSettings(seed, baseLayout, RandomBoardGenerator.instance, baseRules, players);
    }

    public static void assertExecute(CatanGame game, GameEvent event, boolean expectSuccess) {
        try {
            game.execute(event);
            if (!expectSuccess)
                Assert.fail();
        } catch (EventConsumerException ignored) {
            if (expectSuccess) {
                ignored.printStackTrace();
                Assert.fail();
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

    public static void assertGameEquals(CatanGame a, CatanGame b) {
        CatanGameEqualityTester.INSTANCE.assertEquals(a, b);
    }
}
