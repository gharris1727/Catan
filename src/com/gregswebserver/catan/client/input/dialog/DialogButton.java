package com.gregswebserver.catan.client.input.dialog;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.ScreenObject;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.util.Statics;

import java.awt.*;

/**
 * Created by Greg on 8/23/2014.
 * A button that can be put in a dialog window. Meant to be instantiated with an anonymous class.
 */
public abstract class DialogButton extends ScreenObject implements Clickable {

    public DialogButton(String text, Point position, int priority) {
        super(position, priority);
    }

    public void onRightClick() {
        //Do nothing.
    }

    public void onMiddleClick() {
        //Do nothing.
    }

    public void render() {
        if (graphic == null) {
            graphic = new Graphic(Statics.dialogButtonSmallRenderMask);
        }
    }

}
