package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.style.TextStyle;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public abstract class TextLabel extends StaticObject {

    protected TextLabel(int priority, TextStyle style, String text) {
        super(priority, new TextGraphic(style,text));
    }
}
