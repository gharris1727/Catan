package com.gregswebserver.catan.common.locale;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.event.TypedPayloadEvent;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter that simply localizes the type name of a TypedPayloadEvent.
 */
public class LocalizedEventPrinter extends LocalizedPrinter<TypedPayloadEvent<?,?>> {

    public LocalizedEventPrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(TypedPayloadEvent<?, ?> instance) {
        return getLocalization(instance.getType().toString());
    }
}