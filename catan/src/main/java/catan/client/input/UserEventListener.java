package catan.client.input;

/**
 * Created by greg on 6/19/16.
 * An object that listens to incoming user events.
 */
public interface UserEventListener {

    void onUserEvent(UserEvent event);

}
