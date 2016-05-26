package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.InternalEvent;
import com.gregswebserver.catan.common.game.CatanGame;

import java.util.List;

/**
 * Created by greg on 5/25/16.
 * An event used to express a logical composition of other events.
 */
public class LogicEvent extends InternalEvent<CatanGame, LogicEventType> {

    public LogicEvent(CatanGame origin, LogicEventType type, Object payload) {
        super(origin, type, payload);
    }

    //TODO: find a clean way to print these events. It may be useful when debugging the game logic later.
    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        String out = "";
        switch (getType()) {
            case AND:
            case OR:
                out += "\n";
                for (LogicEvent child : (List<LogicEvent>)getPayload())
                    out += getType() + " " + child + "\n";
                break;
            case NOT:
            case NOP:
            case Player_Event:
            case Board_Event:
            case Team_Event:
            case GameState_Event:
                out += getType() + " " + getPayload().toString();
        }
        return "(" + out + ")";
    }
}
