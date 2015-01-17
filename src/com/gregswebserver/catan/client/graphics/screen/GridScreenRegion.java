package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/1/2015.
 * A screen area defined by two arrays of grid sizes.
 */
public abstract class GridScreenRegion extends ScreenRegion {

    private int[] widths, heights, cWidths, cHeights;
    private ScreenObject[][] objects;

    public GridScreenRegion(Point position, int priority, RenderMask mask) {
        super(position, priority, mask);
    }

    protected GridScreenRegion(Point position, int priority) {
        super(position, priority);
    }

    public abstract void setMask(RenderMask mask);

    protected final void setGridSize(int[] widths, int[] heights, RenderMask mask) {
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
        super.setMask(mask);
    }

    public Clickable getClickable(Point p) {
        if (p == null || !getMask().containsPoint(p))
            return this;
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
        objects = new ScreenObject[heights.length][widths.length];
        super.clear();
    }

    protected Point getObjectPosition(ScreenObject object) {
        int x = object.getPosition().x;
        int y = object.getPosition().y;
        x = cWidths[x];
        y = cHeights[y];
        return new Point(x, y);
    }

    protected final Dimension getCellDimension(Point p) {
        return new Dimension(widths[p.x], heights[p.y]);
    }

}
