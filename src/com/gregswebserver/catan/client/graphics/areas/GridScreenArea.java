package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/1/2015.
 * A screen area defined by two arrays of grid sizes.
 */
public abstract class GridScreenArea extends ScreenArea {

    private int[] widths, heights, cWidths, cHeights;
    private ScreenObject[][] objects;

    public GridScreenArea(Point position, int priority) {
        super(position, priority);
    }

    public abstract void resize(Dimension d);

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
        super.resize(new Dimension(width, height));
    }

    public Clickable getClickable(Point p) {
        if (p.x < 0 || p.y < 0 || p.x >= size.width || p.y >= size.height)
            return this;
        int x = -1;
        int y = -1;
        for (int i : cWidths)
            if (p.x >= i)
                x++;
        for (int i : cHeights)
            if (p.y >= i)
                y++;
        Point position = getPosition();
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        ScreenObject object = objects[y][x];
        return (object != null) ? object.getClickable(subPosition) : this;
    }

    public void add(ScreenObject object) {
        if (object == null) return;
        Point p = object.getPosition();
        if (objects[p.y][p.x] != null)
            return;
        objects[p.y][p.x] = object;
        super.add(object);
    }

    public Point getObjectPosition(ScreenObject object) {
        return getCellPosition(object.getPosition());
    }

    public Point getCellPosition(Point p) {
        return new Point(cWidths[p.x], cHeights[p.y]);
    }

    public Dimension getCellDimension(Point p) {
        return new Dimension(widths[p.x], heights[p.y]);
    }

    public Dimension getMultiDimension(Point p, Point size) {
        Dimension out = new Dimension();
        for (int i = p.x; i < p.x + size.x; i++)
            out.width += widths[i];
        for (int i = p.y; i < p.y + size.y; i++)
            out.height += heights[i];
        return out;
    }


    public void clear() {
        if (widths != null && heights != null)
            objects = new ScreenObject[widths.length][heights.length];
        super.clear();
    }
}
