package com.gregswebserver.catan.client.input.clickables;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

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

    public void onRightClick() {

    }

    public void onLeftClick() {

    }

    public void onMiddleClick() {

    }
}
