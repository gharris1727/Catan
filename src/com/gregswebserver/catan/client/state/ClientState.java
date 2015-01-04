package com.gregswebserver.catan.client.state;

/**
 * Created by Greg on 10/16/2014.
 * Details regarding the active state of the client, concerning stages of connection and disconnection,
 * lobby management, and game states.
 */
public enum ClientState {

    Disconnected(null), //Displays the server connect screen
    Disconnecting(Disconnected), //Disconnection in progress...
    Connected(null), //Displays lobby join screen
    Connecting(Connected), //Connection in progress
    Leaving(Connected),
    Quitting(Connected),
    InLobby(null),
    Joining(InLobby),
    InGame(null),
    Starting(InGame),
    InPostGame(null),
    Finishing(InPostGame),
    Spectating(null);

    private ClientState next;

    ClientState(ClientState next) {
        this.next = next;
    }
}
