package com.gregswebserver.catan.client;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.renderer.RenderManager;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.client.ui.primary.ConnectionInfo;
import com.gregswebserver.catan.client.ui.primary.ServerLogin;
import com.gregswebserver.catan.client.ui.primary.ServerPool;
import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.chat.ChatEvent;
import com.gregswebserver.catan.common.chat.ChatThread;
import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.lobby.*;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.network.ClientConnection;

import java.io.IOException;
import java.net.UnknownHostException;

import static com.gregswebserver.catan.client.ClientState.*;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends CoreThread {

    private ClientWindow window;
    private ClientState state;
    private ChatThread chatThread;
    private GameThread gameThread;
    private RenderThread renderThread;

    private RenderManager manager;
    private InputListener listener;
    private ClientConnection connection;

    private ServerPool serverPool;
    private Username username;
    private MatchmakingPool matchmakingPool;
    private String disconnectReason;

    public Client() {
        super(Main.logger); //TODO: Create a separate logger for the client.
        state = Disconnected;
        addEvent(new ClientEvent(this, ClientEventType.Startup, null));
        start();
    }

    public ServerPool getServerList() {
        return serverPool;
    }

    public ClientPool getClientList() {
        return matchmakingPool.getClientList();
    }

    public LobbyPool getLobbyList() {
        return matchmakingPool.getLobbyList();
    }

    public Lobby getActiveLobby() {
        return matchmakingPool.getLobbyList().userGetLobby(username);
    }

    public String getDisconnectMessage() {
        return disconnectReason;
    }

    public Username getUsername() {
        return username;
    }

    private void updatePool(ControlEvent event) {
        try {
            if (matchmakingPool.test(event))
                matchmakingPool.execute(event);
        } catch (EventConsumerException e) {
            logger.log(e, LogLevel.ERROR);
        }
        if (manager.lobbyJoinMenu != null)
            manager.lobbyJoinMenu.update();
        if (manager.lobbyScreen != null)
            manager.lobbyScreen.update();
    }

    @Override
    protected void externalEvent(ExternalEvent event) {
        if (event instanceof ChatEvent && chatThread != null)
            chatThread.addEvent((ChatEvent) event);
        else if (event instanceof GameEvent && gameThread != null)
            gameThread.addEvent((GameEvent) event);
        else if (event instanceof ControlEvent)
            controlEvent((ControlEvent) event);
        else
            throw new IllegalStateException();

    }

    @Override
    protected void internalEvent(InternalEvent event) {
        if (event instanceof RenderEvent && renderThread != null)
            renderThread.addEvent((RenderEvent) event);
        else if (event instanceof ClientEvent)
            clientEvent((ClientEvent) event);
        else if (event instanceof UserEvent)
            userEvent((UserEvent) event);
        else
            throw new IllegalStateException();

    }

    // Client maintenance events, for startup shutdown etc.
    private void clientEvent(ClientEvent event) {
        switch (event.getType()) {
            case Startup:
                startup();
                break;
            case Quit_All:
                shutdown();
                break;
        }
    }

    // Interpreting events from user input to go to the server.
    private void userEvent(UserEvent event) {
        ExternalEvent outgoing;
        switch (event.getType()) {
            case Net_Connect:
                //Connect to remote server
                state = Connecting;
                manager.displayServerConnectingScreen();
                ConnectionInfo info = (ConnectionInfo) event.getPayload();
                try {
                    ServerLogin login = info.createServerLogin();
                    username = login.login.username;
                    connection = new ClientConnection(this, login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    disconnectReason = e.getMessage();
                    manager.displayServerDisconnectingScreen();
                    state = Disconnecting;
                }
                break;
            case Net_Disconnect:
                state = Disconnecting;
                disconnect("Quitting");
                state = Disconnected;
                manager.displayServerDisconnectingScreen();
                break;
            case Lobby_Create:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Create, new LobbyConfig(username));
                sendEvent(outgoing);
                break;
            case Lobby_Join:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Join, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Quit:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Leave, null);
                sendEvent(outgoing);
                break;
            case Lobby_Edit:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Change_Config, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Make_Leader:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Change_Owner, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Kick:
                outgoing = new ControlEvent(username, ControlEventType.Lobby_Leave, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Start:
                outgoing = new ControlEvent(username, ControlEventType.Game_Start, null);
                sendEvent(outgoing);
                break;
            case Lobby_Sort:
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

    // Control events sent by the server to affect the local client.
    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Server_Disconnect:
                state = Disconnected;
                break;
            case Pass_Change:
                throw new IllegalStateException();
            case Pass_Change_Success:
                break;
            case Pass_Change_Failure:
                break;
            case Client_Pool_Sync:
                matchmakingPool = (MatchmakingPool) event.getPayload();
                matchmakingPool.setHost(this);
                manager.displayLobbyJoinMenu();
                break;
            case User_Disconnect:
            case Name_Change:
            case User_Connect:
            case Client_Disconnect:
            case Lobby_Change_Config:
            case Lobby_Change_Owner:
            case Lobby_Delete:
                updatePool(event);
                break;
            case Lobby_Create:
            case Lobby_Join:
                updatePool(event);
                if (username.equals(event.getOrigin())) {
                    manager.displayInLobbyScreen();
                }
                break;
            case Lobby_Leave:
                updatePool(event);
                if (username.equals(event.getOrigin())) {
                    manager.displayLobbyJoinMenu();
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

    // Manage events coming from the network connection.
    @Override
    public void netEvent(NetEvent event) {
        ClientConnection connection = (ClientConnection) event.getConnection();
        switch (event.getType()) {
            case Log_In:
                break;
            case Log_In_Success:
                state = Connected;
                token = (AuthToken) event.getPayload();
                break;
            case Log_In_Failure:
            case Disconnect:
            case Link_Error:
                state = Disconnecting;
                disconnectReason = (String) event.getPayload();
                manager.displayServerDisconnectingScreen();
                break;
            case External_Event:
                externalEvent((ExternalEvent) event.getPayload());
                break;
        }
    }

    private void startup() {
        manager = new RenderManager(this);
        manager.setStyle(UIStyle.Blue);
        listener = new InputListener(this, manager);
        window = new ClientWindow(this, listener);
        renderThread = new RenderThread(this, manager);
        renderThread.start();
        serverPool = new ServerPool();
        manager.displayServerConnectMenu();
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
        try {
            serverPool.save();
        } catch (IOException e) {
            logger.log("Unable to save server list.", e, LogLevel.WARN);
        }
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
            NetEvent out = new NetEvent(token, NetEventType.External_Event, event);
            connection.sendEvent(out);
        }
    }

    public String toString() {
        return "Client";
    }
}
