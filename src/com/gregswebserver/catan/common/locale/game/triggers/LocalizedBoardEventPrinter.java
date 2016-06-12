package com.gregswebserver.catan.common.locale.game.triggers;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.board.BoardEvent;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.locale.LocalizedEnumPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;
import com.gregswebserver.catan.common.locale.game.LocalizedCoordinatePrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter for printing changes to the GameBoard.
 */
public class LocalizedBoardEventPrinter extends LocalizedPrinter<BoardEvent> {

    private final LocalizedEnumPrinter typePrinter;
    private final LocalizedCoordinatePrinter coordinatePrinter;

    public LocalizedBoardEventPrinter(ConfigSource locale) {
        super(locale);
        typePrinter = new LocalizedEnumPrinter(locale.narrow("game.board"));
        coordinatePrinter = new LocalizedCoordinatePrinter(locale);
    }

    @Override
    public String getLocalization(BoardEvent instance) {
        return typePrinter.getLocalization(instance.getType()) + " " +
            coordinatePrinter.getLocalization((Coordinate) instance.getPayload());
    }
}
