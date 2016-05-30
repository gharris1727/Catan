package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 5/29/16.
 * A localized printer for game events.
 */
public class LocalizedGameEventPrinter extends LocalizedPrinter<GameEvent> {

    public LocalizedGameEventPrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(GameEvent instance) {
        return getLocalization("game.event." + instance.getType());
    }
}
