package com.gregswebserver.catan.common.game.gameplay.generator.copy;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;

import java.util.Iterator;

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
        GameBoard board = new GameBoard(layout.getSize());
        Iterator<Coordinate> tiles = layout.getTiles();
        Iterator<Coordinate> ports = layout.getPorts();
        Iterator<Terrain> terrain = layout.getTerrain();
        Iterator<DiceRoll> rolls = layout.getRolls();
        Iterator<TradingPostType> posts = layout.getPosts();
        //Place all of the resource tiles
        while (tiles.hasNext() && terrain.hasNext() && rolls.hasNext())
            setResourceTile(board, tiles.next(), new ResourceTile(terrain.next(), rolls.next()));
        //Generate the beaches.
        generateBeachTiles(board);
        //Replace the beaches with trading posts.
        while(ports.hasNext() && posts.hasNext())
            setTradingPost(board, ports.next(), posts.next());
        return board;
    }

    @Override
    public String toString() {
        return "CopyGenerator";
    }
}
