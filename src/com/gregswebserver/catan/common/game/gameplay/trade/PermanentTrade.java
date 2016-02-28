package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;

/**
 * Created by greg on 2/27/16.
 * A trade between a player and the bank.
 */
public class PermanentTrade extends Trade {

    public PermanentTrade(GameResource source, GameResource target, int count) {
        request.increment(source, count);
        offer.increment(target, 1);
    }
}
