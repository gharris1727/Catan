package com.gregswebserver.catan.util;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.GraphicSource;
import com.gregswebserver.catan.client.masks.*;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.game.gameplay.generator.RandomBoardGenerator;

import java.awt.*;

/**
 * Created by Greg on 8/15/2014.
 * Class full of static objects that need to be loaded.
 * Everything in here should require loading outside resources and take a significant amount of time to load.
 * This is centralized into one place so that it's possible to load this in one shot in the background.
 */
public class Statics {
    public static Graphic nullGraphic;

    public static GraphicSource tileGraphicSource;
    public static GraphicSource oceanGraphicSource;
    public static GraphicSource beachGraphicSource;
    public static GraphicSource tradeGraphicSource;
    public static GraphicSource blankGraphicSource;

    public static GraphicSource redTeamGraphicSource;
    public static GraphicSource orangeTeamGraphicSource;
    public static GraphicSource blueTeamGraphicSource;
    public static GraphicSource whiteTeamGraphicSource;

    public static GraphicSource resourceCardGraphicSource;
    public static GraphicSource developmentCardGraphicSource;
    public static GraphicSource achievementCardGraphicSource;

    public static GraphicSource diceRollTokenGraphicSource;
    public static GraphicSource iconGraphicSource;
    public static GraphicSource dialogGraphicSource;
    public static GraphicSource interfaceGraphicSource;

    public static RenderMask tileRenderMask;
    public static RenderMask vertexRenderMaskLeft;
    public static RenderMask vertexRenderMaskRight;
    public static RenderMask horizontalRenderMask;
    public static RenderMask diagonalUpRenderMask;
    public static RenderMask diagonalDownRenderMask;

    public static RenderMask resourceCardRenderMask;
    public static RenderMask achievementCardRenderMask;
    public static RenderMask diceRollTokenRenderMask;
    public static RenderMask tradeRatioRenderMask;
    public static RenderMask resourceIconRenderMask;

    public static RenderMask tradeDiagonalUpRenderMask;
    public static RenderMask tradeHorizontalRenderMask;
    public static RenderMask tradeDiagonalDownRenderMask;

    public static RenderMask dialogButtonSmallRenderMask;
    public static RenderMask dialogButtonMediumRenderMask;
    public static RenderMask dialogButtonLargeRenderMask;

    public static Graphic hillTexture;
    public static Graphic forestTexture;
    public static Graphic pastureTexture;
    public static Graphic mountainTexture;
    public static Graphic fieldTexture;
    public static Graphic desertTexture;
    public static Graphic oceanTexture;

    public static Graphic beachSingleUp;
    public static Graphic beachSingleUpRight;
    public static Graphic beachSingleDownRight;
    public static Graphic beachSingleDown;
    public static Graphic beachSingleDownLeft;
    public static Graphic beachSingleUpLeft;

    public static Graphic beachDoubleUpRight;
    public static Graphic beachDoubleRight;
    public static Graphic beachDoubleDownRight;
    public static Graphic beachDoubleDownLeft;
    public static Graphic beachDoubleLeft;
    public static Graphic beachDoubleUpLeft;

    public static Graphic tradeUpRight;
    public static Graphic tradeRight;
    public static Graphic tradeDownRight;
    public static Graphic tradeDownLeft;
    public static Graphic tradeLeft;
    public static Graphic tradeUpLeft;

    public static Graphic brickCardTexture;
    public static Graphic lumberCardTexture;
    public static Graphic woolCardTexture;
    public static Graphic grainCardTexture;
    public static Graphic oreCardTexture;

    public static Graphic knightCardTexture;
    public static Graphic progressCardTexture;
    public static Graphic victoryPointCardTexture;

    public static Graphic longestRoadCardTexture;
    public static Graphic largestArmyCardTexture;

    public static Graphic diceRollTwo;
    public static Graphic diceRollThree;
    public static Graphic diceRollFour;
    public static Graphic diceRollFive;
    public static Graphic diceRollSix;
    public static Graphic diceRollSeven;
    public static Graphic diceRollEight;
    public static Graphic diceRollNine;
    public static Graphic diceRollTen;
    public static Graphic diceRollEleven;
    public static Graphic diceRollTwelve;

    public static Graphic tradeRatioThree;
    public static Graphic tradeRatioTwo;

    public static Graphic brickIconTexture;
    public static Graphic lumberIconTexture;
    public static Graphic woolIconTexture;
    public static Graphic grainIconTexture;
    public static Graphic oreIconTexture;
    public static Graphic questionIconTexture;

    public static Graphic redSettlementLeft;
    public static Graphic redSettlementRight;
    public static Graphic redCityLeft;
    public static Graphic redCityRight;
    public static Graphic redHorizontalPath;
    public static Graphic redDiagonalUpPath;
    public static Graphic redDiagonalDownPath;

