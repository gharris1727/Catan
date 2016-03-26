package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.structure.game.Player;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends ConfigurableScreenRegion {

    private final Player player;

    private final TiledBackground background;
    private final TextLabel username;

    public InventoryRegion(Player player) {
        super(2, "inventory");
        //Store instance information.
        this.player = player;
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        username = new TextLabel(1, "username", player.getName().username);
        //Add everything to the screen.
        add(background).setClickable(this);
        add(username).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        clear();
        Point elementOffset = getConfig().getLayout().getPoint("element.offset");
        Point elementSpacing = getConfig().getLayout().getPoint("element.spacing");
        int index = 0;
        for (GameResource gameResource : GameResource.values()) {
            ResourceCounter element = new ResourceCounter(2, player.getInventory(), gameResource) {
                @Override
                public String toString() {
                    return "InventoryResourceCounter";
                }

                @Override
                protected void renderContents() {
                    int current = player.getInventory().get(gameResource);
                    count.setText("" + current);
                    super.renderContents();
                }
            };
            add(element).setClickable(this).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
            element.setConfig(getConfig());
            index++;
        }
        //Add everything to the screen.
        add(background).setClickable(this);
        add(username).setClickable(this);
        center(username).y = getConfig().getLayout().getInt("username.position.y");
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
