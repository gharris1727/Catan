package com.gregswebserver.catan.common.game.gameplay.generator.better;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;

/**
 * Created by greg on 1/24/16.
 * Generator that creates a GameBoard using the BetterSettlers algorithm.
 */
public class BetterGenerator implements BoardGenerator {

    @Override
    public GameBoard generate(BoardLayout layout) {
        //TODO: implement the BetterSettlers generation algorithm.
        return new GameBoard(layout.getSize());
    }
}