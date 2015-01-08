package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * Static graphics configuration, meant as a common source for loading graphics, rendering, and hitboxes.
 * Every thing should be created in one shot, and require no other resources.
 */
public class GraphicsConfig {

    public static final Dimension mapEdgeBufferSize = new Dimension(64, 64);

    public static final int boardUnitWidth = 200;
    public static final int boardUnitHeight = 112;

    public static final int[][] tileOffsets = {
            {12, 112}, //Horizontal
            {16, 72}}; //Vertical
    public static final int[][] edgeOffsets = {
            {0, 0, 36, 100, 100, 136}, //Horizontal
            {9, 65, 0, 65, 9, 56}}; //Vertical
    public static final int[][] vertOffsets = {
            {0, 24, 100, 124}, //Horizontal
            {56, 0, 0, 56}}; //Vertical

    public static Dimension boardToScreen(Dimension size) {
        int outW = ((size.width + 1) / 2) * boardUnitWidth;
        int outH = (size.height + 1) * boardUnitHeight;
        return new Dimension(outW, outH);
    }

    public static Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * boardUnitWidth;
        int outY = (c.y) * boardUnitHeight;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    public static Point edgeToScreen(Coordinate c) {
        int outX = (c.x / 6) * boardUnitWidth;
        int outY = (c.y) * boardUnitHeight;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    public static Point vertexToScreen(Coordinate c) {
        int outX = (c.x / 4) * boardUnitWidth;
        int outY = (c.y) * boardUnitHeight;
        outX += vertOffsets[0][c.x % 4];
        outY += vertOffsets[1][c.x % 4];
        return new Point(outX, outY);
    }

}
