package catan.common.locale.game;

import catan.common.config.ConfigSource;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.util.GameResource;
import catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter responsible for printing trades between players.
 */
public class LocalizedTradePrinter extends LocalizedPrinter<Trade> {

    private final LocalizedEnumCounterPrinter<GameResource> counterPrinter;

    public LocalizedTradePrinter(ConfigSource locale) {
        super(locale.narrow("game.trade"));
        counterPrinter = new LocalizedEnumCounterPrinter<>(locale.narrow("game.resource"));
    }

    @Override
    public String getLocalization(Trade instance) {
        StringBuilder trade = new StringBuilder();
        String request = counterPrinter.getLocalization(instance.getRequest());
        if (request.isEmpty())
            request = getLocalization("nothing");
        trade.append(request);
        trade.append(' ');
        trade.append(getLocalization("for"));
        trade.append(' ');
        String offer = counterPrinter.getLocalization(instance.getOffer());
        if (offer.isEmpty())
            offer = getLocalization("nothing");
        trade.append(offer);
        trade.append(' ');
        trade.append(getLocalization("with"));
        trade.append(' ');
        if (instance.getSeller() != null) {
            trade.append(instance.getSeller().username);
        } else {
            trade.append(getLocalization("the"));
            trade.append(' ');
            trade.append(getLocalization("bank"));
        }
        return trade.toString();
    }
}
