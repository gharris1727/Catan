package com.gregswebserver.catan.common.game.board.layout;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by greg on 1/24/16.
 * A board layout that is dynamically generated from a procgen seed.
 */
public class DynamicBoardLayout implements BoardLayout {

    public DynamicBoardLayout(long seed) {
        //TODO: explore procedurally generating game boards.
        throw new RuntimeException("Unimplemented");
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
}
