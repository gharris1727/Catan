package catan.common.game.gameplay.layout;

import catan.common.game.board.Terrain;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;


/**
 * Created by greg on 1/24/16.
 * A board layout containing information about the locations of game tiles, map size, and trading posts.
 */
public interface BoardLayout extends Serializable {

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
