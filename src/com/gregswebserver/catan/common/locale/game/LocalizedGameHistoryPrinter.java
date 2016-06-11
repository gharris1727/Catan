package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.event.GameHistory;
import com.gregswebserver.catan.common.game.event.GameTriggerEvent;
import com.gregswebserver.catan.common.locale.LocalizedEventPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * A LocalePrinter for printing GameHistory instances.
 */
public class LocalizedGameHistoryPrinter extends LocalizedPrinter<GameHistory> {

    private final LocalizedEventPrinter eventPrinter;
    private final LocalizedGameTriggerEventPrinter triggerPrinter;

    public LocalizedGameHistoryPrinter(ConfigSource locale) {
        super(locale);
        eventPrinter = new LocalizedEventPrinter(locale, "game.event");
        triggerPrinter = new LocalizedGameTriggerEventPrinter(locale);
    }

    @Override
    public String getLocalization(GameHistory instance) {
        StringBuilder output = new StringBuilder();
        output.append(eventPrinter.getLocalization(instance.getGameEvent()));
        output.append('\n');
        output.append("Triggered Events:\n");
        for (GameTriggerEvent event : instance.getTriggeredEvents()) {
            String triggerLocalization = triggerPrinter.getLocalization(event);
            if (triggerLocalization != null) {
                output.append('\t');
                output.append(triggerLocalization);
                output.append('\n');
            }
        }
        return output.toString();
    }
}
