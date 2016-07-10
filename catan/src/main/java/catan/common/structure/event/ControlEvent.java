package catan.common.structure.event;

import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;

/**
 * Created by Greg on 10/16/2014.
 * An external event dealing with things related to server connections and disconnections.
 */
public class ControlEvent extends ExternalEvent<ControlEventType> {

    public ControlEvent(Username origin, ControlEventType type, Object payload) {
        super(origin, type, payload);
    }
}
