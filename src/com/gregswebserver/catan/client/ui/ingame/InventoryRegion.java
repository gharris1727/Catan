package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.structure.game.Player;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends UIScreenRegion {

    private static final Point usernamePosition;
    private static final Point elementOffset;
    private static final Point elementSpacing;

    static {
        usernamePosition = Client.graphicsConfig.getPoint("interface.ingame.inventory.username.position");
        elementOffset = Client.graphicsConfig.getPoint("interface.ingame.inventory.element.offset");
        elementSpacing = Client.graphicsConfig.getPoint("interface.ingame.inventory.element.spacing");
    }

    private final Player player;

    private final TiledBackground background;
    private final TextLabel username;
    private final Map<GameResource, ResourceCounter> elements;

    public InventoryRegion(int priority, Player player) {
        super(priority);
        this.player = player;
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "ContextRegionBackground";
            }
        };
        username = new TextLabel(1, UIStyle.FONT_HEADING, player.getName().username) {
            @Override
            public String toString() {
                return "InventoryUsernameLabel";
            }
        };
        elements = new EnumMap<>(GameResource.class);
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
            elements.put(gameResource, element);
            add(element).setClickable(this).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
            index++;
        }
        add(background).setClickable(this);
        add(username).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        center(username).y = usernamePosition.y;
        elements.values().forEach(ResourceCounter::forceRender);
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