    public static Graphic orangeSettlementLeft;
    public static Graphic orangeSettlementRight;
    public static Graphic orangeCityLeft;
    public static Graphic orangeCityRight;
    public static Graphic orangeHorizontalPath;
    public static Graphic orangeDiagonalUpPath;
    public static Graphic orangeDiagonalDownPath;

    public static Graphic blueSettlementLeft;
    public static Graphic blueSettlementRight;
    public static Graphic blueCityLeft;
    public static Graphic blueCityRight;
    public static Graphic blueHorizontalPath;
    public static Graphic blueDiagonalUpPath;
    public static Graphic blueDiagonalDownPath;

    public static Graphic whiteSettlementLeft;
    public static Graphic whiteSettlementRight;
    public static Graphic whiteCityLeft;
    public static Graphic whiteCityRight;
    public static Graphic whiteHorizontalPath;
    public static Graphic whiteDiagonalUpPath;
    public static Graphic whiteDiagonalDownPath;

    public static Graphic oceanVertexLeft;
    public static Graphic oceanVertexRight;
    public static Graphic oceanHorizontalPath;
    public static Graphic oceanDiagonalUpPath;
    public static Graphic oceanDiagonalDownPath;

    public static Graphic blankVertexLeft;
    public static Graphic blankVertexRight;
    public static Graphic blankHorizontalPath;
    public static Graphic blankDiagonalUpPath;
    public static Graphic blankDiagonalDownPath;

    public static GameType BASE_GAME;

