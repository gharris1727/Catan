package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIStyle;
import com.gregswebserver.catan.client.ui.UIScreen;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.structure.game.Player;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends UIScreen {

    private final Player player;

    private final TiledBackground background;
    private final TextLabel username;
    private final Map<GameResource, ResourceCounter> elements;

    public InventoryRegion(UIScreen parent, Player player) {
        super(2, parent, "inventory");
        //Load layout information.
        Point elementOffset = getLayout().getPoint("element.offset");
        Point elementSpacing = getLayout().getPoint("element.spacing");
        //Store instance information.
        this.player = player;
        //Create sub-regions
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
        username = new TextLabel(1, UIStyle.FONT_HEADING, player.getName().username);
        elements = new EnumMap<>(GameResource.class);
        int index = 0;
        for (GameResource gameResource : GameResource.values()) {
            ResourceCounter element = new ResourceCounter(2, this, player.getInventory(), gameResource) {
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
            elements.put(gameResource, element);
            add(element).setClickable(this).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
            index++;
        }
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
        center(username).y = getLayout().getInt("username.position.y");
        elements.values().forEach(ResourceCounter::forceRender);
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
