package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.io.Serializable;

/**
 * Created by Greg on 8/13/2014.
 * Trading framework with sub-types of trading.
 */
public abstract class Trade implements Comparable<Trade>, Serializable{

    private final EnumCounter<GameResource> offer;
    private final EnumCounter<GameResource> request;

    public Trade(EnumCounter<GameResource> offer, EnumCounter<GameResource> request) {
        this.offer = offer;
        this.request = request;
    }

    protected Trade(Trade other) {
        this.offer = new EnumAccumulator<>(GameResource.class, other.offer);
        this.request = new EnumAccumulator<>(GameResource.class, other.request);
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

    public EnumCounter<GameResource> getOffer() {
        return offer;
    }

    public EnumCounter<GameResource> getRequest() {
        return request;
    }
}
