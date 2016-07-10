package catan.common.game.listeners;

import catan.common.event.EventConsumer;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/28/16.
 *
 */
public interface GameListener extends EventConsumer<GameTriggerEvent> {

    void reportExecuteError(EventConsumerException event);

    void reportTestProblem(EventConsumerProblem e);

}
