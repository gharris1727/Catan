package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.structure.game.EnumCounter;

import java.io.Serializable;

/**
 * Created by Greg on 8/13/2014.
 * Trading framework with sub-types of trading.
 */
public abstract class Trade implements Comparable<Trade>, Serializable{

    public final EnumCounter<GameResource> offer;
    public final EnumCounter<GameResource> request;

    public Trade() {
        offer = new EnumCounter<>(GameResource.class);
        request = new EnumCounter<>(GameResource.class);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Trade))
            return false;
        Trade t = (Trade) o;
        return offer.equals(t.offer) && request.equals(t.request);
    }

    public int hashCode() {
        return offer.hashCode() + request.hashCode();
    }

    @Override
    public int compareTo(Trade trade) {
        if (trade instanceof TemporaryTrade)
            return -1;
        for (GameResource r : GameResource.values()) {
            int diff = trade.offer.get(r) - offer.get(r);
            if (diff != 0) return diff;
            diff = trade.request.get(r) - request.get(r);
            if (diff != 0) return diff;
        }
        return 0;
    }

    @Override
    public String toString() {
        String out = "Trade(/";
        for (GameResource r : GameResource.values()) {
            out += request.get(r) + "-" + offer.get(r) + "/";
        }
        return out + ")";
    }
}
