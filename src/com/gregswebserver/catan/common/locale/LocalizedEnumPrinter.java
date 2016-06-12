package com.gregswebserver.catan.common.locale;

import com.gregswebserver.catan.common.config.ConfigSource;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter that simply localizes by the name of the enum.
 */
public class LocalizedEnumPrinter extends LocalizedPrinter<Enum> {

    public LocalizedEnumPrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(Enum instance) {
        return getLocalization(instance.toString());
    }
}
