package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

import java.awt.*;

/**
 * Created by greg on 3/22/16.
 * Methods for controlling access to a source of configuration details.
 */
public interface ConfigSource {

    ConfigSource narrow(String prefix);

    String get(String key);

    int getInt(String key);

    double getDouble(String key);

    Color getColor(String key);

    int getHexColor(String key);

    Point getPoint(String key);

    Coordinate getCoord(String key);

    Dimension getDimension(String key);
}
