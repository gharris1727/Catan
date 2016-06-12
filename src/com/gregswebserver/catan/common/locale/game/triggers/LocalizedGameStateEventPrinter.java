package com.gregswebserver.catan.common.locale.game.triggers;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gamestate.GameStateEvent;
import com.gregswebserver.catan.common.locale.LocalizedEnumPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter to print changes that a GameStateEvent entails.
 */
public class LocalizedGameStateEventPrinter extends LocalizedPrinter<GameStateEvent> {

    private final LocalizedEnumPrinter typePrinter;
    private final LocalizedEnumPrinter diceRollPrinter;

    public LocalizedGameStateEventPrinter(ConfigSource locale) {
        super(locale);
        typePrinter = new LocalizedEnumPrinter(locale.narrow("game.state"));
        diceRollPrinter = new LocalizedEnumPrinter(locale.narrow("game.rolls"));
    }

    @Override
    public String getLocalization(GameStateEvent instance) {
        String out = typePrinter.getLocalization(instance.getType()) + " ";
        switch (instance.getType()) {
            case Roll_Dice:
                return out + diceRollPrinter.getLocalization((Enum) instance.getPayload());
            case Draw_DevelopmentCard:
            case Advance_Turn:
            case Active_Turn:
                return null;
        }
        return null;
    }
}
