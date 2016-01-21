package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends ScreenRegion {

    private Player player;

    public InventoryRegion(int priority, Player player) {
        super(priority);
        this.player = player;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //TODO: handle resizing the inventory
    }

    @Override
    protected void renderContents() {
        //TODO: handle rendering the inventory screen.
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
