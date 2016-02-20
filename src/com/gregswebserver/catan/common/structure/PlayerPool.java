package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.player.Player;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool implements Serializable{

    private transient Username local;
    private final Map<Username, Player> players;

    public PlayerPool(Username local, Map<Username, Player> players) {
        this.local = local;
        this.players = players;
    }

    public Set<Username> getAllUsers() {
        return players.keySet();
    }

    public Player getPlayer(Username origin) {
        return players.get(origin);
    }

    public void setLocal(Username local) {
        this.local = local;
    }

    public Player getLocalPlayer() {
        return players.get(local);
    }

    @Override
    public String toString() {
        return "PlayerPool(" + local + "/" + players + ")";
    }
}
