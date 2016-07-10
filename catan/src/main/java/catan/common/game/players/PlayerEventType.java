package catan.common.game.players;

import catan.common.event.EventType;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.util.EnumCounter;

/**
 * Created by greg on 5/24/16.
 * Types of events that can be applied to players.
 */
public enum PlayerEventType implements EventType {

    Gain_Resources(EnumCounter.class),
    Discard_Resources(EnumCounter.class),
    Lose_Resources(EnumCounter.class),
    Gain_DevelopmentCard(DevelopmentCard.class),
    Mature_DevelopmentCards(EnumCounter.class),
    Use_DevelopmentCard(DevelopmentCard.class),
    Offer_Trade(Trade.class),
    Use_Trade(Trade.class),
    Cancel_Trade(null),
    Finish_Discarding(null);

    private final Class payloadType;

    PlayerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
