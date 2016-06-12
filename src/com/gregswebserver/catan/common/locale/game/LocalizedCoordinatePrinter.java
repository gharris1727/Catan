package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter for Coordinate objects.
 */
public class LocalizedCoordinatePrinter extends LocalizedPrinter<Coordinate> {

    public LocalizedCoordinatePrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(Coordinate instance) {
        return "(" + instance.getX() + "," + instance.getY() + ")";
    }
}
