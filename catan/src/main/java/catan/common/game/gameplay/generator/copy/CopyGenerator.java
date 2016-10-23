package catan.common.game.gameplay.generator.copy;

import catan.common.game.board.GameBoard;
import catan.common.game.board.Terrain;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.hexarray.HexagonalArray;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;

import java.awt.*;
import java.util.*;

/**
 * Created by greg on 1/24/16.
 * Generator that uses a static configuration from an external file to copy a pre-built map.
 */
public class CopyGenerator implements BoardGenerator {

    public static final CopyGenerator instance = new CopyGenerator();

    private CopyGenerator() {
    }

    @Override
    public GameBoard generate(BoardLayout layout, long seed) {
        Dimension size = layout.getSize();
        HexagonalArray hexArray = new HexagonalArray(size.width, size.height);
        Map<DiceRoll, Set<Coordinate>> diceRolls = new EnumMap<>(DiceRoll.class);
        Set<Coordinate> tradingPosts = new HashSet<>();
        Iterator<Coordinate> tiles = layout.getTiles();
        Iterator<Coordinate> ports = layout.getPorts();
        Iterator<Terrain> terrain = layout.getTerrain();
        Iterator<DiceRoll> rolls = layout.getRolls();
        Iterator<TradingPostType> posts = layout.getPosts();
        //Place all of the resource tiles
        while (tiles.hasNext() && terrain.hasNext() && rolls.hasNext())
            setResourceTile(hexArray, diceRolls, tiles.next(), new ResourceTile(terrain.next(), rolls.next()));
        //Generate the beaches.
        generateBeachTiles(hexArray);
        //Replace the beaches with trading posts.
        while(ports.hasNext() && posts.hasNext())
            setTradingPost(hexArray, tradingPosts, ports.next(), posts.next());
        ((ResourceTile) hexArray.getTile(layout.getRobber())).placeRobber();
        return new GameBoard(size, hexArray, diceRolls, tradingPosts, layout.getRobber());
    }

    @Override
    public String toString() {
        return "CopyGenerator";
    }
}
