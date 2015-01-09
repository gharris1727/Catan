package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/1/2015.
 * A screen area defined by two arrays of grid sizes.
 */
public abstract class GridObjectArea extends ObjectArea {

    private int[] widths, heights, cWidths, cHeights;
    private ScreenObject[][] objects;

    public GridObjectArea(Point position, int priority) {
        super(position, priority);
    }

    public abstract void setSize(Dimension d);

    protected void resize(int[] widths, int[] heights) {
        this.widths = widths;
        this.cWidths = new int[widths.length + 1];
        this.heights = heights;
        this.cHeights = new int[heights.length + 1];
        int width = 0;
        int height = 0;
        for (int i = 0; i < widths.length; i++) {
            width += widths[i];
            cWidths[i + 1] = width;
        }
        for (int i = 0; i < heights.length; i++) {
            height += heights[i];
            cHeights[i + 1] = height;
        }
        super.setSize(new Dimension(width, height));
    }

    public boolean canRender() {
        return widths != null && heights != null && super.canRender();
    }

    public Clickable getClickable(Point p) {
        if (p.x < 0 || p.y < 0 || p.x >= getSize().width || p.y >= getSize().height)
            return null;
        int x = -1;
        int y = -1;
        for (int i : cWidths)
            if (p.x >= i)
                x++;
        for (int i : cHeights)
            if (p.y >= i)
                y++;
        Point subPosition = new Point(p.x - cWidths[x], p.y - cHeights[y]);
        ScreenObject object = objects[y][x];
        return (object == null) ? this : object.getClickable(subPosition);
    }

    public ScreenObject add(ScreenObject object) {
        if (object == null) return null;
        Point p = object.getPosition();
        ScreenObject existing = objects[p.y][p.x];
        if (existing != null)
            super.remove(existing);
        objects[p.y][p.x] = object;
        return super.add(object);
    }

    public ScreenObject remove(ScreenObject object) {
        if (object == null) return null;
        Point p = object.getPosition();
        ScreenObject existing = objects[p.y][p.x];
        if (!object.equals(existing))
            return object;
        objects[p.y][p.x] = null;
        return super.add(object);
    }

    public void clear() {
        if (!canRender()) return;
        objects = new ScreenObject[widths.length][heights.length];
        super.clear();
    }

    public Point getObjectPosition(ScreenObject object) {
        return getCellPosition(object.getPosition());
    }

    private Point getCellPosition(Point p) {
        return new Point(cWidths[p.x], cHeights[p.y]);
    }

    public Dimension getCellDimension(Point p) {
        return new Dimension(widths[p.x], heights[p.y]);
    }

    //Unused and unsupported, maybe sometime in the future.
    //TODO: add support for multi-cell objects.
    private Dimension getMultiDimension(Point p, Point size) {
        Dimension out = new Dimension();
        for (int i = p.x; i < p.x + size.x; i++)
            out.width += widths[i];
        for (int i = p.y; i < p.y + size.y; i++)
            out.height += heights[i];
        return out;
    }

}
