package catan.common.game.replays;

import catan.common.event.InternalEvent;

/**
 * Created by greg on 7/9/17.
 * Type of event defining events that can occur to a replay.
 */
public class ReplayEvent extends InternalEvent<ReplayListener, ReplayEventType> {

    protected ReplayEvent(ReplayListener origin, ReplayEventType type, Object payload) {
        super(origin, type, payload);
    }
}
