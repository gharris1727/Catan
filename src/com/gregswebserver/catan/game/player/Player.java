package com.gregswebserver.catan.game.player;

import com.gregswebserver.catan.game.gameplay.trade.Trade;
import com.gregswebserver.catan.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.game.gameplay.trade.Trader;
import com.gregswebserver.catan.network.Identity;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Trader {

    private Identity identity;
    private Team team;
    private HashMap<Tradeable, Integer> inventory;

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

    @Override
    public boolean canOffer(Trade t) {
        return false;
    }

    @Override
    public boolean canFillRequest(Trade t) {
        return false;
    }

    @Override
    public HashSet<Trade> getTrades() {
        return null;
    }

    public HashMap<Tradeable, Integer> getInventory() {
        return inventory;
    }
}
