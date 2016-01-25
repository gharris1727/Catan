package com.gregswebserver.catan.common.game.board.layout;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;

import java.awt.*;
import java.util.Iterator;


/**
 * Created by greg on 1/24/16.
 * A board layout containing information about the locations of game tiles, map size, and trading posts.
 */
public interface BoardLayout {

    String getName();

    Dimension getSize();

    Coordinate getRobber();

    int getTileCount();

    int getDesertCount();

    int getPortCount();

    Iterator<Coordinate> getTiles();

    Iterator<Coordinate> getPorts();

    Iterator<Terrain> getTerrain();

    Iterator<TradingPostType> getPosts();

    Iterator<DiceRoll> getRolls();
}
