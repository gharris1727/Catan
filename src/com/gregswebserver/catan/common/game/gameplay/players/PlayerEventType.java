package com.gregswebserver.catan.common.game.gameplay.players;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.gameplay.enums.DevelopmentCard;
import com.gregswebserver.catan.common.game.gameplay.enums.Purchase;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.structure.game.EnumCounter;

/**
 * Created by greg on 5/24/16.
 * Types of events that can be applied to players.
 */
public enum PlayerEventType implements EventType {

    Gain_Resources(EnumCounter.class),
    Make_Purchase(Purchase.class),
    Gain_DevelopmentCard(DevelopmentCard.class),
    Mature_DevelopmentCards(EnumCounter.class),
    Use_DevelopmentCard(DevelopmentCard.class),
    Offer_Trade(TemporaryTrade.class),
    Fill_Trade(TemporaryTrade.class),
    Make_Trade(Trade.class);

    private final Class payloadType;

    PlayerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
