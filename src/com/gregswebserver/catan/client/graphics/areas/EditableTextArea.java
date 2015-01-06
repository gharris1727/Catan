package com.gregswebserver.catan.client.graphics.areas;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public abstract class EditableTextArea extends ColorScreenArea {

    protected TiledArea background;
    protected TextArea foreground;
    protected String text;
    protected EditableState state;
    protected EditableState previous;
    protected int cursorPos;
    protected int selectStart;
    protected int selectEnd;

    public EditableTextArea(Point position, int priority) {
        super(position, priority);
        state = EditableState.Deselected;
        previous = state;
    }

    public void resize(Dimension d) {
        super.resize(d);
        background.resize(d);
    }

    protected abstract void render();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        foreground = null;
    }

    protected enum EditableState {
        Deselected, SingleChar, DragSelect, DragMove,
    }

}
