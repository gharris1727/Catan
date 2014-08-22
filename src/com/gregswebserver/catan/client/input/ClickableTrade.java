package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.game.gameplay.trade.Trade;

/**
 * Created by Greg on 8/21/2014.
 * A clickable object
 */
public class ClickableTrade implements Clickable {

    //TODO: implement.
    private Trade trade;

    public ClickableTrade(Trade trade) {
        this.trade = trade;
    }

    @Override
    public GenericEvent onRightClick() {
        return null;
    }

    @Override
    public GenericEvent onLeftClick() {
        return null;
    }
}
