package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by greg on 2/27/16.
 * Class representing a trade between two players.
 */
public class TemporaryTrade extends Trade {

    public final Username seller;

    public TemporaryTrade(Username seller) {
        this.seller = seller;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof TemporaryTrade) {
            TemporaryTrade other = (TemporaryTrade) o;
            return seller.equals(other.seller) && super.equals(other);
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() * 31 + seller.hashCode();
    }

    @Override
    public int compareTo(Trade trade) {
        if (!(trade instanceof TemporaryTrade))
            return 1;
        TemporaryTrade t = (TemporaryTrade) trade;
        int diff = seller.username.compareTo(t.seller.username);
        if (diff != 0) return diff;
        return super.compareTo(trade);
    }
}
