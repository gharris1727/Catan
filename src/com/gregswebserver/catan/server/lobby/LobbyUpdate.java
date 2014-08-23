package com.gregswebserver.catan.server.lobby;

import com.gregswebserver.catan.network.Identity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class LobbyUpdate implements Serializable {

    //TODO: finish this.

    private ArrayList<ArrayList<Identity>> data;

    public LobbyUpdate(HashMap<Identity, Lobby> lobbies) {
        data = new ArrayList<>();
        for (Identity owner : lobbies.keySet()) {

        }
    }
}
