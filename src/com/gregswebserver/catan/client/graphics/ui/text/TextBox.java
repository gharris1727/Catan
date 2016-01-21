package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public abstract class TextBox extends UIScreenRegion {

    private EditableState state;
    private EditableState previous;
    private String text;
    private int cursorPos;
    private int selectStart;
    private int selectEnd;

    public TextBox(int priority) {
        super(priority);
        state = EditableState.Deselected;
        previous = state;
        //TODO: implement.
    }

    public String getText() {
        return text;
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return super.onMouseClick(event);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return super.onMouseDrag(p);
    }

    @Override
    public UserEvent onSelect() {
        return super.onSelect();
    }

    @Override
    public UserEvent onDeselect() {
        return super.onDeselect();
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        return super.onKeyTyped(event);
    }

    private enum EditableState {
        Deselected, SingleChar, DragSelect, DragMove,
    }
}
