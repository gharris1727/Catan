package catan.client.renderer;

/**
 * Created by greg on 1/20/16.
 * Thrown when a graphical object is not yet able to be rendered.
 */
public class NotYetRenderableException extends RuntimeException {

    public NotYetRenderableException(String message) {
        super(message);
    }

    public NotYetRenderableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
