package com.gregswebserver.catan.test.client;

import com.gregswebserver.catan.client.GameManagerListener;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.event.GameControlEvent;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.gameplay.allocator.RandomTeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.allocator.TeamAllocator;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.generator.random.RandomBoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.resources.BoardLayoutInfo;
import com.gregswebserver.catan.common.resources.GameRulesInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.game.GameProgress;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.test.common.game.EqualityException;
import com.gregswebserver.catan.test.common.game.GameEventGenerator;
import com.gregswebserver.catan.test.common.game.GameTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by greg on 6/29/16.
 * Test framework to test GameManager functionality.
 */
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
    public void testConsistency() throws EqualityException {
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
            a.checkConsistency();
            b.checkConsistency();
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
            fail();
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
            //If there is a local success, then broadcast that to both managers.
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
            GameEventType type = ((GameEvent) event.getPayload()).getType();
            if (type != GameEventType.Offer_Trade && type != GameEventType.Cancel_Trade)
                System.out.println(event);
            synchronized(lock) {
                processing = false;
                lock.notify();
            }
        }

        @Override
        public void remoteFailure(EventConsumerException e) {
            e.printStackTrace();
            synchronized(lock) {
                running = false;
                lock.notify();
            }
        }

        @Override
        public void refreshScreen() {
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
