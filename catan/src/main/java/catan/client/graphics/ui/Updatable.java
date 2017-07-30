package catan.client.graphics.ui;

/**
 * Created by greg on 6/4/16.
 * Interface for objects that have structure that needs an update following an external change.
 */
@FunctionalInterface
public interface Updatable {

    void update();
}
