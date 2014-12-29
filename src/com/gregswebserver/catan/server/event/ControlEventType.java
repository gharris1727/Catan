package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.crypto.Password;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 10/16/2014.
 * Type determining the purpose of a lobby event that controls repeating data between clients.
 */
public enum ControlEventType implements EventType {

    Client_Connect(UserLogin.class),
    Client_Connected(Integer.class),
    Client_Disconnect(null),
    Client_Disconnected(Integer.class),
    Pass_Change(Password.class),
    Lobby_Create(Identity.class),
    Lobby_Delete(null),
    Lobby_Join(Identity.class),
    Lobby_Leave(null),
    Lobby_Update(GameType.class),
    Game_Start(GameType.class),
    Game_Quit(null),
    Game_End(null),
    Game_Replay(null),
    Replay_Start(null),
    Replay_Quit(null),
    Spectate_Start(Identity.class),
    Spectate_Quit(null);

    private Class payloadType;

    ControlEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }
}
