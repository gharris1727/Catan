package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.chat.ChatThread;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.client.game.GameThread;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.event.*;
import com.gregswebserver.catan.network.ClientConnection;
import com.gregswebserver.catan.network.NetID;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends QueuedInputThread {

    private Client instance;
    private ClientWindow window;
    private ClientConnection connection;
    private ChatThread chatThread;
    private GameThread gameThread;
    private InputListener listener;
    private RenderThread renderThread;

    public Client(NetID server) {
        super(Main.logger); //TODO: REMOVE ME!
//        super(new Logger());
        instance = this;
        window = new ClientWindow(instance);
        listener = new InputListener(instance);
        window.setListener(listener);
        chatThread = new ChatThread(this);
        gameThread = new GameThread(this);
        renderThread = new RenderThread(logger);
        renderThread.setScreen(window.getScreen());
        listener.setHitbox(renderThread.getHitbox());
        connection = new ClientConnection(this);

        start();
        chatThread.start();
        gameThread.start();
        renderThread.start();
        connection.connectTo(server);
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
        }
        if (event instanceof InternalEvent) {
            if (event instanceof RenderEvent) {
                renderThread.addEvent(event);
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

    public void shutdown() {
        //Shut down the client and all running threads.
        connection.disconnect();
        chatThread.stop();
        gameThread.stop();
        renderThread.stop();
        stop();
    }
}
