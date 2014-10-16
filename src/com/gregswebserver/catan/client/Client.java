package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.chat.ChatThread;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.client.game.GameThread;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.event.*;
import com.gregswebserver.catan.network.ClientConnection;
import com.gregswebserver.catan.network.Identity;
import com.gregswebserver.catan.server.ServerEvent;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends QueuedInputThread {

    private ClientWindow window;
    private InputListener listener;
    private ChatThread chatThread;
    private GameThread gameThread;
    private RenderThread renderThread;
    private ClientConnection connection;

    public Client() {
        super(Main.logger); //TODO: REMOVE ME!
//        super(new Logger());

        window = new ClientWindow(this);
        listener = new InputListener(this);
        window.setListener(listener);
        chatThread = new ChatThread(this);
        gameThread = new GameThread(this);
        renderThread = new RenderThread(this);

        start();
        chatThread.start();
        gameThread.start();
        renderThread.start();
    }


    public void execute() throws ThreadStop {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof ExternalEvent) {
            if (event instanceof ChatEvent) {
                chatThread.addEvent(event);
            }
            if (event instanceof GameEvent) {
                gameThread.addEvent(event);
            }
            if (event instanceof ServerEvent) {
                ServerEvent sEvent = (ServerEvent) event;
                switch (sEvent.getType()) {
                    case Client_Connect:
                    case Client_Disconnect:
                        break;
                    case Lobby_Create:
                        break;
                    case Lobby_Delete:
                        break;
                    case Lobby_Join:
                        //addPlayer((Identity) event.getPayload());
                        break;
                    case Lobby_Leave:
                        //removePlayer((Identity) event.getPayload());
                        break;
                    case Lobby_Update:
                        break;
                    case Game_Start:
                        //gameThread.createNewGame((GameType) event.getPayload());
                        gameThread.start();
                        break;
                    case Game_End:
                        gameThread.stop();
                        break;
                }
            }
        }
        if (event instanceof InternalEvent) {
            if (event instanceof RenderEvent) {
                renderThread.addEvent(event);
            }
            if (event instanceof ClientEvent) {
                ClientEvent cEvent = (ClientEvent) event;
                switch (cEvent.getType()) {
                    case Net_Connect:
                        connection = new ClientConnection(this, (Identity) cEvent.getPayload());
                        connection.connectTo(null);
                        break;
                    case Net_Disconnect:
                        connection.disconnect();
                        break;
                    case Canvas_Update:
                        //The ClientWindow is not visible until this event happens.
                        window.setCanvas((Canvas) ((ClientEvent) event).getPayload());
                        break;
                    case Hitbox_Update:
                        listener.setHitbox((ScreenArea) ((ClientEvent) event).getPayload());
                        break;
                }
            }
        }

    }

    public void shutdown() {
        //Shut down the client and all running threads.
        if (connection != null)
            connection.disconnect();
        chatThread.stop();
        gameThread.stop();
        renderThread.stop();
        stop();
    }

    public String toString() {
        return "Client";
    }

}
