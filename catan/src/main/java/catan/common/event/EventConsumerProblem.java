package catan.common.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 7/1/16.
 * A partner to the EventConsumerException for indicating the success/failure of the EventConsumer::test method.
 */
public class EventConsumerProblem {

    private final String message;
    private final List<EventConsumerProblem> causes;

    public EventConsumerProblem(String message) {
        this.message = message;
        this.causes = new ArrayList<>();
    }

    public void addCause(EventConsumerProblem problem) {
        causes.add(problem);
    }

    private StringBuilder print(StringBuilder out, int depth) {
        for (int i = 0; i < depth; i++)
            out.append('\t');
        out.append(message);
        if (!causes.isEmpty()) {
            out.append('\n');
            for (int i = 0; i < depth; i++)
                out.append('\t');
            out.append("Caused By:");
        }
        for (EventConsumerProblem cause : causes) {
            out.append('\n');
            cause.print(out, depth + 1);
        }
        return out;
    }

    public String toString() {
        return message;
    }

    public String getMessage() {
        return print(new StringBuilder("Problem while testing event: "), 0).toString();
    }
}
