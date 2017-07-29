package catan.common.game.replays;

import catan.common.config.EditableConfigSource;
import catan.common.event.EventType;
import catan.common.game.event.GameEvent;

/**
 * Created by greg on 7/9/17.
 * An enumeration of all types of events that can be applied to a replay.
 */
public enum ReplayEventType implements EventType {

    Execute(GameEvent.class),
    Undo(null),
    Save(EditableConfigSource.class);

    private Class payloadType;

    ReplayEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
