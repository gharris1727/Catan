package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;

/**
 * Created by Greg on 8/21/2014.
 * An inventory item on the screen that can be clicked on.
 */
public class ClickableInventoryItem implements Clickable {

    public ClickableInventoryItem() {

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
