package catan.client.input;

import catan.client.structure.ConnectionInfo;
import catan.common.crypto.Username;
import catan.common.event.EventType;
import catan.common.structure.lobby.LobbyConfig;

/**
 * Created by Greg on 1/4/2015.
 * The types of events that can be sent as InputEvents.
 */
public enum UserEventType implements EventType {

    Shutdown(null), //Command to shut down the client.
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
    History_Jump(Integer.class), //Jump to that historical event.
    Linger_Trigger(Number.class); //Trigger a delay to call the onLinger method.

    private final Class payloadType;

    UserEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
