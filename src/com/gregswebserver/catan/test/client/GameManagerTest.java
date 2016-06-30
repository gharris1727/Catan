package com.gregswebserver.catan.test.client;

import com.gregswebserver.catan.client.GameManagerListener;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.event.GameControlEvent;
import com.gregswebserver.catan.common.game.event.GameEvent;
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
import com.gregswebserver.catan.test.common.game.GameEventGenerator;
import com.gregswebserver.catan.test.common.game.GameTestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

/**
 * Created by greg on 6/29/16.
 * Test framework to test GameManager functionality.
 */
public class GameManagerTest {

    private Logger logger;
    private Set<Username> players;
    private GameProgress newGame;

    @Before
    public void setup() {
        logger = new Logger();
        logger.useStdOut();
        players = GameTestUtils.generatePlayerUsernames(4);
        BoardLayout boardLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("base"));
        BoardGenerator boardGenerator = RandomBoardGenerator.instance;
        GameRules rules = ResourceLoader.getGameRuleSet(new GameRulesInfo("default"));
        TeamAllocator teams = new RandomTeamAllocator(players);
        GameSettings settings = new GameSettings(System.nanoTime(), boardLayout, boardGenerator, rules, teams);
        newGame = new GameProgress(settings, Collections.emptyList());
    }

    @Test
    public void testConsistency() {
        GameEventGenerator eventGenerator = new GameEventGenerator(System.nanoTime(), players);
        GameManagerBroadcastListener aListener = new GameManagerBroadcastListener();
        GameManagerBroadcastListener bListener = new GameManagerBroadcastListener();
        GameManager a = new GameManager(aListener, logger, players.iterator().next(), newGame);
        GameManager b = new GameManager(bListener, logger, players.iterator().next(), newGame);
        aListener.others = Collections.singleton(b);
        bListener.others = Collections.singleton(a);
        for(int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                a.local(eventGenerator.next());
            } else {
                b.local(eventGenerator.next());
            }
        }
        System.out.println(a.getRemoteGame().getHistory());
    }

    private class GameManagerBroadcastListener implements GameManagerListener {

        private Set<GameManager> others;

        @Override
        public void localSuccess(GameControlEvent event) {
            for (GameManager other : others)
                other.remote((GameEvent) event.getPayload());
        }

        @Override
        public void localFailure(EventConsumerException e) {
        }

        @Override
        public void refreshScreen() {
        }
    }

}
