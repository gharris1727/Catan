package com.gregswebserver.catan.common.structure.event;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.structure.UserInfo;
import com.gregswebserver.catan.common.structure.game.GameProgress;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.lobby.LobbyConfig;

/**
 * Created by greg on 1/27/16.
 * The payload type for a lobby event.
 */
public enum LobbyEventType implements EventType {

    User_Connect(UserInfo.class), //Server -> Broadcast, when a user logs in.
    User_Disconnect(null), //Client -> Server -> Broadcast, when a user logs out.
    Lobby_Create(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is created.
    Lobby_Change_Config(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is modified.
    Lobby_Join(Username.class), //Client -> Server -> Broadcast, when a client joins a lobby, stores owner of lobby.
    Lobby_Leave(null), //Client -> Server -> Broadcast, when a client leaves a lobby.
    Game_Start(GameSettings.class), //Client -> Server -> Broadcast when a game starts.
    Game_Join(null), //(Client ->) Server -> Broadcast when a player joins a game in progress.
    Game_Leave(null), //Client -> Server -> Broadcast when a player leaves a game in progress
    Game_Sync(GameProgress.class), //Server -> Client when a player joins a game and needs a synced list of GameEvents
    Game_Finish(null); //Server -> Client when a game finishes and will not be accepting further changes.

    private final Class payloadType;

    LobbyEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
