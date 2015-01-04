package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public class EditableTextArea extends ColorScreenArea {

    private TiledArea background;
    private TextArea foreground;
    private String text;
    private Font font;
    private EditableState state;
    private EditableState previous;
    private int cursorPos;
    private int selectStart;
    private int selectEnd;

    public EditableTextArea(Point position, int priority, Graphic texture, Font font) {
        super(position, priority, new EditableTextClickable());
        ((EditableTextClickable) clickable).init(this);
        state = EditableState.Deselected;
        previous = state;
        background = new TiledArea(new Point(), 0, clickable, texture);
        this.font = font;
    }

    public void resize(Dimension d) {
        super.resize(d);
        background.resize(d);
    }

    protected void render() {
        clear();
        add(background);
        if (foreground == null)
            foreground = new TextArea(new Point(), 0, clickable, font, text);
        add(foreground);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        foreground = null;
    }

    private enum EditableState {
        Deselected, SingleChar, DragSelect, DragMove,
    }

    private static class EditableTextClickable implements Clickable {

        private EditableTextArea area;

        private void init(EditableTextArea area) {
            this.area = area;
        }

        public void onSelect() {
            area.state = area.previous;
        }

        public void onDeselect() {
            area.previous = area.state;
            area.state = EditableState.Deselected;
        }

        public void onMouseDrag(Point p) {
            //TODO: finish editing the text field.
        }

        public void onKeyTyped(KeyEvent event) {

        }

        public void onMouseClick(MouseEvent event) {

        }
    }
}
