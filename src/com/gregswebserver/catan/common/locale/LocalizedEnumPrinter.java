package com.gregswebserver.catan.common.locale;

import com.gregswebserver.catan.common.config.ConfigSource;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter that simply localizes by the name of the enum.
 */
public class LocalizedEnumPrinter extends LocalizedPrinter<Enum> {

    private final String key;

    public LocalizedEnumPrinter(ConfigSource locale, String key) {
        super(locale);
        this.key = key;
    }

    @Override
    public String getLocalization(Enum instance) {
        return getLocalization(key + "." + instance);
    }
}
