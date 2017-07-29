package catan.common.game.replays;

import catan.common.config.ConfigSource;
import catan.common.config.EditableConfigSource;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.event.GameEvent;
import catan.common.util.BlockChain;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 7/4/17.
 * A collection of game events that define a game replay.
 * Backwards traversal and branching are supported.
 */
public class Replay implements ReversibleEventConsumer<GameEvent> {

    private final BlockChain<GameEvent> stateStorage;
    private final BigInteger genesis;
    private BigInteger current;
    private final boolean writable;

    {
        try {
            stateStorage = new BlockChain<>("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }

    public Replay(GameEvent genesis) {
        this.genesis = this.current = this.stateStorage.addBlock(null, genesis);
        this.writable = true;
    }

    public Replay(ConfigSource storage) throws ReplayFormatException {
        // Load all nodes from storage and create ReplayNodes for them.
        for (Map.Entry<String, String> entry : storage) {
            String[] arr = entry.getValue().split(",");
            if (arr.length != 2) {
                throw new ReplayFormatException("Incomplete record");
            }
            try {
                BigInteger parent = new BigInteger(arr[0], 16);
                GameEvent event = (GameEvent) GameEvent.deserialize(new BigInteger(arr[1], 16).toByteArray());
                stateStorage.addBlock(parent, event);
            } catch (IOException e) {
                throw new ReplayFormatException("Malformed game event", e);
            }
        }

        this.genesis = this.current = this.stateStorage.resolveGenesis();
        this.writable = false;

        if (this.genesis == null) {
            throw new ReplayFormatException("No unique genesis node found");
        }
    }

    public void save(EditableConfigSource storage) {
        storage.clearEntries();
        for (Map.Entry<BigInteger, GameEvent> entry : stateStorage) {
            String identity = entry.getKey().toString(16);
            String parent = stateStorage.getParent(entry.getKey()).toString(16);
            String event = new BigInteger(entry.getValue().serialize()).toString(16);
            storage.setEntry(identity, parent + "," + event);
        }
    }

    @Override
    public EventConsumerProblem test(GameEvent event) {
        if (this.writable) {
            // Alwas succeed if this replay is writable.
            return null;
        } else {
            // If we are reading from this replay, then test() should reflect whether that event was valid in the replay.
            Iterator<BigInteger> it = this.stateStorage.getChildren(this.current);
            while (it.hasNext()) {
                if (this.stateStorage.get(it.next()).equals(event)) {
                    // We found a child state that exists in the tree, so it's allowed.
                    return null;
                }
            }
            // The event we are search for did not exist in the tree, so it's not allowed.
            return new EventConsumerProblem("Event does not exist in replay!");
        }
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        this.current = this.stateStorage.addBlock(this.current, event);
    }

    @Override
    public void undo() throws EventConsumerException {
        this.current = this.stateStorage.getParent(this.current);
    }
}
