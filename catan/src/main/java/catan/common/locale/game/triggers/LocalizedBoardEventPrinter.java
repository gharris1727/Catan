package catan.common.locale.game.triggers;

import catan.common.config.ConfigSource;
import catan.common.game.board.BoardEvent;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.locale.LocalizedEnumPrinter;
import catan.common.locale.LocalizedPrinter;
import catan.common.locale.game.LocalizedCoordinatePrinter;

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
