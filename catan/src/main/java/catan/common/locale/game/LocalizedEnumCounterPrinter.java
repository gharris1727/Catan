package catan.common.locale.game;

import catan.common.config.ConfigSource;
import catan.common.game.util.EnumCounter;
import catan.common.locale.LocalizedEnumPrinter;
import catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * Printer to localize the EnumAccumulator class.
 */
public class LocalizedEnumCounterPrinter<T extends Enum<T>> extends LocalizedPrinter<EnumCounter<T>> {

    private final LocalizedEnumPrinter typePrinter;

    public LocalizedEnumCounterPrinter(ConfigSource locale) {
        super(locale);
        typePrinter = new LocalizedEnumPrinter(locale);
    }

    @Override
    public String getLocalization(EnumCounter<T> instance) {
        StringBuilder out = new StringBuilder();
        String delimiter = ", ";
        for (T e : instance) {
            if (instance.get(e) != 0) {
                out.append(instance.get(e));
                out.append(" ");
                out.append(typePrinter.getLocalization(e));
                out.append(delimiter);
            }
        }
        if (out.length() > delimiter.length())
            out.setLength(out.length() - delimiter.length());
        return out.toString();
    }
}
