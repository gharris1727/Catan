package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.TextObject;

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
    private UIScreenRegion background;
    private String text;
    private TextObject graphic;
    private int cursorPos;
    private int selectStart;
    private int selectEnd;

    public TextBox(Point position, int priority, RenderMask mask, UIStyle style) {
        super(position, priority, mask, style);
        state = EditableState.Deselected;
        previous = state;
    }

    public void setMask(RenderMask mask) {
        if (background != null)
            background.setMask(mask);
        super.setMask(mask);
    }

    public String getText() {
        return text;
    }

    //TODO: add text editing.

    public UserEvent onMouseClick(MouseEvent event) {
        return super.onMouseClick(event);
    }

    public UserEvent onMouseDrag(Point p) {
        return super.onMouseDrag(p);
    }

    public UserEvent onSelect() {
        return super.onSelect();
    }

    public UserEvent onDeselect() {
        return super.onDeselect();
    }

    public UserEvent onKeyTyped(KeyEvent event) {
        return super.onKeyTyped(event);
    }

    public void render() {
        clear();
        add(background);
        add(graphic);
    }

    protected enum EditableState {
        Deselected, SingleChar, DragSelect, DragMove,
    }
}
