package com.gregswebserver.catan.common.game.listeners;

import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
import com.gregswebserver.catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/28/16.
 *
 */
public interface GameListener extends EventConsumer<GameTriggerEvent> {

    void reportExecuteError(EventConsumerException event);

    void reportTestProblem(EventConsumerProblem e);

}
