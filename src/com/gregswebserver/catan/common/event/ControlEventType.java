package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.Password;
import com.gregswebserver.catan.common.lobby.MatchmakingPool;
import com.gregswebserver.catan.common.lobby.LobbyConfig;
import com.gregswebserver.catan.common.lobby.UserInfo;
import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by Greg on 10/16/2014.
 * Type determining the purpose of a lobby event that controls repeating data between clients.
 */
public enum ControlEventType implements EventType {

    User_Connect(UserInfo.class), //Server -> Broadcast, when a user logs in.
    User_Disconnect(null), //Client -> Server -> Broadcast, when a user logs out.
    Name_Change(String.class), //Client -> Server -> Broadcast, contains a name change
    Pass_Change(Password.class), //Client -> Server, when the client requests a password change.
    Pass_Change_Success(null), //Server -> Client, when a password change succeeds.
    Pass_Change_Failure(null), //Server -> Client, when a password change fails.
    Server_Disconnect(String.class), //Server -> Broadcast, When the server disconnects, String why.
    Client_Disconnect(String.class), //Client -> Server, when a client disconnects, String why.
    Client_Pool_Sync(MatchmakingPool.class), //Server -> Client, when first joining to send the current client pool state.
    Lobby_Create(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is created.
    Lobby_Change_Config(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is modified.
    Lobby_Change_Owner(Username.class), //Client -> Server -> Broadcast, when a lobby's owner is changed.
    Lobby_Delete(null), //Client -> Server -> Broadcast, when a lobby is deleted.
    Lobby_Join(Username.class), //Client -> Server -> Broadcast, when a client joins a lobby, stores owner of lobby.
    Lobby_Leave(null), //Client -> Server -> Broadcast, when a client leaves a lobby.
    Game_Start(null),
    Game_Quit(null),
    Game_End(null),
    Game_Replay(null),
    Replay_Start(null),
    Replay_Quit(null),
    Spectate_Start(Username.class),
    Spectate_Quit(null);

    private Class payloadType;

    ControlEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
