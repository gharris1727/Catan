package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.graphics.HexagonalMask;
import com.gregswebserver.catan.graphics.TriangularMask;

/**
 * Created by Greg on 8/14/2014.
 * Hitbox for referencing
 */
public class GameHitbox implements ScreenHitbox {

    private int dw, dh;

    public GameHitbox(GameBoard board) {
        HexagonalMask hexMask = new HexagonalMask(56, 48);
        dw = 28;
        dh = 16;
        TriangularMask triMask = new TriangularMask(16);
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
