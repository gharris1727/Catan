package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.structure.ConnectionInfo;
import com.gregswebserver.catan.client.ui.PopupWindow;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.structure.lobby.LobbyConfig;

/**
 * Created by Greg on 1/4/2015.
 * The types of events that can be sent as InputEvents.
 */
public enum UserEventType implements EventType {

    Shutdown(null), //Command to shut down the client.
    Composite_Event(UserEvent[].class), //A UserEvent that contains multiple other UserEvents.
    Display_Popup(PopupWindow.class), //Display a popup on the screen.
    Expire_Popup(PopupWindow.class), //Remove a popup from the screen.
    Server_Remove(ConnectionInfo.class), //Remove a server from the server list.
    Server_Add(ConnectionInfo.class), //Add a server to the server list.
    Register_Account(ConnectionInfo.class), //Register an account on the remote server.
    Net_Connect(ConnectionInfo.class), //Begin connecting to a remote server
    Net_Disconnect(null), //Disconnects from a remote server.
    Net_Clear(null), //Clears the connection error message.
    Lobby_Create(null), //Create a new lobby.
    Lobby_Join(Username.class), //Join an existing lobby.
    Lobby_Quit(null), //Quit from the active lobby.
    Lobby_Edit(LobbyConfig.class), //Edit the current lobby's settings.
    Lobby_Start(null), //Start the game from the lobby.
    Tile_Rob(Coordinate.class), //Place the robber on a tile.
    End_Turn(null), //End the player's turn.
    Road_Purchase(Coordinate.class), //Purchase a road in the board.
    Settlement_Purchase(Coordinate.class), //Purchase a settlement.
    City_Purchase(Coordinate.class), //Purchase a city.
    Make_Trade(Trade.class), //Complete a trade.
    History_Jump(Integer.class), //Jump to that historical event.
    Propose_Trade(TemporaryTrade.class); //advertise a temporary trade to other players.

    private final Class payloadType;

    UserEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
