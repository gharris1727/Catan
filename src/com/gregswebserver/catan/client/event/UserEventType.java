package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.client.structure.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.structure.LobbyConfig;
import com.gregswebserver.catan.common.structure.LobbySortOption;

/**
 * Created by Greg on 1/4/2015.
 * The types of events that can be sent as InputEvents.
 */
public enum UserEventType implements EventType {

    Net_Connect(ConnectionInfo.class), //Begin connecting to a remote server
    Net_Disconnect(null), //Disconnects from a remote server.
    Net_Clear(null), //Clears the connection error message.
    Lobby_Create(null),
    Lobby_Join(Username.class),
    Lobby_Quit(null),
    Lobby_Edit(LobbyConfig.class),
    Lobby_Start(null),
    Lobby_Sort(LobbySortOption.class),
    Tile_Clicked(Coordinate.class),
    Edge_Clicked(Coordinate.class),
    Vertex_Clicked(Coordinate.class),
    Inventory_Clicked(Integer.class),
    Server_Clicked(Integer.class);

    private final Class payloadType;

    UserEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
