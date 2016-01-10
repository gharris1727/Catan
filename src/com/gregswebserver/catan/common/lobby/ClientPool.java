package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.crypto.Username;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Greg on 12/29/2014.
 * Pool of clients
 */
public class ClientPool extends EventPayload implements EventConsumer<ControlEvent>, Iterable<Username> {

    private final HashMap<Username, UserInfo> clients; //Maps identities to the server clients.
    private final HashMap<Username, Lobby> lobbies; //Maps Users to their lobbies.
    private transient final HashMap<Username, Integer> connectionIDs;
    private transient final HashMap<Integer, Username> connectedUsers;
    private transient CoreThread host;

    public ClientPool(CoreThread host) {
        this.clients = new HashMap<>();
        this.lobbies = new HashMap<>();
        this.connectionIDs = new HashMap<>();
        this.connectedUsers = new HashMap<>();
        this.host = host;
    }

    public void setHost(CoreThread host) {
        this.host = host;
    }

    public Iterator<Username> iterator() {
        return clients.keySet().iterator();
    }

    public boolean test(ControlEvent event) {
        Username origin = event.getOrigin();
        boolean exists = clients.containsKey(origin);
        boolean inLobby = exists && lobbies.containsKey(origin);
        switch (event.getType()) {
            case User_Connect:
                return !exists;
            case User_Disconnect:
            case Name_Change:
            case Pass_Change:
            case Pass_Change_Success:
            case Pass_Change_Failure:
            case Client_Disconnect:
            case Client_Pool_Sync:
                return exists;
            case Server_Disconnect:
                break;
            case Lobby_Create:
                return exists && !inLobby;
            case Lobby_Change_Config:
            case Lobby_Change_Owner:
            case Lobby_Delete:
                //Must be owner of lobby.
                return inLobby && lobbies.get(origin).getOwner().equals(origin);
            case Lobby_Join:
                //Lobby in payload must exist.
                return exists && !inLobby && lobbies.containsKey(event.getPayload());
            case Lobby_Leave:
                return inLobby;
            case Game_Start:
                break;
            case Game_Quit:
                break;
            case Game_End:
                break;
            case Game_Replay:
                break;
            case Replay_Start:
                break;
            case Replay_Quit:
                break;
            case Spectate_Start:
                break;
            case Spectate_Quit:
                break;
        }
        return false;
    }

    public void execute(ControlEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        UserInfo userInfo;
        Username username;
        Lobby lobby;
        switch (event.getType()) {
            case Client_Disconnect:
                break;
            case User_Disconnect:
                username = (Username) event.getPayload();
                clients.remove(username);
                if (lobbies.containsKey(username))
                    host.addEvent(new ControlEvent(username, ControlEventType.Lobby_Delete,null));
                break;
            case User_Connect:
            case Name_Change:
                userInfo = (UserInfo) event.getPayload();
                clients.put(origin, userInfo);
                break;
            case Pass_Change:
                break;
            case Pass_Change_Success:
                break;
            case Pass_Change_Failure:
                break;
            case Server_Disconnect:
                break;
            case Client_Pool_Sync:
                break;
            case Lobby_Create:
                lobby = new Lobby(origin);
                lobbies.put(origin, lobby);
                break;
            case Lobby_Change_Config:
                lobby = lobbies.get(event.getOrigin());
                lobby.setConfig((LobbyConfig) event.getPayload());
                break;
            case Lobby_Change_Owner:
                lobby = lobbies.get(event.getOrigin());
                lobby.setOwner((Username) event.getPayload());
                break;
            case Lobby_Delete:
                lobby = lobbies.get(event.getOrigin());
                for (Username user : lobby) {
                    host.addEvent(new ControlEvent(event.getOrigin(),ControlEventType.Lobby_Leave,user));
                }
                break;
            case Lobby_Join:
                username = (Username) event.getPayload();
                lobby = lobbies.get(username);
                lobby.add(origin);
                lobbies.put(origin, lobby);
                break;
            case Lobby_Leave:
                lobby = lobbies.get(event.getOrigin());
                lobby.remove(event.getOrigin());
                if (lobby.isEmpty())
                    lobbies.remove(event.getOrigin());
                break;
            case Game_Start:
                break;
            case Game_Quit:
                break;
            case Game_End:
                break;
            case Game_Replay:
                break;
            case Replay_Start:
                break;
            case Replay_Quit:
                break;
            case Spectate_Start:
                break;
            case Spectate_Quit:
                break;
        }
    }

    public Lobby getLobby(Username username) {
        return lobbies.get(username);
    }

    public void storeUserConnection(Username username, int connectionID) {
        connectionIDs.put(username,connectionID);
        connectedUsers.put(connectionID,username);
    }

    public Username getConnectionUsername(int id) {
        return connectedUsers.get(id);
    }

    public int getConnectionID(Username username) {
        if (connectionIDs.containsKey(username))
            return connectionIDs.get(username);
        return 0;
    }

    public void removeUserConnection(Username username) {
        if (connectionIDs.containsKey(username))
            connectedUsers.remove(connectionIDs.remove(username));
    }

    public void removeUserConnection(int connectionID) {
        if (connectedUsers.containsKey(connectionID))
            connectionIDs.remove(connectedUsers.remove(connectionID));
    }

    public String toString() {
        return "ClientPool";
    }
}
