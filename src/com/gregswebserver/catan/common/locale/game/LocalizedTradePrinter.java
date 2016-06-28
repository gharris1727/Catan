package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter resposible for printing trades between players.
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
        if (request.length() == 0)
            request = getLocalization("nothing");
        trade.append(request);
        trade.append(' ');
        trade.append(getLocalization("for"));
        trade.append(' ');
        String offer = counterPrinter.getLocalization(instance.getOffer());
        if (offer.length() == 0)
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
