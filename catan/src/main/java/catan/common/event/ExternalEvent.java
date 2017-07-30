package catan.common.event;

import catan.common.crypto.Username;

import java.io.*;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin username information.
 */
public abstract class ExternalEvent<T extends EventType> extends GenericEvent implements Serializable, TypedPayloadEvent<Username, T> {

    private final Username origin;
    private final T type;
    private final Object payload;

    protected ExternalEvent(Username origin, T type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
        if ((payload != null) && !(payload instanceof Serializable))
            throw new EventPayloadError(payload, Serializable.class);
    }

    @Override
    public Username getOrigin() {
        return origin;
    }

    @Override
    public T getType() {
        return type;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    public String toString() {
        return "[EXTERNAL] O(" + origin + ") T(" + type + ") P(" + payload + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalEvent)) return false;

        ExternalEvent<?> other = (ExternalEvent<?>) o;

        if ((origin != null) ? !origin.equals(other.origin) : (other.origin != null)) return false;
        if (!type.equals(other.type)) return false;
        return (payload != null) ? payload.equals(other.payload) : (other.payload == null);

    }

    @Override
    public int hashCode() {
        int result = (origin != null) ? origin.hashCode() : 0;
        result = (31 * result) + type.hashCode();
        result = (31 * result) + ((payload != null) ? payload.hashCode() : 0);
        return result;
    }

    public byte[] serialize() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(this);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExternalEvent deserialize(byte[] serialized) throws IOException {
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
            return (ExternalEvent) objectStream.readObject();
        } catch (ClassCastException | ClassNotFoundException e) {
            throw new IOException("Could not deserialize GameEvent.", e);
        }
    }
}
