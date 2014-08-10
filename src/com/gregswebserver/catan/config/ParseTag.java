package com.gregswebserver.catan.config;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;

/**
 * Created by Greg on 8/10/2014.
 * Text tags that are matched by the custom parser
 */
public enum ParseTag {

    Comment("<Comment>", null),
    Tile("<Tile>", Coordinate.class),
    Name("<Name>", String.class),
    Size("<Size>", Coordinate.class);

    private String text;

    ParseTag(String text, Class expected) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
