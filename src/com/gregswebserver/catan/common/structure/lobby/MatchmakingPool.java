package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.structure.UserInfo;
import com.gregswebserver.catan.common.structure.event.LobbyEvent;

/**
 * Created by Greg on 12/29/2014.
 * Pool of clients
 */
public class MatchmakingPool extends EventPayload implements EventConsumer<LobbyEvent>{

    private final ClientPool clients;
    private final LobbyPool lobbies;

    public MatchmakingPool() {
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
                return exists;
            case Lobby_Create:
                return exists && !inLobby;
            case Lobby_Join:
                return exists && !inLobby && lobbies.userInLobby((Username) event.getPayload());
            case Lobby_Change_Config:
            case Lobby_Leave:
            case Lobby_Start:
            case Lobby_Finish:
                return inLobby;
        }
        return false;
    }

    @Override
    public void execute(LobbyEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        switch (event.getType()) {
            case User_Disconnect:
                lobbies.leave(origin);
                clients.remove(origin);
                break;
            case User_Connect:
                clients.add(origin, (UserInfo) event.getPayload());
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
                lobbies.leave(origin);
                break;
            case Lobby_Start:
                lobbies.start(origin);
                break;
            case Lobby_Finish:
                lobbies.finish(origin);
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
