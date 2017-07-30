package catan.common.game.gameplay.layout;

import catan.common.game.board.Terrain;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by greg on 1/24/16.
 * A board layout that is procedurally generated from a seed.
 */
public class DynamicBoardLayout implements BoardLayout {

    private final long seed;

    public DynamicBoardLayout(long seed) {
        this.seed = seed;
        //TODO: explore procedurally generating game boards.
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Coordinate getRobber() {
        return null;
    }

    @Override
    public int getTileCount() {
        return 0;
    }

    @Override
    public int getDesertCount() {
        return 0;
    }

    @Override
    public int getPortCount() {
        return 0;
    }

    @Override
    public Iterator<Coordinate> getTiles() {
        return null;
    }

    @Override
    public Iterator<Coordinate> getPorts() {
        return null;
    }

    @Override
    public Iterator<Terrain> getTerrain() {
        return null;
    }

    @Override
    public Iterator<TradingPostType> getPosts() {
        return null;
    }

    @Override
    public Iterator<DiceRoll> getRolls() {
        return null;
    }

    @Override
    public String toString() {
        return "DynamicBoardLayout(" + seed + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        DynamicBoardLayout other = (DynamicBoardLayout) o;

        return seed == other.seed;
    }

    @Override
    public int hashCode() {
        return (int) (seed ^ (seed >>> 32));
    }
}
