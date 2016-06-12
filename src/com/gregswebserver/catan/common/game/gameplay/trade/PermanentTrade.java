package com.gregswebserver.catan.common.game.gameplay.trade;

import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.GameResource;

/**
 * Created by greg on 2/27/16.
 * A trade between a player and the bank.
 */
public class PermanentTrade extends Trade {

    public PermanentTrade(GameResource source, GameResource target, int count) {
        super(new EnumAccumulator<>(GameResource.class, target),
            new EnumAccumulator<>(GameResource.class, source, count));
    }
}
