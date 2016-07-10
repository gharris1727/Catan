package catan.client;

import catan.common.event.EventConsumerException;
import catan.common.game.event.GameControlEvent;

/**
 * Created by greg on 6/29/16.
 * Interface for a class that listens to the actions of the GameManager.
 */
public interface GameManagerListener {

    void localSuccess(GameControlEvent event);

    void localFailure(EventConsumerException e);

    void remoteSuccess(GameControlEvent event);

    void remoteFailure(EventConsumerException e);

}
