package catan.common.game.replays;

import catan.common.config.DummyConfigSource;
import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.GameTestUtils;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.event.GameEvent;
import catan.common.structure.game.GameSettings;
import catan.junit.UnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;

import static catan.common.game.event.GameEventType.*;

/**
 * Created by greg on 7/10/17.
 * Class to test functionality of the Replay class.
 */
@Category(UnitTests.class)
public class ReplayTest {

    private final Username greg = new Username("Greg");
    private final Username bob = new Username("Bob");
    private final GameSettings gameSettings = GameTestUtils.createSettings(
            317239L, Arrays.asList(greg, bob));

    private final GameEvent genesis = new GameEvent(null, Start, gameSettings);
    private final GameEvent[] gameA = {
            new GameEvent(bob, Build_Settlement, new Coordinate(18,6)),
            new GameEvent(bob, Build_Road, new Coordinate(26,6)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(16,5)),
            new GameEvent(greg, Build_Road, new Coordinate(23,5)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(14,6)),
            new GameEvent(greg, Build_Road, new Coordinate(20,6)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(bob, Build_Settlement, new Coordinate(16,7)),
            new GameEvent(bob, Build_Road, new Coordinate(23,7)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Turn_Advance, null),
    };
    private final GameEvent[] gameB = {
            new GameEvent(bob, Build_Settlement, new Coordinate(16,7)),
            new GameEvent(bob, Build_Road, new Coordinate(26,6)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(16,5)),
            new GameEvent(greg, Build_Road, new Coordinate(23,5)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(14,6)),
            new GameEvent(greg, Build_Road, new Coordinate(20,6)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(bob, Build_Settlement, new Coordinate(18,6)),
            new GameEvent(bob, Build_Road, new Coordinate(23,7)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Turn_Advance, null),
    };
    private final GameEvent[] gameC = {
            new GameEvent(bob, Build_Settlement, new Coordinate(18,6)),
            new GameEvent(bob, Build_Road, new Coordinate(26,6)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(14,6)),
            new GameEvent(greg, Build_Road, new Coordinate(23,5)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(greg, Build_Settlement, new Coordinate(16,5)),
            new GameEvent(greg, Build_Road, new Coordinate(20,6)),
            new GameEvent(greg, Turn_Advance, null),
            new GameEvent(bob, Build_Settlement, new Coordinate(16,7)),
            new GameEvent(bob, Build_Road, new Coordinate(23,7)),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(bob, Turn_Advance, null),
            new GameEvent(greg, Turn_Advance, null),
    };

    @Test
    public void testNewGame() throws EventConsumerException {
        Replay replay = new Replay(genesis);
        for (GameEvent e : gameA) {
            replay.execute(e);
        }
        for (GameEvent e : gameA) {
            replay.undo();
        }
        for (GameEvent e : gameB) {
            replay.execute(e);
        }
        for (GameEvent e : gameB) {
            replay.undo();
        }
        DummyConfigSource storage = new DummyConfigSource();
        replay.save(storage);
    }

    @Test
    public void testExistingGame() throws EventConsumerException, ReplayFormatException {
        Replay replay = new Replay(genesis);
        for (GameEvent e : gameA) {
            replay.execute(e);
        }
        for (GameEvent e : gameA) {
            replay.undo();
        }
        for (GameEvent e : gameB) {
            replay.execute(e);
        }
        for (GameEvent e : gameB) {
            replay.undo();
        }
        DummyConfigSource storage = new DummyConfigSource();
        replay.save(storage);

        Replay copy = new Replay(storage);
        for (GameEvent e : gameA) {
            Assert.assertNull(copy.test(e));
            copy.execute(e);
        }
        for (GameEvent e : gameA) {
            copy.undo();
        }
        for (GameEvent e : gameC) {
            if (copy.test(e) != null) {
                return;
            }
            replay.execute(e);
        }
    }
}
