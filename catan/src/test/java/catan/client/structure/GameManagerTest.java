package catan.client.structure;

import catan.client.GameManagerListener;
import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.GameEventGenerator;
import catan.common.game.GameTestUtils;
import catan.common.game.event.GameControlEvent;
import catan.common.game.event.GameControlEventType;
import catan.common.game.event.GameEvent;
import catan.common.game.gameplay.allocator.RandomTeamAllocator;
import catan.common.game.gameplay.allocator.TeamAllocator;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.scoring.rules.GameRules;
import catan.common.log.Logger;
import catan.common.resources.BoardLayoutInfo;
import catan.common.resources.GameRulesInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;
import catan.junit.FuzzTests;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.util.*;

/**
 * Created by greg on 6/29/16.
 * Test framework to test GameManager functionality.
 */
@Category(FuzzTests.class)
public class GameManagerTest {

    private final Object lock = new Object();
    private final long seed = 0L;
    private boolean waiting;
    private boolean running;
    private Logger logger;
    private Set<Username> players;
    private GameProgress newGame;
    private Random random;
    private GameManager a;
    private GameManager b;

    @Before
    public void setup() {
        logger = new Logger();
        logger.useStdOut();
        players = GameTestUtils.generatePlayerUsernames(seed, 2);
        BoardLayout boardLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        BoardGenerator boardGenerator = RandomBoardGenerator.instance;
        GameRules rules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator teams = new RandomTeamAllocator(players);
        GameSettings settings = new GameSettings(seed, boardLayout, boardGenerator, rules, teams);
        newGame = new GameProgress(settings, Collections.emptyList());
        random = new Random(seed);
    }

    @After
    public void cleanup() {
        if (a!= null)
            a.join();
        if (b!= null)
            b.join();
    }

    @Test
    @Ignore
    public void testConsistency() {
        GameManagerBroadcastListener aListener = new GameManagerBroadcastListener("TestA");
        GameManagerBroadcastListener bListener = new GameManagerBroadcastListener("TestB");
        a = new GameManager(aListener, logger, players.iterator().next(), newGame);
        b = new GameManager(bListener, logger, players.iterator().next(), newGame);
        GameEventGenerator aGenerator = new GameEventGenerator(seed, a.getLocalGame());
        GameEventGenerator bGenerator = new GameEventGenerator(seed, b.getLocalGame());
        aListener.others = bListener.others = new HashSet<>(Arrays.asList(a, b));
        running = true;
        while(running && a.getLocalGame().getHistory().size() < 1000) {
            waiting = true;
            aListener.processing = bListener.processing = true;
            GameTestUtils.assertGameEquals(a.getLocalGame(), b.getLocalGame());
            GameTestUtils.assertGameEquals(b.getLocalGame(), b.getRemoteGame());
            GameTestUtils.assertGameEquals(b.getRemoteGame(), a.getRemoteGame());
            GameTestUtils.assertGameEquals(a.getRemoteGame(), a.getLocalGame());
            if (random.nextInt(2) == 0) {
                a.local(aGenerator.next());
            } else {
                b.local(bGenerator.next());
            }
            synchronized(lock) {
                while (running && waiting && (aListener.processing || bListener.processing))
                    try {
                        lock.wait();
                    } catch (InterruptedException ignored) {
                    }
            }
        }
        if (a.getLocalGame().getHistory().size() != 1000)
            Assert.fail();
    }

    private class GameManagerBroadcastListener implements GameManagerListener {

        private final String name;
        private Set<GameManager> others;
        private boolean processing;

        private GameManagerBroadcastListener(String name) {
            this.name = name;
        }

        @Override
        public void localSuccess(GameControlEvent event) {
            logger.debug(this, "local " + event.getType() + " success" + event);
            //If there is a local success, then broadcast that to both managers.
            if (event.getType() == GameControlEventType.Test)
                for (GameManager other : others)
                  other.remote((GameEvent) event.getPayload());
        }

        @Override
        public void localFailure(EventConsumerException e) {
            //If the thing failed locally, then we should skip this event.
            synchronized (lock) {
                waiting = false;
                lock.notify();
            }
        }

        @Override
        public void remoteSuccess(GameControlEvent event) {
            synchronized(lock) {
                logger.debug(this, "remote success " + event);
                processing = false;
                lock.notify();
            }
        }

        @Override
        public void remoteFailure(EventConsumerException e) {
            synchronized(lock) {
                logger.debug(this, "remote failure: " + e.getMessage());
                running = false;
                lock.notify();
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
