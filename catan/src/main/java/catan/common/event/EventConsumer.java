package catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * A stateful object that can use incoming events to change state.
 * Events can be checked before execution using the test() method.
 * An exception in the test method describes the reason for failure
 * and implies that the execution of that event should also fail.
 * When correctly implemented, a successful call to test
 * implies that execution of that same event will complete successfully.
 */
public interface EventConsumer<T extends GenericEvent> {

    EventConsumerProblem test(T event);

    void execute(T event) throws EventConsumerException;

}
