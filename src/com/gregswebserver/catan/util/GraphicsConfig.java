package com.gregswebserver.catan.util;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * Static graphics configuration, meant as a common source for loading graphics, rendering, and hitboxes.
 * Every thing should be created in one shot, and require no other resources.
 */
public class GraphicsConfig {

    public static final String tileGraphicSourcePath = "res/graphics/tiles.jpg";
    public static final String oceanGraphicSourcePath = "res/graphics/ocean.png";
    public static final String blankGraphicSourcePath = "res/graphics/blank.png";

    public static final String redTeamGraphicSourcePath = "res/graphics/redTeam.png";
    public static final String orangeTeamGraphicSourcePath = "res/graphics/orangeTeam.png";
    public static final String blueTeamGraphicSourcePath = "res/graphics/blueTeam.png";
    public static final String whiteTeamGraphicSourcePath = "res/graphics/whiteTeam.png";

    public static final Dimension tileRenderMaskSize = new Dimension(112, 96);
    public static final Dimension vertexRenderMaskSize = new Dimension(0, 16);
    public static final Dimension horizontalRenderMaskSize = new Dimension(64, 16);
    public static final Dimension diagonalUpRenderMaskSize = new Dimension(18, 52);
    public static final Dimension diagonalDownRenderMaskSize = new Dimension(18, 52);

    public static final Point hillTextureLocation = new Point(235, 523);
    public static final Point forestTextureLocation = new Point(586, 321);
    public static final Point pastureTextureLocation = new Point(484, 551);
    public static final Point mountainTextureLocation = new Point(248, 104);
    public static final Point fieldTextureLocation = new Point(476, 116);
    public static final Point desertTextureLocation = new Point(131, 284);
    public static final Point oceanTextureLocation = new Point(351, 333);

    public static final Point redSettlementLocationLeft = new Point();
    public static final Point redSettlementLocationRight = new Point();
    public static final Point redCityLocationLeft = new Point();
    public static final Point redCityLocationRight = new Point();
    public static final Point redHorizontalPathLocation = new Point();
    public static final Point redDiagonalUpPathLocation = new Point();
    public static final Point redDiagonalDownPathLocation = new Point();

    public static final Point orangeSettlementLocationLeft = new Point();
    public static final Point orangeSettlementLocationRight = new Point();
    public static final Point orangeCityLocationLeft = new Point();
    public static final Point orangeCityLocationRight = new Point();
    public static final Point orangeHorizontalPathLocation = new Point();
    public static final Point orangeDiagonalUpPathLocation = new Point();
    public static final Point orangeDiagonalDownPathLocation = new Point();

    public static final Point blueSettlementLocationLeft = new Point();
    public static final Point blueSettlementLocationRight = new Point();
    public static final Point blueCityLocationLeft = new Point();
    public static final Point blueCityLocationRight = new Point();
    public static final Point blueHorizontalPathLocation = new Point();
    public static final Point blueDiagonalUpPathLocation = new Point();
    public static final Point blueDiagonalDownPathLocation = new Point();

    public static final Point whiteSettlementLocationLeft = new Point();
    public static final Point whiteSettlementLocationRight = new Point();
    public static final Point whiteCityLocationLeft = new Point();
    public static final Point whiteCityLocationRight = new Point();
    public static final Point whiteHorizontalPathLocation = new Point();
    public static final Point whiteDiagonalUpPathLocation = new Point();
    public static final Point whiteDiagonalDownPathLocation = new Point();

    public static final Point oceanVertexLocationLeft = new Point();
    public static final Point oceanVertexLocationRight = new Point();
    public static final Point oceanHorizontalPathLocation = new Point();
    public static final Point oceanDiagonalUpPathLocation = new Point();
    public static final Point oceanDiagonalDownPathLocation = new Point();

    public static final Point blankVertexLocationLeft = new Point();
    public static final Point blankVertexLocationRight = new Point();
    public static final Point blankHorizontalPathLocation = new Point();
    public static final Point blankDiagonalUpPathLocation = new Point();
    public static final Point blankDiagonalDownPathLocation = new Point();

    public static final int boardUnitWidth = 250;
    public static final int boardUnitHeight = 200;

    public static final int[][] tileOffsets = {
            {15, 130}, //Horizontal
            {16, 60}}; //Vertical
    public static final int[][] edgeOffsets = {
            {0, 0, 39, 100, 100, 150}, //Horizontal
            {8, 65, 0, 65, 8, 30}}; //Vertical
    public static final int[][] vertOffsets = {
            {0, 50, 100, 150}, //Horizontal
            {100, 0, 0, 100}}; //Vertical

    public static Point tileToScreen(Coordinate c) {
        int outX = (c.x / 2) * boardUnitWidth;
        int outY = (c.y / 2) * boardUnitHeight;
        outX += tileOffsets[0][c.x % 2];
        outY += tileOffsets[1][c.x % 2];
        return new Point(outX, outY);
    }

    public static Point edgeToScreen(Coordinate c) {
        int outX = (c.x / 6) * boardUnitWidth;
        int outY = (c.y / 6) * boardUnitHeight;
        outX += edgeOffsets[0][c.x % 6];
        outY += edgeOffsets[1][c.x % 6];
        return new Point(outX, outY);
    }

    public static Point vertexToScreen(Coordinate c) {
        int outX = (c.x / 4) * boardUnitWidth;
        int outY = (c.y / 4) * boardUnitHeight;
        outX += vertOffsets[0][c.x % 4];
        outY += vertOffsets[1][c.x % 4];
        return new Point(outX, outY);
    }
}
