package catan.common.game.scoring;

import catan.common.crypto.Username;
import catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/26/16.
 * Event that can be tracked to provide scoring information.
 */
public class ScoreEvent extends GameTriggerEvent<Username, ScoreEventType> {

    public ScoreEvent(Username origin, ScoreEventType type, Object payload) {
        super(origin, type, payload);
    }
}
