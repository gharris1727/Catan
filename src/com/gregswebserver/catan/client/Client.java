package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.chat.ChatThread;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.client.game.GameThread;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.InternalEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.network.ClientConnection;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends QueuedInputThread<GenericEvent> {

    private Client instance;
    private ClientWindow window;
    private ClientConnection connection;
    private ChatThread chatThread;
    private GameThread gameThread;
    private InputListener listener;
    private RenderThread renderThread;

    public Client() {
        super(new Logger());
        instance = this;
        window = new ClientWindow(instance);
        listener = new InputListener(instance);
        window.setListener(listener);
        chatThread = new ChatThread(logger);
        gameThread = new GameThread(logger);
        renderThread = new RenderThread(logger);
        listener.setScreenHitbox(renderThread.getScreenHitbox());
        connection = new ClientConnection(this);
    }


    public void execute() {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof ExternalEvent) {
            if (event instanceof ChatEvent) {
                chatThread.addEvent((ChatEvent) event);
            }
            if (event instanceof GameEvent) {
                gameThread.addEvent((GameEvent) event);
            }
        }
        if (event instanceof InternalEvent) {
            if (event instanceof RenderEvent) {
                renderThread.addEvent((RenderEvent) event);
            }
            if (event instanceof ClientEvent) {
                ClientEvent clientEvent = (ClientEvent) event;
                switch (clientEvent.type) {
                    case Net_Connect:
                        connection.connect();
                        break;
                    case Net_Disconnect:
                        connection.disconnect();
                        break;
                    case Net_Join:
                        break;
                    case Net_Leave:
                        //TODO: handle server joins/leaves
                        break;
                }
            }
        }

    }
}
