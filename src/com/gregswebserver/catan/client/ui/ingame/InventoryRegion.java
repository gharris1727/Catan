package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.player.Player;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends UIScreenRegion {

    private final Player player;

    private final TiledBackground background;

    public InventoryRegion(int priority, Player player) {
        super(priority);
        this.player = player;
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_GAME) {
            @Override
            public String toString() {
                return "ContextRegionBackground";
            }
        };
        add(background);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
