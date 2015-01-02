package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.renderer.ScreenObject;
import com.gregswebserver.catan.client.input.clickables.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/1/2015.
 * A screen area defined by two arrays of grid sizes.
 */
public abstract class GridScreenArea extends ScreenArea {

    private int[] widths, heights, cWidths, cHeights;
    private ScreenObject[][] objects;

    public GridScreenArea(Point position, int priority, Clickable clickable) {
        super(position, priority, clickable);
    }

    public void resize(int[] widths, int[] heights) {
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
            return clickable;
        int x = -1;
        int y = -1;
        for (int i : cWidths)
            if (p.x >= i)
                x++;
        for (int i : cHeights)
            if (p.y >= i)
                y++;
        //TODO: check these lines for validity
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        ScreenObject object = objects[y][x];
        return (object != null) ? object.getClickable(subPosition) : clickable;
    }

    public void add(ScreenObject object) {
        Point p = object.getPosition();
        objects[p.y][p.x] = object;
        super.add(object);
    }

    public Point getObjectPosition(ScreenObject object) {
        return getCellPosition(object.getPosition());
    }

    public Dimension getCellDimension(Point p) {
        return new Dimension(widths[p.x], heights[p.y]);
    }

    public Point getCellPosition(Point p) {
        return new Point(cWidths[p.x], cHeights[p.y]);
    }

    public void clear() {
        if (widths != null && heights != null)
            objects = new ScreenObject[widths.length][heights.length];
        super.clear();
    }
}
