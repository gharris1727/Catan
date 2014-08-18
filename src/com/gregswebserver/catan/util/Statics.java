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
 */
public class Statics {

    public static GraphicSource tileSpriteSheet;
    public static HexagonalMask tileRenderMask;
    public static TriangularMask vertexRenderMask;
    public static RectangularMask horizontalRenderMask;
    public static DiagonalUpMask diagonalUpRenderMask;
    public static DiagonalDownMask diagonalDownRenderMask;
    public static Graphic hillTexture;
    public static Graphic forestTexture;
    public static Graphic pastureTexture;
    public static Graphic mountainTexture;
    public static Graphic fieldTexture;
    public static Graphic desertTexture;
    public static Graphic oceanTexture;
    public static GameType BASE_GAME;

    public Statics() {
        //TODO: change all of this to represent the new sizes.
        tileSpriteSheet = GraphicSource.load("res/graphics/tiles.png");
        tileRenderMask = new HexagonalMask(112, 96);
        vertexRenderMask = new TriangularMask(16);
        horizontalRenderMask = new RectangularMask(64, 16);
        diagonalUpRenderMask = new DiagonalUpMask(52, 18);
        diagonalDownRenderMask = new DiagonalDownMask(52, 18);
        hillTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(12, 18), 0);
        forestTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(90, 97), 0);
        pastureTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(178, 16), 0);
        mountainTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(15, 185), 0);
        fieldTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(180, 184), 0);
        desertTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(99, 21), 0);
        oceanTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(183, 107), 0);

        //TODO: might want to increase the border size to offer more padding for scrolling.
        BASE_GAME = new GameType("Settlers of Catan Base");
        BASE_GAME.size(7, 7);
        BASE_GAME.post(0, 3);
        BASE_GAME.post(1, 1).tile(1, 3).post(1, 5);
        BASE_GAME.tile(2, 1).tile(2, 2).tile(2, 3).tile(2, 4).tile(2, 5);
        BASE_GAME.post(3, 0).tile(3, 1).tile(3, 2).tile(3, 3).tile(3, 4).tile(3, 5).post(3, 6);
        BASE_GAME.tile(4, 1).tile(4, 2).tile(4, 3).tile(4, 4).tile(4, 5);
        BASE_GAME.post(5, 0).tile(5, 2).tile(5, 3).tile(5, 4).post(5, 5);
        BASE_GAME.post(6, 2).post(6, 4);
        BASE_GAME.gen(new RandomBoardGenerator());
        BASE_GAME.players(3);
    }
}
