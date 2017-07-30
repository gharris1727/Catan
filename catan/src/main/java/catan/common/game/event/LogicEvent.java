package catan.common.game.event;

import catan.common.event.InternalEvent;
import catan.common.game.CatanGame;

import java.util.Iterator;
import java.util.List;

/**
 * Created by greg on 5/25/16.
 * An event used to express a logical composition of other events.
 */
public class LogicEvent extends InternalEvent<CatanGame, LogicEventType> {

    public LogicEvent(CatanGame origin, LogicEventType type, Object payload) {
        super(origin, type, payload);
    }

    @Override
    public String toString() {
        switch (getType()) {
            case AND:
            case OR:
                @SuppressWarnings("unchecked")
                Iterator<LogicEvent> payload = ((List<LogicEvent>) getPayload()).iterator();
                if (payload.hasNext()) {
                    StringBuilder builder = new StringBuilder("(");
                    builder.append(payload.next());
                    while (payload.hasNext()) {
                        builder.append(getType());
                        builder.append(payload.next());
                    }
                    builder.append(")");
                    return builder.toString();
                }
                return "";
            case NOT:
                return "!(" + getPayload() + ")";
            case NOP:
                return "true";
            case Trigger:
                return ((GameTriggerEvent) getPayload()).getType().toString();
            default:
                throw new IllegalStateException();
        }
    }
}
