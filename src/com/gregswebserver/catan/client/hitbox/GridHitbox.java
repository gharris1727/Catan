package com.gregswebserver.catan.client.hitbox;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.hexarray.TwoDimensionalArray;

import java.awt.*;

/**
 * Created by Greg on 8/13/2014.
 * Hitbox object for storing what object rendered what on the screen.
 */
public class GridHitbox implements Hitbox {

    TwoDimensionalArray<Object> objectArray;
    int totalColWidth = 0;
    int totalRowHeight = 0;
    int[] colWidths, rowHeights;
    int[] cumulativeWidth, cumulativeHeight;

    public GridHitbox() {
    }

    protected GridHitbox init(int width, int height, int[] colWidths, int[] rowHeights) {
        this.colWidths = colWidths;
        cumulativeWidth = new int[colWidths.length];
        this.rowHeights = rowHeights;
        cumulativeHeight = new int[rowHeights.length];
        for (int i = 0; i < colWidths.length; i++) {
            totalColWidth += colWidths[i];
            cumulativeWidth[i] = totalColWidth;
        }
        for (int i = 0; i < rowHeights.length; i++) {
            totalRowHeight += rowHeights[i];
            cumulativeHeight[i] = totalRowHeight;
        }
        objectArray = new TwoDimensionalArray<>(width / totalColWidth * colWidths.length, height / totalRowHeight * rowHeights.length);
        return this;
    }

    public GridHitbox addObject(int x, int y, Object o) {
        objectArray.set(new Coordinate(x, y), o);
        return this;
    }

    public GridHitbox addObject(Point p, Object o) {
        objectArray.set(getArrayCoordinate(p), o);
        return this;
    }

    public Object getObject(Point p) {
        Object o = objectArray.get(getArrayCoordinate(p));
        if (o instanceof Hitbox) {
            return ((Hitbox) o).getObject(getSubCoordinate(p));
        }
        return o;
    }

    private Coordinate getArrayCoordinate(Point p) {
        int bucketX = p.x / totalColWidth;
        int bucketY = p.y / totalRowHeight;
        int specificX = p.x % totalColWidth;
        int specificY = p.y % totalRowHeight;
        int arrayX = bucketX * colWidths.length;
        int arrayY = bucketY * rowHeights.length;
        for (int width : cumulativeWidth) {
            if (specificX > width) arrayX++;
        }
        for (int height : cumulativeHeight) {
            if (specificY > height) arrayY++;
        }
        return new Coordinate(arrayX, arrayY);
    }

    private Point getSubCoordinate(Point p) {
        int bucketX = p.x / totalColWidth;
        int bucketY = p.y / totalRowHeight;
        int specificX = p.x % totalColWidth;
        int specificY = p.y % totalRowHeight;
        int arrayX = 0;
        int arrayY = 0;
        for (int width : cumulativeWidth) {
            if (specificX > width) arrayX++;
        }
        for (int height : cumulativeHeight) {
            if (specificY > height) arrayY++;
        }
        int subX = p.x - bucketX * totalColWidth - cumulativeWidth[arrayX];
        int subY = p.y - bucketY * totalRowHeight - cumulativeHeight[arrayY];
        return new Point(subX, subY);
    }

}
