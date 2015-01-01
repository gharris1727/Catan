package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.ScreenObject;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/23/2014.
 * A button that can be put in a dialog window. Meant to be instantiated with an anonymous class.
 */
public abstract class DialogButton extends ScreenObject implements Clickable {

    public DialogButton(String text, Point position, int priority) {
        super(position, priority);
    }

    public abstract void onRightClick();

    public abstract void onMiddleClick();

    protected abstract void render();

}
