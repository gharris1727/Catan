package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.gameplay.trade.Trader;
import com.gregswebserver.catan.common.lobby.ServerClient;
import com.gregswebserver.catan.common.network.Identity;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Trader {

    private final Identity identity;
    private final String name;
    private final Team team;
    private HashMap<Tradeable, Integer> inventory;
    private Coordinate selected;

    public Player(ServerClient client, Team team) {
        this.identity = client.getIdentity();
        this.name = client.getDisplayName();
        this.team = team;
    }

    public Identity getIdentity() {
        return identity;
    }

    public Team getTeam() {
        return team;
    }

    public boolean canOffer(Trade t) {
        return false;
    }

    public boolean canFillRequest(Trade t) {
        return false;
    }

    public HashSet<Trade> getTrades() {
        return null;
    }

    public HashMap<Tradeable, Integer> getInventory() {
        return inventory;
    }

    public Coordinate getSelected() {
        return selected;
    }

    public void setSelected(Coordinate selected) {
        this.selected = selected;
    }
}
