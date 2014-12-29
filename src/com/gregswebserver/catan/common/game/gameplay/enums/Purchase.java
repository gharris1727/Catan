package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;

/**
 * Created by Greg on 8/13/2014.
 * Enum of purchase-able items for use in the trading framework.
 */
public enum Purchase implements Tradeable {

    Road,
    Settlement,
    City,
    DevelopmentCard
}