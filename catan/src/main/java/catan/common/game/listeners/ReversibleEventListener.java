package catan.common.game.listeners;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.GenericEvent;
import catan.common.event.ReversibleEventConsumer;

/**
 * Created by greg on 7/9/17.
 * Superclass of external listeners for events that are reversible.
 */
public interface ReversibleEventListener<T extends GenericEvent> extends ReversibleEventConsumer<T> {

    void reportTestProblem(EventConsumerProblem e);

    void reportExecuteException(EventConsumerException event);

    void reportUndoException(EventConsumerException event);

}