    public Statics() {
        //Dummy graphic that is used to prevent a NullPointerException in some cases.
        nullGraphic = new Graphic(new Dimension(1, 1));

        tileGraphicSource = GraphicSource.load(GraphicsConfig.tileGraphicSourcePath);
        oceanGraphicSource = GraphicSource.load(GraphicsConfig.oceanGraphicSourcePath);
        beachGraphicSource = GraphicSource.load(GraphicsConfig.beachGraphicSourcePath);
        blankGraphicSource = GraphicSource.load(GraphicsConfig.blankGraphicSourcePath);

        redTeamGraphicSource = GraphicSource.load(GraphicsConfig.redTeamGraphicSourcePath);
        orangeTeamGraphicSource = GraphicSource.load(GraphicsConfig.orangeTeamGraphicSourcePath);
        blueTeamGraphicSource = GraphicSource.load(GraphicsConfig.blueTeamGraphicSourcePath);
        whiteTeamGraphicSource = GraphicSource.load(GraphicsConfig.whiteTeamGraphicSourcePath);

        resourceCardGraphicSource = GraphicSource.load(GraphicsConfig.resourceCardGraphicSourcePath);
        developmentCardGraphicSource = GraphicSource.load(GraphicsConfig.developmentCardGraphicSourcePath);
        achievementCardGraphicSource = GraphicSource.load(GraphicsConfig.achievementCardGraphicSourcePath);

        diceRollTokenGraphicSource = GraphicSource.load(GraphicsConfig.diceRollTokenGraphicSourcePath);
        iconGraphicSource = GraphicSource.load(GraphicsConfig.iconGraphicSourcePath);
        dialogGraphicSource = GraphicSource.load(GraphicsConfig.dialogGraphicSourcePath);
        interfaceGraphicSource = GraphicSource.load(GraphicsConfig.interfaceGraphicSourcePath);
        tradeGraphicSource = GraphicSource.load(GraphicsConfig.tradeGraphicSourcePath);

        tileRenderMask = new HexagonalMask(GraphicsConfig.tileRenderMaskSize);
        vertexRenderMaskLeft = new TriangularMask(GraphicsConfig.vertexRenderMaskSize);
        vertexRenderMaskRight = new FlippedMask(vertexRenderMaskLeft, FlippedMask.HORIZONTAL);
        horizontalRenderMask = new RectangularMask(GraphicsConfig.horizontalRenderMaskSize);
        diagonalUpRenderMask = new DiagonalMask(GraphicsConfig.diagonalUpRenderMaskSize);
        diagonalDownRenderMask = new FlippedMask(diagonalUpRenderMask, FlippedMask.VERTICAL);

        resourceCardRenderMask = new RoundedRectangularMask(GraphicsConfig.resourceCardRenderMaskSize, GraphicsConfig.resourceCardRenderMaskCornerSize);
        achievementCardRenderMask = new RectangularMask(GraphicsConfig.achievementCardRenderMaskSize);

        diceRollTokenRenderMask = new RoundedMask(GraphicsConfig.diceRollTokenRenderMaskSize);
        tradeRatioRenderMask = new RectangularMask(GraphicsConfig.tradeRatioRenderMaskSize);
        resourceIconRenderMask = new RectangularMask(GraphicsConfig.resourceIconRenderMaskSize);

        tradeHorizontalRenderMask = new RectangularMask(GraphicsConfig.tradeHorizontalRenderMaskSize);
        tradeDiagonalUpRenderMask = new DiagonalMask(GraphicsConfig.tradeDiagonalRenderMaskSize);
        tradeDiagonalDownRenderMask = new FlippedMask(tradeDiagonalUpRenderMask, FlippedMask.VERTICAL);

        dialogButtonSmallRenderMask = new RoundedRectangularMask(GraphicsConfig.dialogButtonSmallRenderMaskSize);
        dialogButtonMediumRenderMask = new RoundedRectangularMask(GraphicsConfig.dialogButtonMediumRenderMaskSize);
        dialogButtonLargeRenderMask = new RoundedRectangularMask(GraphicsConfig.dialogButtonLargeRenderMaskSize);

        hillTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.hillTextureLocation);
        forestTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.forestTextureLocation);
        pastureTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.pastureTextureLocation);
        mountainTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.mountainTextureLocation);
        fieldTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.fieldTextureLocation);
        desertTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.desertTextureLocation);
        oceanTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.oceanTextureLocation);

        beachSingleUp = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleUpLocation);
        beachSingleUpRight = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleUpRightLocation);
        beachSingleDownRight = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleDownRightLocation);
        beachSingleDown = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleDownLocation);
        beachSingleDownLeft = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleDownLeftLocation);
        beachSingleUpLeft = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachSingleUpLeftLocation);

        beachDoubleUpRight = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleUpRightLocation);
        beachDoubleRight = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleRightLocation);
        beachDoubleDownRight = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleDownRightLocation);
        beachDoubleDownLeft = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleDownLeftLocation);
        beachDoubleLeft = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleLeftLocation);
        beachDoubleUpLeft = new Graphic(beachGraphicSource, tileRenderMask, GraphicsConfig.beachDoubleUpLeftLocation);

        tradeUpRight = new Graphic(tradeGraphicSource, tradeDiagonalUpRenderMask, GraphicsConfig.tradeUpRightLocation);
        tradeRight = new Graphic(tradeGraphicSource, tradeHorizontalRenderMask, GraphicsConfig.tradeDoubleRightLocation);
        tradeDownRight = new Graphic(tradeGraphicSource, tradeDiagonalDownRenderMask, GraphicsConfig.tradeDownRightLocation);
        tradeDownLeft = new Graphic(tradeGraphicSource, tradeDiagonalUpRenderMask, GraphicsConfig.tradeDownLeftLocation);
        tradeLeft = new Graphic(tradeGraphicSource, tradeHorizontalRenderMask, GraphicsConfig.tradeLeftLocation);
        tradeUpLeft = new Graphic(tradeGraphicSource, tradeDiagonalDownRenderMask, GraphicsConfig.tradeUpLeftLocation);

        brickCardTexture = new Graphic(resourceCardGraphicSource, resourceCardRenderMask, GraphicsConfig.brickCardTextureLocation);
        lumberCardTexture = new Graphic(resourceCardGraphicSource, resourceCardRenderMask, GraphicsConfig.lumberCardTextureLocation);
        woolCardTexture = new Graphic(resourceCardGraphicSource, resourceCardRenderMask, GraphicsConfig.woolCardTextureLocation);
        grainCardTexture = new Graphic(resourceCardGraphicSource, resourceCardRenderMask, GraphicsConfig.grainCardTextureLocation);
        oreCardTexture = new Graphic(resourceCardGraphicSource, resourceCardRenderMask, GraphicsConfig.oreCardTextureLocation);

        knightCardTexture = new Graphic(developmentCardGraphicSource, resourceCardRenderMask, GraphicsConfig.knightCardTextureLocation);
        progressCardTexture = new Graphic(developmentCardGraphicSource, resourceCardRenderMask, GraphicsConfig.progressCardTextureLocation);
        victoryPointCardTexture = new Graphic(developmentCardGraphicSource, resourceCardRenderMask, GraphicsConfig.victoryPointCardTextureLocation);

        longestRoadCardTexture = new Graphic(achievementCardGraphicSource, achievementCardRenderMask, GraphicsConfig.longestRoadCardTextureLocation);
        largestArmyCardTexture = new Graphic(achievementCardGraphicSource, achievementCardRenderMask, GraphicsConfig.largestArmyCardTextureLocation);

        diceRollTwo = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollTwoLocation);
        diceRollThree = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollThreeLocation);
        diceRollFour = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollFourLocation);
        diceRollFive = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollFiveLocation);
        diceRollSix = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollSixLocation);
        diceRollSeven = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollSevenLocation);
        diceRollEight = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollEightLocation);
        diceRollNine = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollNineLocation);
        diceRollTen = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollTenLocation);
        diceRollEleven = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollElevenLocation);
        diceRollTwelve = new Graphic(diceRollTokenGraphicSource, diceRollTokenRenderMask, GraphicsConfig.diceRollTwelveLocation);

        tradeRatioThree = new Graphic(iconGraphicSource, tradeRatioRenderMask, GraphicsConfig.tradeRatioThreeLocation);
        tradeRatioTwo = new Graphic(iconGraphicSource, tradeRatioRenderMask, GraphicsConfig.tradeRatioTwoLocation);

        brickIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.brickIconTextureLocation);
        lumberIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.lumberIconTextureLocation);
        woolIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.woolIconTextureLocation);
        grainIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.grainIconTextureLocation);
        oreIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.oreIconTextureLocation);
        questionIconTexture = new Graphic(iconGraphicSource, resourceIconRenderMask, GraphicsConfig.questionIconTextureLocation);

        redSettlementLeft = new Graphic(redTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementLeftLocation);
        redSettlementRight = new Graphic(redTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementRightLocation);
        redCityLeft = new Graphic(redTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.cityLeftLocation);
        redCityRight = new Graphic(redTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.cityRightLocation);
        redHorizontalPath = new Graphic(redTeamGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        redDiagonalUpPath = new Graphic(redTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        redDiagonalDownPath = new Graphic(redTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        orangeSettlementLeft = new Graphic(orangeTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementRightLocation);
        orangeSettlementRight = new Graphic(orangeTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementLeftLocation);
        orangeCityLeft = new Graphic(orangeTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.cityLeftLocation);
        orangeCityRight = new Graphic(orangeTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.cityRightLocation);
        orangeHorizontalPath = new Graphic(orangeTeamGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        orangeDiagonalUpPath = new Graphic(orangeTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        orangeDiagonalDownPath = new Graphic(orangeTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        blueSettlementLeft = new Graphic(blueTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementLeftLocation);
        blueSettlementRight = new Graphic(blueTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementRightLocation);
        blueCityLeft = new Graphic(blueTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.cityLeftLocation);
        blueCityRight = new Graphic(blueTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.cityRightLocation);
        blueHorizontalPath = new Graphic(blueTeamGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        blueDiagonalUpPath = new Graphic(blueTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        blueDiagonalDownPath = new Graphic(blueTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        whiteSettlementLeft = new Graphic(whiteTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementLeftLocation);
        whiteSettlementRight = new Graphic(whiteTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementRightLocation);
        whiteCityLeft = new Graphic(whiteTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.cityLeftLocation);
        whiteCityRight = new Graphic(whiteTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.cityRightLocation);
        whiteHorizontalPath = new Graphic(whiteTeamGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        whiteDiagonalUpPath = new Graphic(whiteTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        whiteDiagonalDownPath = new Graphic(whiteTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        oceanVertexLeft = new Graphic(oceanGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementLeftLocation);
        oceanVertexRight = new Graphic(oceanGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementRightLocation);
        oceanHorizontalPath = new Graphic(oceanGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        oceanDiagonalUpPath = new Graphic(oceanGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        oceanDiagonalDownPath = new Graphic(oceanGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        blankVertexLeft = new Graphic(blankGraphicSource, vertexRenderMaskLeft, GraphicsConfig.settlementLeftLocation);
        blankVertexRight = new Graphic(blankGraphicSource, vertexRenderMaskRight, GraphicsConfig.settlementRightLocation);
        blankHorizontalPath = new Graphic(blankGraphicSource, horizontalRenderMask, GraphicsConfig.horizontalPathLocation);
        blankDiagonalUpPath = new Graphic(blankGraphicSource, diagonalUpRenderMask, GraphicsConfig.diagonalUpPathLocation);
        blankDiagonalDownPath = new Graphic(blankGraphicSource, diagonalDownRenderMask, GraphicsConfig.diagonalDownPathLocation);

        BASE_GAME = new GameType("Settlers of Catan Base");
        BASE_GAME.size(15, 11);
        BASE_GAME.post(7, 2);
        BASE_GAME.post(5, 3).tile(7, 3).post(9, 3);
        BASE_GAME.tile(5, 4).tile(6, 4).tile(7, 4).tile(8, 4).tile(9, 4);
        BASE_GAME.post(4, 5).tile(5, 5).tile(6, 5).tile(7, 5).tile(8, 5).tile(9, 5).post(10, 5);
        BASE_GAME.tile(5, 6).tile(6, 6).tile(7, 6).tile(8, 6).tile(9, 6);
        BASE_GAME.post(4, 7).tile(6, 7).tile(7, 7).tile(8, 7).post(10, 7);
        BASE_GAME.post(6, 8).post(8, 8);
        BASE_GAME.gen(new RandomBoardGenerator());
        BASE_GAME.players(3);
    }
}
