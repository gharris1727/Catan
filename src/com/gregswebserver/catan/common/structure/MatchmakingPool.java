package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.event.LobbyEvent;

/**
 * Created by Greg on 12/29/2014.
 * Pool of clients
 */
public class MatchmakingPool extends EventPayload implements EventConsumer<LobbyEvent>{

    private final ClientPool clients;
    private final LobbyPool lobbies;

    public MatchmakingPool(CoreThread host) {
        this.clients = new ClientPool();
        this.lobbies = new LobbyPool();
    }

    @Override
    public boolean test(LobbyEvent event) {
        Username origin = event.getOrigin();
        boolean exists = clients.hasUser(origin);
        boolean inLobby = exists && lobbies.userInLobby(origin);
        switch (event.getType()) {
            case User_Connect:
                return !exists;
            case User_Disconnect:
            case Name_Change:
                return exists;
            case Lobby_Create:
                return exists && !inLobby;
            case Lobby_Join:
                //Lobby in payload must exist.
                return exists && !inLobby && lobbies.userInLobby((Username) event.getPayload());
            case Lobby_Change_Config:
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

    @Override
    public void execute(LobbyEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        UserInfo userInfo;
        Username username;
        Lobby lobby;
        switch (event.getType()) {
            case User_Disconnect:
                username = (Username) event.getPayload();
                break;
            case User_Connect:
            case Name_Change:
                userInfo = (UserInfo) event.getPayload();
                clients.updateUserInfo(origin, userInfo);
                break;
            case Lobby_Create:
                lobbies.add(origin, (LobbyConfig) event.getPayload());
                break;
            case Lobby_Change_Config:
                lobbies.changeConfig(origin, (LobbyConfig) event.getPayload());
                break;
            case Lobby_Join:
                lobbies.join(origin, (Username) event.getPayload());
                break;
            case Lobby_Leave:
                lobbies.leave((Username) event.getPayload());
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

    public ClientPool getClientList() {
        return clients;
    }

    public LobbyPool getLobbyList() {
        return lobbies;
    }
}
