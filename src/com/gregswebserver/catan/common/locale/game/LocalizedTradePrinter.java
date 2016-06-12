package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter resposible for printing trades between players.
 */
public class LocalizedTradePrinter extends LocalizedPrinter<Trade> {

    private final LocalizedEnumCounterPrinter counterPrinter;

    public LocalizedTradePrinter(ConfigSource locale) {
        super(locale.narrow("game.trade"));
        counterPrinter = new LocalizedEnumCounterPrinter(locale.narrow("game.resource"));
    }

    @Override
    public String getLocalization(Trade instance) {
        StringBuilder trade = new StringBuilder();
        String request = counterPrinter.getLocalization(instance.request);
        if (request.length() == 0)
            request = getLocalization("nothing");
        trade.append(request);
        trade.append(' ');
        trade.append(getLocalization("for"));
        trade.append(' ');
        String offer = counterPrinter.getLocalization(instance.offer);
        if (offer.length() == 0)
            offer = getLocalization("nothing");
        trade.append(offer);
        if (instance instanceof TemporaryTrade) {
            trade.append(' ');
            trade.append(getLocalization("with"));
            trade.append(' ');
            trade.append(((TemporaryTrade) instance).seller.username);
        }
        return trade.toString();
    }
}
