package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.GameSettings;

/**
 * Created by Greg on 8/12/2014.
 * Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public class GameThread extends QueuedInputThread<GameEvent> {

    private final QueuedInputThread<GenericEvent> host;
    private CatanGame game;

    public GameThread(QueuedInputThread<GenericEvent> host) {
        super(host.logger);
        this.host = host;
    }

    //Process GameEvents from the event queue.
    @Override
    protected void execute() throws ThreadStop {
        GameEvent event = getEvent(true);
        switch(event.getType()) {
            case Create_Game:
                game = new CatanGame((GameSettings) event.getPayload());
                break;
            case Turn_Advance:
                break;
            case Player_Roll_Dice:
                break;
            case Player_Move_Robber:
                break;
            case Player_Select_Location:
                break;
            case Build_Settlement:
                break;
            case Build_City:
                break;
            case Build_Road:
                break;
        }
    }

    public String toString() {
        return host + "GameThread";
    }
}
