package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.util.Graphic;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public abstract class EditableTextScreenRegion extends TextScreenRegion {

    protected String text;
    protected EditableState state;
    protected EditableState previous;
    protected int cursorPos;
    protected int selectStart;
    protected int selectEnd;

    public EditableTextScreenRegion(Point position, int priority, Font font) {
        super(position, priority, font);
        state = EditableState.Deselected;
        previous = state;
    }

    protected void render() {
        clear();
        for (int i = 0; i < chars.length; i++)
            add(new CharStaticObject(i, i, chars[i]));
    }

    public String getText() {
        return text;
    }

    protected enum EditableState {
        Deselected, SingleChar, DragSelect, DragMove,
    }

    private class CharStaticObject extends StaticObject {

        public CharStaticObject(int position, int priority, Graphic graphic) {
            super(new Point(position, 0), priority, graphic);
            setClickable(EditableTextScreenRegion.this);
        }

        public String toString() {
            return "CharStaticObject #" + getPosition().x + " inside " + EditableTextScreenRegion.this;
        }
    }

}
