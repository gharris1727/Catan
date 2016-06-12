package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.locale.LocalizedEnumPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

import java.util.Map;

/**
 * Created by greg on 6/11/16.
 * Printer to localize the EnumCounter class.
 */
public class LocalizedEnumCounterPrinter extends LocalizedPrinter<EnumCounter<?>> {

    private final LocalizedEnumPrinter typePrinter;

    public LocalizedEnumCounterPrinter(ConfigSource locale) {
        super(locale);
        typePrinter = new LocalizedEnumPrinter(locale);
    }

    @Override
    public String getLocalization(EnumCounter<?> instance) {
        StringBuilder out = new StringBuilder();
        String delimiter = ", ";
        for (Map.Entry<?, Integer> entry : instance) {
            if (entry.getValue() != 0) {
                out.append(entry.getValue());
                out.append(" ");
                out.append(typePrinter.getLocalization((Enum) entry.getKey()));
                out.append(delimiter);
            }
        }
        if (out.length() > delimiter.length())
            out.setLength(out.length() - delimiter.length());
        return out.toString();
    }
}
