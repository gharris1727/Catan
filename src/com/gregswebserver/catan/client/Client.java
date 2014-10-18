package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.graphics.ScreenArea;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.chat.ChatEvent;
import com.gregswebserver.catan.common.chat.ChatThread;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.network.ClientConnection;
import com.gregswebserver.catan.server.event.ControlEvent;

import java.awt.*;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends QueuedInputThread<GenericEvent> {

    private ClientWindow window;
    private InputListener listener;
    //TODO: fully implement client states.
    private ClientState state;
    //Other sub-QueuedInputThreads
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
        chatThread = new ChatThread(logger);
        gameThread = new GameThread(this);
        renderThread = new RenderThread(this);

        state = ClientState.Disconnected;
        start();
    }


    public void execute() throws ThreadStop {
        GenericEvent event = getEvent(true);
        if (event instanceof ExternalEvent) {
            if (event instanceof ChatEvent) {
                chatThread.addEvent((ChatEvent) event);
            }
            if (event instanceof GameEvent) {
                gameThread.addEvent((GameEvent) event);
            }
            if (event instanceof ControlEvent) {
                switch (((ControlEvent) event).getType()) {
                    case Lobby_Create:
                        break;
                    case Lobby_Delete:
                        break;
                    case Lobby_Join:
                        break;
                    case Lobby_Leave:
                        break;
                    case Lobby_Update:
                        break;
                    case Game_Start:
                        state = ClientState.Starting;
                        gameThread.init((GameType) event.getPayload());
                        gameThread.start();
                        renderThread.start(); //TEMP
                        chatThread.start(); //TEMP
                        break;
                    case Game_Quit:
                        state = ClientState.Quitting;
                        break;
                    case Game_End:
                        state = ClientState.Finishing;
                        break;
                    case Game_Replay:
                        state = ClientState.Joining;
                        break;
                    case Replay_Start:
                        state = ClientState.Spectating;
                        break;
                    case Replay_Quit:
                        state = ClientState.Quitting;
                        break;
                    case Spectate_Start:
                        state = ClientState.Spectating;
                        break;
                    case Spectate_Quit:
                        state = ClientState.Quitting;
                        break;
                }
            }
        }
        if (event instanceof InternalEvent) {
            if (event instanceof RenderEvent) {
                renderThread.addEvent((RenderEvent) event);
            }
            if (event instanceof ClientEvent) {
                switch (((ClientEvent) event).getType()) {
                    case Quit_All:
                        shutdown();
                        break;
                    case Net_Connect:
                        state = ClientState.Connecting;
                        connection = new ClientConnection(this, (ConnectionInfo) event.getPayload());
                        break;
                    case Net_Connected:
                        state = ClientState.Connected;
                        break;
                    case Net_Disconnect:
                        state = ClientState.Disconnecting;
                        connection.disconnect();
                        break;
                    case Net_Disconnected:
                        state = ClientState.Disconnected;
                        break;
                    case Canvas_Update:
                        //The ClientWindow is not visible until this event happens.
                        window.setCanvas((Canvas) event.getPayload());
                        break;
                    case Hitbox_Update:
                        listener.setHitbox((ScreenArea) event.getPayload());
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
