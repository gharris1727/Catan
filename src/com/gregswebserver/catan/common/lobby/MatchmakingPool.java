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
public class MatchmakingPool extends EventPayload implements EventConsumer<ControlEvent>{

    private final ClientPool clients;
    private final LobbyPool lobbies;
    private transient CoreThread host;

    public MatchmakingPool(CoreThread host) {
        this.clients = new ClientPool();
        this.lobbies = new LobbyPool();
        this.host = host;
    }

    public void setHost(CoreThread host) {
        this.host = host;
    }

    public boolean test(ControlEvent event) {
        Username origin = event.getOrigin();
        boolean exists = clients.hasUser(origin);
        boolean inLobby = exists && lobbies.userInLobby(origin);
        switch (event.getType()) {
            case Client_Disconnect:
            case Client_Pool_Sync:
            case Server_Disconnect:
            case Pass_Change_Success:
            case Pass_Change_Failure:
            case Pass_Change:
                throw new IllegalStateException();
            case User_Connect:
                return !exists;
            case User_Disconnect:
            case Name_Change:
                return exists;
            case Lobby_Create:
                return exists && !inLobby;
            case Lobby_Change_Config:
            case Lobby_Change_Owner:
            case Lobby_Delete:
                //Must be owner of lobby.
                return inLobby && lobbies.userGetLobby(origin).getOwner().equals(origin);
            case Lobby_Join:
                //Lobby in payload must exist.
                return exists && !inLobby && lobbies.userInLobby((Username) event.getPayload());
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
                clients.removeUserConnection(username);
                if (lobbies.userInLobby(username))
                    host.addEvent(new ControlEvent(username, ControlEventType.Lobby_Delete,null));
                break;
            case User_Connect:
            case Name_Change:
                userInfo = (UserInfo) event.getPayload();
                clients.updateUserInfo(origin, userInfo);
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
                lobbies.add(origin, (LobbyConfig) event.getPayload());
                break;
            case Lobby_Change_Config:
                lobbies.changeConfig(origin, (LobbyConfig) event.getPayload());
                break;
            case Lobby_Change_Owner:
                lobbies.changeOwner(origin, (Username) event.getPayload());
                break;
            case Lobby_Delete:
                lobby = lobbies.userGetLobby(origin);
                for (Username user : lobby)
                    host.addEvent(new ControlEvent(event.getOrigin(),ControlEventType.Lobby_Leave,user));
                break;
            case Lobby_Join:
                lobbies.join(origin, (Username) event.getPayload());
                break;
            case Lobby_Leave:
                lobbies.leave(origin);
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

    public String toString() {
        return "MatchmakingPool";
    }

    public ClientPool getUserList() {
        return clients;
    }

    public LobbyPool getLobbyList() {
        return lobbies;
    }
}
