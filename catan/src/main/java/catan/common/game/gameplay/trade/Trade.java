package catan.common.game.gameplay.trade;

import catan.common.crypto.Username;
import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;
import catan.common.game.util.UnmodifiableEnumCounter;

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
        if ((o == null) || (getClass() != o.getClass())) return false;

        Trade trade = (Trade) o;

        if ((seller != null) ? !seller.equals(trade.seller) : (trade.seller != null)) return false;
        if (!offer.equals(trade.offer)) return false;
        return request.equals(trade.request);

    }

    @Override
    public int hashCode() {
        int result = (seller != null) ? seller.hashCode() : 0;
        result = (31 * result) + offer.hashCode();
        result = (31 * result) + request.hashCode();
        return result;
    }

    @Override
    public int compareTo(Trade t) {
        if (seller == null) {
            if (t.seller != null) {
                return -1;
            } else {
                for (GameResource r : GameResource.values()) {
                    int diff = Integer.compare(t.offer.get(r), offer.get(r));
                    if (diff != 0) return diff;
                    diff = Integer.compare(t.request.get(r), request.get(r));
                    if (diff != 0) return diff;
                }
                return 0;
            }
        } else {
            return t.seller == null ? 1 : seller.username.compareTo(t.seller.username);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Trade(" + seller + "/");
        for (GameResource r : GameResource.values()) {
            out.append(request.get(r));
            out.append("-");
            out.append(offer.get(r));
            out.append("/");
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
