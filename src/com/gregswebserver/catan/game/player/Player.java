package com.gregswebserver.catan.game.player;

import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player {

    private Identity identity;
    private Team team;

    public Player(Identity identity) {
        this.identity = identity;
        //TODO: store user data here.
    }

    public Identity getIdentity() {
        return identity;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
