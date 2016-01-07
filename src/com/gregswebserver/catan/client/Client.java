package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.*;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.chat.ChatEvent;
import com.gregswebserver.catan.common.chat.ChatThread;
import com.gregswebserver.catan.common.crypto.AuthToken;
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
import com.gregswebserver.catan.common.crypto.Username;

import java.net.UnknownHostException;

import static com.gregswebserver.catan.client.state.ClientState.*;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends CoreThread {

    private ClientWindow window;
    private InputListener listener;
    private ClientState state;
    private ChatThread chatThread;
    private GameThread gameThread;
    private RenderThread renderThread;
    private ClientConnection connection;
    private Username username;
    private ClientPool clientPool;

    public Client() {
        super(Main.logger); //TODO: Create a separate logger for the client.
        state = Disconnected;
        addEvent(new ClientEvent(this, ClientEventType.Startup, null));
        start();
    }

    protected void externalEvent(ExternalEvent event) {
        if (event instanceof ChatEvent && chatThread != null)
            chatThread.addEvent((ChatEvent) event);
        else if (event instanceof GameEvent && gameThread != null)
            gameThread.addEvent((GameEvent) event);
        else if (event instanceof ControlEvent)
            controlEvent((ControlEvent) event);
        else
            logger.log("Received invalid external event", LogLevel.WARN);

    }

    protected void internalEvent(InternalEvent event) {
        if (event instanceof RenderEvent && renderThread != null)
            renderThread.addEvent((RenderEvent) event);
        else if (event instanceof ClientEvent)
            clientEvent((ClientEvent) event);
        else if (event instanceof UserEvent)
            userEvent((UserEvent) event);
        else
            logger.log("Received invalid internal event", LogLevel.WARN);

    }

    private void clientEvent(ClientEvent event) {
        switch (event.getType()) {
            case Startup:
                startup();
                //TODO: remove this testing data.
                ServerList loginList = new ServerList();
                loginList.add(new ConnectionInfo("localhost", "25000", "Greg", "incorrect"));
                loginList.add(new ConnectionInfo("invalid", "25000", "Jeff", "password1"));
                loginList.add(new ConnectionInfo("localhost", "NaN", "Brian", "password2"));
                loginList.add(new ConnectionInfo("localhost", "25000", "", "password3"));
                loginList.add(new ConnectionInfo("localhost", "25000", "Greg", "password"));
                renderThread.addEvent(new RenderEvent(this, RenderEventType.ConnectionListCreate, loginList));
                break;
            case Quit_All:
                shutdown();
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
                state = Connecting;
                addEvent(new RenderEvent(this, RenderEventType.ConnectProgress, 0));
                ConnectionInfo info = (ConnectionInfo) event.getPayload();
                try {
                    ServerLogin login = info.createServerLogin();
                    username = login.login.username;
                    connection = new ClientConnection(this, login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    addEvent(new RenderEvent(this, RenderEventType.DisconnectMessage, e.getMessage()));
                    state = Disconnecting;
                }
                break;
            case Net_Disconnect:
                state = Disconnecting;
                disconnect("Quitting");
                state = Disconnected;
                break;
            case Lobby_Create:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Create, new LobbyConfig(username));
                sendEvent(outgoing);
                break;
            case Lobby_Join:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Join, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Leave:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Leave, null);
                sendEvent(outgoing);
                break;
            case Lobby_Modify:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Change_Config, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Start:
                outgoing = new ControlEvent(username, ControlEventType.Game_Start, null);
                sendEvent(outgoing);
                break;
            case Tile_Clicked:
                break;
            case Edge_Clicked:
                break;
            case Vertex_Clicked:
                break;
            case Inventory_Clicked:
                break;
            case Server_Clicked:
                break;
        }
    }

    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Server_Disconnect:
                state = Disconnected;
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
            case Lobby_Change_Owner:
                break;
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
                state = Starting;
                //TODO: create a new game.
                gameThread.start();
                break;
            case Game_Quit:
                state = Quitting;
                break;
            case Game_End:
                state = Finishing;
                break;
            case Game_Replay:
                state = Joining;
                break;
            case Replay_Start:
                state = Spectating;
                break;
            case Replay_Quit:
                state = Quitting;
                break;
            case Spectate_Start:
                state = Spectating;
                break;
            case Spectate_Quit:
                state = Quitting;
                break;
        }
    }

    public void netEvent(NetEvent event) {
        ClientConnection connection = (ClientConnection) event.getConnection();
        switch (event.getType()) {
            case Authenticate:
                break;
            case AuthenticationSuccess:
                state = Connected;
                token = (AuthToken) event.getPayload();
                //TODO: move this somewhere else
                //addEvent(new RenderEvent(this, RenderEventType.LobbyListUpdate, clientPool));
                logger.log("Connected to remote server", LogLevel.DEBUG);
                break;
            case AuthenticationFailure:
            case Disconnect:
            case Error:
                state = Disconnecting;
                addEvent(new RenderEvent(this, RenderEventType.DisconnectMessage, event.getPayload()));
                logger.log("Unable to connect to remote server: " + event.getPayload(), LogLevel.DEBUG);
                break;
            case External:
                break;
        }
    }

    private void startup() {
        window = new ClientWindow(this);
        listener = new InputListener(this);
        window.setListener(listener);
        renderThread = new RenderThread(this);
        renderThread.start();
    }

    public void shutdown() {
        //Shut down the client and all running threads.
        disconnect("Quitting");
        if (renderThread != null && renderThread.isRunning())
            renderThread.stop();
//        if (chatThread != null && chatThread.isRunning())
//            chatThread.stop();
        if (gameThread != null && gameThread.isRunning())
            gameThread.stop();
        stop();
    }

    public void disconnect(String reason) {
        if (connection != null && connection.isOpen()) {
            connection.sendEvent(new NetEvent(token, NetEventType.Disconnect, reason));
            connection.disconnect();
        }
    }

    public void sendEvent(ExternalEvent event) {
        if (connection != null && connection.isOpen()) {
            NetEvent out = new NetEvent(token, NetEventType.External, event);
            connection.sendEvent(out);
        }
    }

    public String toString() {
        return "Client";
    }

    public ClientState getState() {
        return state;
    }
}
