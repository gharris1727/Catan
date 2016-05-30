package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.InternalEvent;
import com.gregswebserver.catan.common.game.CatanGame;

import java.util.ArrayList;
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
        ArrayList<String> output = new ArrayList<>();
        print(output, 0);
        StringBuilder out = new StringBuilder();
        for (String line : output) {
            out.append('\n');
            out.append(line);
        }
        return out.toString();
    }

    @SuppressWarnings("unchecked")
    private void print(List<String> output, int depth) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i <= depth; i++)
            line.append('\t');
        switch (getType()) {
            case AND:
            case OR:
                line.append(getType().toString());
                output.add(line.toString());
                for (LogicEvent child : (List<LogicEvent>) getPayload())
                    child.print(output, depth + 1);
                break;
            case NOT:
                line.append(getType().toString());
                output.add(line.toString());
                print(output, depth + 1);
                break;
            case NOP:
                line.append(getType().toString());
                output.add(line.toString());
                break;
            case Trigger:
                line.append(getPayload().toString());
                output.add(line.toString());
                break;
        }
    }
}
