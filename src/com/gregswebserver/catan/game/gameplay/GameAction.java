package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.game.player.Player;

/**
 * Created by Greg on 8/9/2014.
 * A generic action taken in the game, stored in a way that it can be undone.
 */
public class GameAction {

    public final Player player;
    public final GameActionType type;
    public final Object data;

    public GameAction(Player player, GameActionType type, Object data) {
        this.player = player;
        this.type = type;
        this.data = data;
    }
}
