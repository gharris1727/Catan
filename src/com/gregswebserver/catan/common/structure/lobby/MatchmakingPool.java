package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
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
    public EventConsumerProblem test(LobbyEvent event) {
        Username origin = event.getOrigin();
        boolean exists = clients.hasUser(origin);
        boolean inLobby = exists && lobbies.userInLobby(origin);
        switch (event.getType()) {
            case User_Connect:
                if (exists)
                    return new EventConsumerProblem("User exists");
                break;
            case User_Disconnect:
                if (!exists)
                    return new EventConsumerProblem("User does not exist");
                break;
            case Lobby_Create:
                if (!exists)
                    return new EventConsumerProblem("User does not exist");
                if (inLobby)
                    return new EventConsumerProblem("User is already in a lobby");
                break;
            case Lobby_Join:
                if (!exists)
                    return new EventConsumerProblem("User does not exist");
                if (inLobby)
                    return new EventConsumerProblem("User is already in a lobby");
                if (!lobbies.userInLobby((Username) event.getPayload()))
                    return new EventConsumerProblem("Other user is not in a lobby");
                break;
            case Lobby_Change_Config:
            case Lobby_Leave:
            case Game_Start:
            case Game_Join:
            case Game_Leave:
            case Game_Sync:
            case Game_Finish:
                if (!inLobby)
                    return new EventConsumerProblem("User is not in a lobby");
                break;
        }
        return null;
    }

    @Override
    public void execute(LobbyEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(problem);
        try {
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
                case Game_Start:
                    lobbies.start(origin);
                    break;
                case Game_Join:
                    lobbies.connect(origin);
                    break;
                case Game_Leave:
                    lobbies.disconnect(origin);
                    break;
                case Game_Sync:
                    break;
                case Game_Finish:
                    lobbies.finish(origin);
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
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
