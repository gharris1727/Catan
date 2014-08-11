package com.gregswebserver.catan.config;

/**
 * Created by Greg on 8/10/2014.
 * Text tags that are matched by the custom parser
 */
public enum ParseTag {

    Comment("<Comment>"),
    Tile("<Tile>"),
    Name("<Name>"),
    Size("<Size>"),
    Trade("<Trade>"),
    Generator("<Gen>"),
    Players("<Players>");

    private String text;

    ParseTag(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
