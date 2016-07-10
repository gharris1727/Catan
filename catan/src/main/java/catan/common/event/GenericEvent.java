package catan.common.event;

/**
 * Created by Greg on 8/12/2014.
 * Event superclass extended by other custom events.
 */
public abstract class GenericEvent {

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}
