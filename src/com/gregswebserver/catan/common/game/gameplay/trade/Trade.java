package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.game.util.UnmodifiableEnumCounter;

import java.io.Serializable;

/**
 * Created by Greg on 8/13/2014.
 * Trading framework with sub-types of trading.
 */
public class Trade implements Comparable<Trade>, Serializable {

    private final Username seller;
    private final EnumCounter<GameResource> offer;
    private final EnumCounter<GameResource> request;

    public Trade(Username seller, EnumCounter<GameResource> offer, EnumCounter<GameResource> request) {
        this.seller = seller;
        this.offer = new UnmodifiableEnumCounter<>(offer);
        this.request = new UnmodifiableEnumCounter<>(request);
    }

    public Trade(Trade other) {
        this(other.seller,
            new EnumAccumulator<>(GameResource.class, other.offer),
            new EnumAccumulator<>(GameResource.class, other.request));
    }

    public Trade(GameResource source, GameResource target, int count) {
        this(null,
            new EnumAccumulator<>(GameResource.class, target),
            new EnumAccumulator<>(GameResource.class, source, count));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (seller != null ? !seller.equals(trade.seller) : trade.seller != null) return false;
        if (!offer.equals(trade.offer)) return false;
        return request.equals(trade.request);

    }

    @Override
    public int hashCode() {
        int result = seller != null ? seller.hashCode() : 0;
        result = 31 * result + offer.hashCode();
        result = 31 * result + request.hashCode();
        return result;
    }

    @Override
    public int compareTo(Trade trade) {
        if (seller == null) {
            if (trade.seller != null) {
                return -1;
            } else {
                for (GameResource r : GameResource.values()) {
                    int diff = trade.offer.get(r) - offer.get(r);
                    if (diff != 0) return diff;
                    diff = trade.request.get(r) - request.get(r);
                    if (diff != 0) return diff;
                }
                return 0;
            }
        } else {
            if (trade.seller == null) {
                return 1;
            } else {
                return seller.username.compareTo(trade.seller.username);
            }
        }
    }

    @Override
    public String toString() {
        String out = "Trade(" + seller + "/";
        for (GameResource r : GameResource.values()) {
            out += request.get(r) + "-" + offer.get(r) + "/";
        }
        return out + ")";
    }

    public Username getSeller() {
        return seller;
    }

    public EnumCounter<GameResource> getOffer() {
        return offer;
    }

    public EnumCounter<GameResource> getRequest() {
        return request;
    }
}
