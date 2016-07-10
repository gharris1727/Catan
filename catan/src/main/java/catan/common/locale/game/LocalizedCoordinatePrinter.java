package catan.common.locale.game;

import catan.common.config.ConfigSource;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.locale.LocalizedPrinter;

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
