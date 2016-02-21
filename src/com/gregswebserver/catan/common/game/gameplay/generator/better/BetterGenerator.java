package com.gregswebserver.catan.common.game.gameplay.generator.better;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;

/**
 * Created by greg on 1/24/16.
 * Generator that creates a GameBoard using the BetterSettlers algorithm.
 */
public class BetterGenerator implements BoardGenerator {

    public static final BetterGenerator instance = new BetterGenerator();

    private BetterGenerator() {
    }

    @Override
    public GameBoard generate(BoardLayout layout, long seed) {
        //TODO: implement the BetterSettlers generation algorithm.
        return new GameBoard(layout.getSize());
    }

    @Override
    public String toString() {
        return "BetterGenerator";
    }
}
