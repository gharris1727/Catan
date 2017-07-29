package catan.common.game.gameplay.generator.better;

import catan.common.game.board.GameBoard;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;

/**
 * Created by greg on 1/24/16.
 * Generator that creates a GameBoard using the BetterSettlers algorithm.
 */
public class BetterGenerator implements BoardGenerator {

    public BetterGenerator() {
    }

    @Override
    public GameBoard generate(BoardLayout layout, long seed) {
        //TODO: implement the BetterSettlers generation algorithm.
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public String toString() {
        return "BetterGenerator";
    }

    public boolean equals(Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }

    public int hashCode() {
        return 0;
    }
}
