package com.gregswebserver.catan.util;

import com.gregswebserver.catan.game.board.hexarray.Coordinate;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * Static graphics configuration, meant as a common source for loading graphics, rendering, and hitboxes.
 * Every thing should be created in one shot, and require no other resources.
 */
public class GraphicsConfig {

    public static final String tileGraphicSourcePath = "/graphics/tiles.jpg";
    public static final String oceanGraphicSourcePath = "/graphics/ocean.png";
    public static final String beachGraphicSourcePath = "/graphics/beach.png";
    public static final String blankGraphicSourcePath = "/graphics/blank.png";

    public static final String redTeamGraphicSourcePath = "/graphics/redTeam.png";
    public static final String orangeTeamGraphicSourcePath = "/graphics/orangeTeam.png";
    public static final String blueTeamGraphicSourcePath = "/graphics/blueTeam.png";
    public static final String whiteTeamGraphicSourcePath = "/graphics/whiteTeam.png";

    public static final String resourceCardGraphicSourcePath = "/graphics/resources.png";
    public static final String developmentCardGraphicSourcePath = "/graphics/development.png";
    public static final String achievementCardGraphicSourcePath = "/graphics/achievement.png";

    public static final String diceRollTokenGraphicSourcePath = "/graphics/tokens.png";
    public static final String iconGraphicSourcePath = "/graphics/icons.png";
    public static final String dialogGraphicSourcePath = "/graphics/dialogs.png";
    public static final String interfaceGraphicSourcePath = "/graphics/interface.png";
    public static final String tradeGraphicSourcePath = "/graphics/trade.png";

    public static final Dimension tileRenderMaskSize = new Dimension(112, 96);
    public static final Dimension vertexRenderMaskSize = new Dimension(0, 16);
    public static final Dimension horizontalRenderMaskSize = new Dimension(64, 16);
    public static final Dimension diagonalUpRenderMaskSize = new Dimension(16, 54);

    public static final Dimension resourceCardRenderMaskSize = new Dimension(128, 192);
    public static final Dimension resourceCardRenderMaskCornerSize = new Dimension(7, 7);
    public static final Dimension achievementCardRenderMaskSize = new Dimension(192, 192);

    public static final Dimension diceRollTokenRenderMaskSize = new Dimension(16, 16);
    public static final Dimension tradeRatioRenderMaskSize = new Dimension(32, 16);
    public static final Dimension resourceIconRenderMaskSize = new Dimension(16, 16);

    public static final Dimension tradeDiagonalRenderMaskSize = new Dimension(10, 32);
    public static final Dimension tradeHorizontalRenderMaskSize = new Dimension(32, 10);

    public static final Dimension dialogButtonSmallRenderMaskSize = new Dimension(64, 32);
    public static final Dimension dialogButtonMediumRenderMaskSize = new Dimension(128, 32);
    public static final Dimension dialogButtonLargeRenderMaskSize = new Dimension(256, 32);

    public static final Point resourceTileTokenRender = new Point(tileRenderMaskSize.width / 2 - diceRollTokenRenderMaskSize.width, tileRenderMaskSize.height / 2 - diceRollTokenRenderMaskSize.height);
    public static final Point tradeTileResourceIconRender = new Point(tileRenderMaskSize.width / 2 - resourceIconRenderMaskSize.width / 2, tileRenderMaskSize.height / 2 - resourceIconRenderMaskSize.height / 2 + 12);
    public static final Point tradeTileRatioRender = new Point(tileRenderMaskSize.width / 2 - tradeRatioRenderMaskSize.width / 2, tileRenderMaskSize.height / 2 - tradeRatioRenderMaskSize.height / 2 - 12);

