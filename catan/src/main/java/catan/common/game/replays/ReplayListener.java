package catan.common.game.replays;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.event.GameEvent;
import catan.common.game.listeners.GameEventListener;

/**
 * Created by greg on 7/3/17.
 * A Game Listener that
 */
public class ReplayListener implements GameEventListener {

    private ReplayRecordingThread replayThread;

    public ReplayListener(ReplayRecordingThread replayThread) {
        this.replayThread = replayThread;
    }

    @Override
    public void reportExecuteException(EventConsumerException event) {
    }

    @Override
    public void reportUndoException(EventConsumerException event) {
    }

    @Override
    public EventConsumerProblem test(GameEvent event) {
        return null;
    }

    @Override
    public void reportTestProblem(EventConsumerProblem e) {
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        replayThread.addEvent(new ReplayEvent(this, ReplayEventType.Execute, event));
    }

    @Override
    public void undo() throws EventConsumerException {
        replayThread.addEvent(new ReplayEvent(this, ReplayEventType.Undo, null));
    }
}
