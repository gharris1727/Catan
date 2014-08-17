package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.board.GameBoard;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/14/2014.
 * Hitbox for referencing
 */
public class ColorHitbox implements Hitbox {

    private Graphic graphic;
    private HashMap<Color, Object> map;

    public ColorHitbox(Graphic graphic) {
        this.graphic = graphic;
    }

    public void init(GameBoard board) {

    }

    public Object getObject(Point p) {
        return null;
    }
}
