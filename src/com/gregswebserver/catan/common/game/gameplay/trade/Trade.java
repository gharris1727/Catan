package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 8/13/2014.
 * Trading framework with sub-types of trading.
 */
public class Trade {

    public final HashMap<GameResource, Integer> offer;
    public final HashMap<GameResource, Integer> request;

    public Trade() {
        offer = new HashMap<>();
        request = new HashMap<>();
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Trade))
            return false;
        Trade t = (Trade) o;
        if (!mapEquals(t.offer, offer))
            return false;
        return mapEquals(t.request, request);
    }

    public boolean mapEquals(Map<?, ?> a, Map<?, ?> b) {
        if (a.size() != b.size())
            return false;
        for (Map.Entry entry : a.entrySet()) {
            if (!b.containsKey(entry.getKey()))
                return false;
            if (!b.get(entry.getKey()).equals(entry.getValue()))
                return false;
        }
        for (Map.Entry entry : b.entrySet()) {
            if (!a.containsKey(entry.getKey()))
                return false;
            if (!a.get(entry.getKey()).equals(entry.getValue()))
                return false;
        }
        return true;
    }

    public int hashCode() {
        return offer.hashCode() + request.hashCode();
    }
}
