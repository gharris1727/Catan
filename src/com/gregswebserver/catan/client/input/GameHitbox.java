package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.game.board.GameBoard;

/**
 * Created by Greg on 8/14/2014.
 * Hitbox for referencing
 */
public class GameHitbox implements ScreenHitbox {

    //TODO: implement me.

    private int[] cols = {19, 32};
    private int[] rows = {8, 20};

    public GameHitbox(GameBoard board) {

    }

    public void setGlobalSize(int x, int y) {

    }

    public void setViewSize(int x, int y) {

    }

    public void setViewLocation(int x, int y) {

    }

    public void setScreenLocation(int x, int y) {

    }

    public Clickable getObjectAtPoint(int x, int y) {
        return null;
    }
}
