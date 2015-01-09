package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.gameplay.trade.Trader;
import com.gregswebserver.catan.common.lobby.ServerClient;

import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Trader {

    private final String name;
    private final Team team;
    private HashMap<Tradeable, Integer> inventory;
    private Coordinate selected;

    public Player(ServerClient client, Team team) {
        this.name = client.getDisplayName();
        this.team = team;
    }


    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
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
