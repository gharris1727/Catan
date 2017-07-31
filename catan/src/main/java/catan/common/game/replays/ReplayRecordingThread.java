package catan.common.game.replays;

import catan.common.config.EditableConfigSource;
import catan.common.event.QueuedInputThread;

/**
 * Created by greg on 7/3/17.
 * Thread for accepting Game Events to record.
 */
public class ReplayRecordingThread extends QueuedInputThread<ReplayEvent> {

    private Replay replay;
    private final EditableConfigSource storage;

    protected ReplayRecordingThread(EditableConfigSource storage) {
        this.storage = storage;
    }

    @Override
    protected void execute() throws ThreadStopException {
        ReplayEvent event = getEvent(true);
    }

    @Override
    public String toString() {
        return null;
    }
}
