package catan.common.locale.game.triggers;

import catan.common.config.ConfigSource;
import catan.common.game.gamestate.GameStateEvent;
import catan.common.locale.LocalizedEnumPrinter;
import catan.common.locale.LocalizedPrinter;


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
            case Advance_Theft:
            case Draw_DevelopmentCard:
            case Advance_Turn:
            case Active_Turn:
                return null;
        }
        return null;
    }
}
