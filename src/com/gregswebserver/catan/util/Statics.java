package com.gregswebserver.catan.util;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.GraphicSource;
import com.gregswebserver.catan.client.masks.*;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.game.gameplay.generator.RandomBoardGenerator;

/**
 * Created by Greg on 8/15/2014.
 * Class full of static objects that need to be loaded.
 * Everything in here should require loading outside resources and take a significant amount of time to load.
 */
public class Statics {

    public static GraphicSource tileGraphicSource;
    public static GraphicSource oceanGraphicSource;
    public static GraphicSource blankGraphicSource;

    public static GraphicSource redTeamGraphicSource;
    public static GraphicSource orangeTeamGraphicSource;
    public static GraphicSource blueTeamGraphicSource;
    public static GraphicSource whiteTeamGraphicSource;

    public static RenderMask tileRenderMask;
    public static RenderMask vertexRenderMaskLeft;
    public static RenderMask vertexRenderMaskRight;
    public static RenderMask horizontalRenderMask;
    public static RenderMask diagonalUpRenderMask;
    public static RenderMask diagonalDownRenderMask;

    public static Graphic hillTexture;
    public static Graphic forestTexture;
    public static Graphic pastureTexture;
    public static Graphic mountainTexture;
    public static Graphic fieldTexture;
    public static Graphic desertTexture;
    public static Graphic oceanTexture;

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
        tileGraphicSource = GraphicSource.load(GraphicsConfig.tileGraphicSourcePath);
        oceanGraphicSource = GraphicSource.load(GraphicsConfig.oceanGraphicSourcePath);
        blankGraphicSource = GraphicSource.load(GraphicsConfig.blankGraphicSourcePath);

        redTeamGraphicSource = GraphicSource.load(GraphicsConfig.redTeamGraphicSourcePath);
        orangeTeamGraphicSource = GraphicSource.load(GraphicsConfig.orangeTeamGraphicSourcePath);
        blueTeamGraphicSource = GraphicSource.load(GraphicsConfig.blueTeamGraphicSourcePath);
        whiteTeamGraphicSource = GraphicSource.load(GraphicsConfig.whiteTeamGraphicSourcePath);

        tileRenderMask = new HexagonalMask(GraphicsConfig.tileRenderMaskSize);
        vertexRenderMaskLeft = new TriangularMask(GraphicsConfig.vertexRenderMaskSize);
        vertexRenderMaskRight = new FlippedMask(vertexRenderMaskLeft, FlippedMask.HORIZONTAL);
        horizontalRenderMask = new RectangularMask(GraphicsConfig.horizontalRenderMaskSize);
        diagonalUpRenderMask = new DiagonalUpMask(GraphicsConfig.diagonalUpRenderMaskSize);
        diagonalDownRenderMask = new DiagonalDownMask(GraphicsConfig.diagonalDownRenderMaskSize);

        hillTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.hillTextureLocation, 0);
        forestTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.forestTextureLocation, 0);
        pastureTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.pastureTextureLocation, 0);
        mountainTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.mountainTextureLocation, 0);
        fieldTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.fieldTextureLocation, 0);
        desertTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.desertTextureLocation, 0);
        oceanTexture = new Graphic(tileGraphicSource, tileRenderMask, GraphicsConfig.oceanTextureLocation, 0);

        //TODO: beach textures.

        redSettlementLeft = new Graphic(redTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.redSettlementLocationLeft, 0);
        redSettlementRight = new Graphic(redTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.redSettlementLocationRight, 0);
        redCityLeft = new Graphic(redTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.redCityLocationLeft, 0);
        redCityRight = new Graphic(redTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.redCityLocationRight, 0);
        redHorizontalPath = new Graphic(redTeamGraphicSource, horizontalRenderMask, GraphicsConfig.redHorizontalPathLocation, 0);
        redDiagonalUpPath = new Graphic(redTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.redDiagonalUpPathLocation, 0);
        redDiagonalDownPath = new Graphic(redTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.redDiagonalDownPathLocation, 0);

        orangeSettlementLeft = new Graphic(orangeTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.orangeSettlementLocationRight, 0);
        orangeSettlementRight = new Graphic(orangeTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.orangeSettlementLocationLeft, 0);
        orangeCityLeft = new Graphic(orangeTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.orangeCityLocationLeft, 0);
        orangeCityRight = new Graphic(orangeTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.orangeCityLocationRight, 0);
        orangeHorizontalPath = new Graphic(orangeTeamGraphicSource, horizontalRenderMask, GraphicsConfig.orangeHorizontalPathLocation, 0);
        orangeDiagonalUpPath = new Graphic(orangeTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.orangeDiagonalUpPathLocation, 0);
        orangeDiagonalDownPath = new Graphic(orangeTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.orangeDiagonalDownPathLocation, 0);

        blueSettlementLeft = new Graphic(blueTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.blueSettlementLocationLeft, 0);
        blueSettlementRight = new Graphic(blueTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.blueSettlementLocationRight, 0);
        blueCityLeft = new Graphic(blueTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.blueCityLocationLeft, 0);
        blueCityRight = new Graphic(blueTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.blueCityLocationRight, 0);
        blueHorizontalPath = new Graphic(blueTeamGraphicSource, horizontalRenderMask, GraphicsConfig.blueHorizontalPathLocation, 0);
        blueDiagonalUpPath = new Graphic(blueTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.blueDiagonalUpPathLocation, 0);
        blueDiagonalDownPath = new Graphic(blueTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.blueDiagonalDownPathLocation, 0);

        whiteSettlementLeft = new Graphic(whiteTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.whiteSettlementLocationLeft, 0);
        whiteSettlementRight = new Graphic(whiteTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.whiteSettlementLocationRight, 0);
        whiteCityLeft = new Graphic(whiteTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.whiteCityLocationLeft, 0);
        whiteCityRight = new Graphic(whiteTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.whiteCityLocationRight, 0);
        whiteHorizontalPath = new Graphic(whiteTeamGraphicSource, horizontalRenderMask, GraphicsConfig.whiteHorizontalPathLocation, 0);
        whiteDiagonalUpPath = new Graphic(whiteTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.whiteDiagonalUpPathLocation, 0);
        whiteDiagonalDownPath = new Graphic(whiteTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.whiteDiagonalDownPathLocation, 0);

        oceanVertexLeft = new Graphic(blueTeamGraphicSource, vertexRenderMaskLeft, GraphicsConfig.oceanVertexLocationLeft, 0);
        oceanVertexRight = new Graphic(blueTeamGraphicSource, vertexRenderMaskRight, GraphicsConfig.oceanVertexLocationRight, 0);
        oceanHorizontalPath = new Graphic(blueTeamGraphicSource, horizontalRenderMask, GraphicsConfig.oceanHorizontalPathLocation, 0);
        oceanDiagonalUpPath = new Graphic(blueTeamGraphicSource, diagonalUpRenderMask, GraphicsConfig.oceanDiagonalUpPathLocation, 0);
        oceanDiagonalDownPath = new Graphic(blueTeamGraphicSource, diagonalDownRenderMask, GraphicsConfig.oceanDiagonalDownPathLocation, 0);

        blankVertexLeft = new Graphic(blankGraphicSource, vertexRenderMaskLeft, GraphicsConfig.blankVertexLocationLeft, 0);
        blankVertexRight = new Graphic(blankGraphicSource, vertexRenderMaskRight, GraphicsConfig.blankVertexLocationRight, 0);
        blankHorizontalPath = new Graphic(blankGraphicSource, horizontalRenderMask, GraphicsConfig.blankHorizontalPathLocation, 0);
        blankDiagonalUpPath = new Graphic(blankGraphicSource, diagonalUpRenderMask, GraphicsConfig.blankDiagonalUpPathLocation, 0);
        blankDiagonalDownPath = new Graphic(blankGraphicSource, diagonalDownRenderMask, GraphicsConfig.blankDiagonalDownPathLocation, 0);

        //TODO: might want to increase the border size to offer more padding for scrolling.
        BASE_GAME = new GameType("Settlers of Catan Base");
        BASE_GAME.size(7, 7);
        BASE_GAME.post(3, 0);
        BASE_GAME.post(1, 1).tile(3, 1).post(5, 1);
        BASE_GAME.tile(1, 2).tile(2, 2).tile(3, 2).tile(4, 2).tile(5, 2);
        BASE_GAME.post(0, 3).tile(1, 3).tile(2, 3).tile(3, 3).tile(4, 3).tile(5, 3).post(6, 3);
        BASE_GAME.tile(1, 4).tile(2, 4).tile(3, 4).tile(4, 4).tile(5, 4);
        BASE_GAME.post(0, 5).tile(2, 5).tile(3, 5).tile(4, 5).post(5, 5);
        BASE_GAME.post(2, 6).post(4, 6);
        BASE_GAME.gen(new RandomBoardGenerator());
        BASE_GAME.players(3);
    }
}
