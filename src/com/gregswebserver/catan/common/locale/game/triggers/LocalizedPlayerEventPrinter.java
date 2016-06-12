package com.gregswebserver.catan.common.locale.game.triggers;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.players.PlayerEvent;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.locale.LocalizedEnumPrinter;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;
import com.gregswebserver.catan.common.locale.game.LocalizedEnumCounterPrinter;

/**
 * Created by greg on 6/11/16.
 * A LocalizedPrinter for printing events that are applied to players.
 */
public class LocalizedPlayerEventPrinter extends LocalizedPrinter<PlayerEvent> {

    private final LocalizedEnumCounterPrinter<GameResource> resourcePrinter;
    private final LocalizedEnumPrinter developmentPrinter;

    public LocalizedPlayerEventPrinter(ConfigSource locale) {
        super(locale.narrow("game.player"));
        resourcePrinter = new LocalizedEnumCounterPrinter<>(locale.narrow("game.resource"));
        developmentPrinter = new LocalizedEnumPrinter(locale.narrow("game.development"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getLocalization(PlayerEvent instance) {
        String out = instance.getOrigin().username;
        out += " ";
        out += getLocalization(instance.getType().toString());
        switch (instance.getType()) {
            case Gain_Resources:
            case Lose_Resources:
                return out + ": " + resourcePrinter.getLocalization((EnumCounter<GameResource>) instance.getPayload());
            case Use_DevelopmentCard:
                return out + ": " + developmentPrinter.getLocalization((DevelopmentCard) instance.getPayload());
            case Offer_Trade:
            case Cancel_Trade:
                return out;
            case Gain_DevelopmentCard:
            case Mature_DevelopmentCards:
                return null;
        }
        return null;
    }
}
