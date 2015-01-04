package com.gregswebserver.catan.client.input;

/**
 * Created by Greg on 1/3/2015.
 * Interface for clicking on a character in text.
 */
public interface TextClickable extends Clickable {

    public default void charSelected(int index) {
    }
}
