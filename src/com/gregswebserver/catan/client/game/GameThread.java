package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderEventType;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.gameplay.GameAction;
import com.gregswebserver.catan.game.gameplay.GameType;

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
        switch (event.type) {
            case Game_Create:
                //Create a new game, load the GameType data, and preform a first time rendering.
                game = new CatanGame((GameType) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Create, game));
                break;
            case Player_Join:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Player_Update, event.origin));
                break;
            case Player_Leave:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Player_Update, event.origin));
                break;
            case Player_Build_Settlement:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Update, game));
                break;
            case Player_Build_City:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Update, game));
                break;
            case Player_Build_Road:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Update, game));
                break;
            case Player_Move_Robber:
                game.applyAction((GameAction) event.data);
                client.addEvent(new RenderEvent(this, RenderEventType.Game_Update, game));
                break;
            case Player_Roll_Dice:
                game.applyAction((GameAction) event.data);
                //TODO: start dice animation
                break;
            case Player_Offer_Trade:
                break;
            case Player_Accept_Trade:
                //TODO: finish player trading.
                break;
            case Player_Make_Trade:
                game.applyAction((GameAction) event.data);
                break;
        }
    }

    public String toString() {
        return client + "GameThread";
    }
}
