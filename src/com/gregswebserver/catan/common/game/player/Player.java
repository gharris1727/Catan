package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.gameplay.trade.Trader;

import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * A player in a game of catan, stores resource accounts, victory points, and can make moves on the catan game.
 */
public class Player implements Trader {

    private final Username name;
    private final Team team;
    private HashMap<Tradeable, Integer> inventory;
    private Coordinate selected;

    public Player(Username name, Team team) {
        this.name = name;
        this.team = team;
    }


    public Username getName() {
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
