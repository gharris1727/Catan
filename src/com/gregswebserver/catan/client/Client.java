package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.event.RenderEventType;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.chat.ChatEvent;
import com.gregswebserver.catan.common.chat.ChatThread;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.crypto.ServerLogin;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.lobby.ClientPool;
import com.gregswebserver.catan.common.lobby.LobbyConfig;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.network.ClientConnection;
import com.gregswebserver.catan.common.network.ControlEvent;
import com.gregswebserver.catan.common.network.ControlEventType;
import com.gregswebserver.catan.common.network.Identity;

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
    private Identity identity;
    private ClientPool clientPool;
    private ServerList loginList;

    public Client() {
        super(Main.logger); //TODO: Create a separate logger for the client.

        window = new ClientWindow(this);
        listener = new InputListener(this);
        window.setListener(listener);
        chatThread = new ChatThread(logger);
        gameThread = new GameThread(this);
        renderThread = new RenderThread(this);
        loginList = new ServerList();

        state = ClientState.Disconnected;
        start();
        renderThread.start();
        loginList.add(new ConnectionInfo("localhost", "25000", "Greg", "password"));
        loginList.add(new ConnectionInfo("invalid", "25000", "Jeff", "password1"));
        loginList.add(new ConnectionInfo("localhost", "NaN", "Brian", "password2"));
        loginList.add(new ConnectionInfo("localhost", "25000", "", "password3"));
        loginList.add(new ConnectionInfo("localhost", "25000", "Greg", "incorrect"));
        renderThread.addEvent(new RenderEvent(this, RenderEventType.ConnectionList_Create, loginList));
    }


    public void execute() throws ThreadStop {
        GenericEvent event = getEvent(true);
        logger.log("Client received event: " + event, LogLevel.DEBUG);
        if (event instanceof ExternalEvent) {
            if (event instanceof ChatEvent)
                chatThread.addEvent((ChatEvent) event);
            if (event instanceof GameEvent)
                gameThread.addEvent((GameEvent) event);
            if (event instanceof ControlEvent)
                controlEvent((ControlEvent) event);
        }
        if (event instanceof InternalEvent) {
            if (event instanceof RenderEvent)
                renderThread.addEvent((RenderEvent) event);
            if (event instanceof ClientEvent)
                clientEvent((ClientEvent) event);
            if (event instanceof UserEvent)
                userEvent((UserEvent) event);
        }

    }

    private void clientEvent(ClientEvent event) {
        switch (event.getType()) {
            case Quit_All:
                shutdown();
                break;
            case Canvas_Update:
                window.setCanvas((Canvas) event.getPayload());
                break;
            case Hitbox_Update:
                listener.setHitbox((ScreenObject) event.getPayload());
                break;
        }
    }

    private void userEvent(UserEvent event) {
        ExternalEvent outgoing;
        switch (event.getType()) {
            case Net_Connect:
                //Connect to remote server
                state = ClientState.Connecting;
                ServerLogin serverLogin = (ServerLogin) event.getPayload();
                identity = serverLogin.login.identity;
                connection = new ClientConnection(this, serverLogin);
                connection.connect();
                break;
            case Net_Disconnect:
                state = ClientState.Disconnecting;
                disconnect("Quitting");
                break;
            case Lobby_Create:
                outgoing = new ControlEvent(identity, ControlEventType.Lobby_Create, new LobbyConfig(identity));
                connection.sendEvent(outgoing);
                break;
            case Lobby_Join:
                outgoing = new ControlEvent(identity, ControlEventType.Lobby_Join, event.getPayload());
                connection.sendEvent(outgoing);
                break;
            case Lobby_Leave:
                outgoing = new ControlEvent(identity, ControlEventType.Lobby_Leave, null);
                connection.sendEvent(outgoing);
                break;
            case Lobby_Modify:
                outgoing = new ControlEvent(identity, ControlEventType.Lobby_Change_Config, event.getPayload());
                connection.sendEvent(outgoing);
                break;
            case Lobby_Start:
                outgoing = new ControlEvent(identity, ControlEventType.Game_Start, null);
                connection.sendEvent(outgoing);
                break;
            case Map_Drag:
                renderThread.addEvent(new RenderEvent(this, RenderEventType.Game_Scroll, event.getPayload()));
                break;
            case Tile_Clicked:
                break;
            case Edge_Clicked:
                break;
            case Vertex_Clicked:
                break;
            case Inventory_Clicked:
                break;
        }
    }

    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Handshake_Client_Connect:
                //Should never get here, this event should die in the server.
                break;
            case Handshake_Client_Connect_Success:
                state = ClientState.Connected;
                clientPool = (ClientPool) event.getPayload();
                logger.log("Connected to remote server", LogLevel.DEBUG);
                break;
            case Handshake_Client_Connect_Failure:
                state = ClientState.Disconnected;
                logger.log("Unable to connect to remote server: " + event.getPayload(), LogLevel.DEBUG);
                break;
            case Server_Disconnect:
                state = ClientState.Disconnected;
                logger.log("Disconnected from remote server: " + event.getPayload(), LogLevel.DEBUG);
                break;
            case Pass_Change:
                //Should never get here, this event should die in the server.
                break;
            case Pass_Change_Success:
                break;
            case Pass_Change_Failure:
                break;
            case Name_Change:
            case Client_Connect:
            case Client_Disconnect:
            case Lobby_Create:
            case Lobby_Change_Config:
            case Lobby_Delete:
            case Lobby_Join:
            case Lobby_Leave:
                try {
                    if (clientPool.test(event))
                        clientPool.execute(event);
                } catch (EventConsumerException e) {
                    logger.log(e, LogLevel.ERROR);
                }
                break;
            case Game_Start:
                state = ClientState.Starting;
                //TODO: create a new game.
                gameThread.start();
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

    public void disconnect(String reason) {
        if (connection != null && connection.isOpen()) {
            connection.sendEvent(new ControlEvent(identity, ControlEventType.Client_Disconnect, reason));
            connection.disconnect();
        }
    }

    public void shutdown() {
        //Shut down the client and all running threads.
        disconnect("Game Quitting");
        chatThread.stop();
        gameThread.stop();
        renderThread.stop();
        stop();
    }

    public Identity getIdentity() {
        return identity;
    }

    public String toString() {
        return "Client";
    }

    public ClientState getState() {
        return state;
    }
}
