package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderEventType;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;

/**
 * Created by Greg on 8/12/2014.
 * Main game thread that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public class GameThread extends QueuedInputThread {

    private Client client;
    private CatanGame game;

    public GameThread(Client client) {
        super(client.logger);
        this.client = client;
    }

    //Process GameEvents from the event queue.
    protected void execute() throws ThreadStop {
        GameEvent event = (GameEvent) getEvent(true);
        switch (event.getType()) {
            case Build_Settlement:
                break;
            case Build_City:
                break;
            case Build_Road:
                break;
            case Trade_Offer:
                break;
            case Trade_Accept:
                break;
            case Trade_Bank:
                break;
            case Player_Select_Location:
                break;
            case Player_Move_Robber:
                break;
            case Player_Roll_Dice:
                break;
        }
        client.addEvent(new RenderEvent(this, RenderEventType.Game_Update, null));
    }

    public String toString() {
        return client + "GameThread";
    }
}
