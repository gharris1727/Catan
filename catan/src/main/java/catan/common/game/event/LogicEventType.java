package catan.common.game.event;

import catan.common.event.EventType;

import java.util.List;

/**
 * Created by greg on 5/25/16.
 * A logic event that serves as a logical composition of different event types.
 */
public enum LogicEventType implements EventType {

    AND(List.class),
    OR(List.class),
    NOT(LogicEvent.class),
    NOP(null),
    Trigger(GameTriggerEvent.class);

    private final Class payloadType;

    LogicEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }


}