    public static final Point tradeUpLeftRender = new Point(tileRenderMaskSize.height / 4, -tradeDiagonalRenderMaskSize.width / 2);
    public static final Point tradeUpRightRender = new Point(tileRenderMaskSize.height * 3 / 4, 0);
    public static final Point tradeLeftRender = new Point(0, tileRenderMaskSize.height / 2 - tradeHorizontalRenderMaskSize.height / 2);
    public static final Point tradeRightRender = new Point(tileRenderMaskSize.width - tradeHorizontalRenderMaskSize.width, tileRenderMaskSize.height / 2 - tradeHorizontalRenderMaskSize.height / 2);
    public static final Point tradeDownLeftRender = new Point(tileRenderMaskSize.height / 4, tileRenderMaskSize.height - tradeDiagonalRenderMaskSize.height + tradeDiagonalRenderMaskSize.width / 2);
    public static final Point tradeDownRightRender = new Point(tileRenderMaskSize.height * 3 / 4, tileRenderMaskSize.height - tradeDiagonalRenderMaskSize.height);

    public static final Point hillTextureLocation = new Point(235, 523);
    public static final Point forestTextureLocation = new Point(586, 321);
    public static final Point pastureTextureLocation = new Point(484, 551);
    public static final Point mountainTextureLocation = new Point(248, 104);
    public static final Point fieldTextureLocation = new Point(476, 116);
    public static final Point desertTextureLocation = new Point(131, 284);
    public static final Point oceanTextureLocation = new Point(351, 333);

    public static final Point beachSingleUpLocation = new Point();
    public static final Point beachSingleUpRightLocation = new Point();
    public static final Point beachSingleDownRightLocation = new Point();
    public static final Point beachSingleDownLocation = new Point();
    public static final Point beachSingleDownLeftLocation = new Point();
    public static final Point beachSingleUpLeftLocation = new Point();

    public static final Point beachDoubleUpRightLocation = new Point();
    public static final Point beachDoubleRightLocation = new Point();
    public static final Point beachDoubleDownRightLocation = new Point();
    public static final Point beachDoubleDownLeftLocation = new Point();
    public static final Point beachDoubleLeftLocation = new Point();
    public static final Point beachDoubleUpLeftLocation = new Point();

    public static final Point tradeUpRightLocation = new Point();
    public static final Point tradeDoubleRightLocation = new Point();
    public static final Point tradeDownRightLocation = new Point();
    public static final Point tradeDownLeftLocation = new Point();
    public static final Point tradeLeftLocation = new Point();
    public static final Point tradeUpLeftLocation = new Point();

    public static final Point brickCardTextureLocation = new Point();
    public static final Point lumberCardTextureLocation = new Point();
    public static final Point woolCardTextureLocation = new Point();
    public static final Point grainCardTextureLocation = new Point();
    public static final Point oreCardTextureLocation = new Point();

    public static final Point knightCardTextureLocation = new Point();
    public static final Point progressCardTextureLocation = new Point();
    public static final Point victoryPointCardTextureLocation = new Point();

    public static final Point longestRoadCardTextureLocation = new Point();
    public static final Point largestArmyCardTextureLocation = new Point();

    public static final Point diceRollTwoLocation = new Point();
    public static final Point diceRollThreeLocation = new Point();
    public static final Point diceRollFourLocation = new Point();
    public static final Point diceRollFiveLocation = new Point();
    public static final Point diceRollSixLocation = new Point();
    public static final Point diceRollSevenLocation = new Point();
    public static final Point diceRollEightLocation = new Point();
    public static final Point diceRollNineLocation = new Point();
    public static final Point diceRollTenLocation = new Point();
    public static final Point diceRollElevenLocation = new Point();
    public static final Point diceRollTwelveLocation = new Point();

    public static final Point tradeRatioThreeLocation = new Point();
    public static final Point tradeRatioTwoLocation = new Point();

    public static final Point brickIconTextureLocation = new Point();
    public static final Point lumberIconTextureLocation = new Point();
    public static final Point woolIconTextureLocation = new Point();
    public static final Point grainIconTextureLocation = new Point();
    public static final Point oreIconTextureLocation = new Point();
    public static final Point questionIconTextureLocation = new Point();

    public static final Point settlementLeftLocation = new Point();
    public static final Point settlementRightLocation = new Point();
    public static final Point cityLeftLocation = new Point();
    public static final Point cityRightLocation = new Point();
    public static final Point horizontalPathLocation = new Point();
    public static final Point diagonalUpPathLocation = new Point();
    public static final Point diagonalDownPathLocation = new Point();

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
