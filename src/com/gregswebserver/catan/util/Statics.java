package com.gregswebserver.catan.util;

import com.gregswebserver.catan.client.graphics.*;
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
        tileSpriteSheet = GraphicSource.load("res/graphics/tiles.png");
        tileRenderMask = new HexagonalMask(56, 48);
        vertexRenderMask = new TriangularMask(8);
        horizontalRenderMask = new RectangularMask(32, 8);
        diagonalUpRenderMask = new DiagonalUpMask(26, 9);
        diagonalDownRenderMask = new DiagonalDownMask(26, 9);
        hillTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(12, 18));
        forestTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(90, 97));
        pastureTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(178, 16));
        mountainTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(15, 185));
        fieldTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(180, 184));
        desertTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(99, 21));
        oceanTexture = new Graphic(tileSpriteSheet, tileRenderMask, new Point(183, 107));

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
