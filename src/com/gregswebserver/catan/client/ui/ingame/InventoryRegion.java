package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends UIScreenRegion {

    private static final Point usernamePosition;
    private static final Dimension elementSize;
    private static final Point elementIconPosition;
    private static final Point elementCountPosition;
    private static final Point elementOffset;
    private static final Point elementSpacing;
    private static final GraphicSet icons;

    static {
        usernamePosition = Client.staticConfig.getPoint("catan.graphics.interface.ingame.inventory.username.position");
        elementSize = Client.staticConfig.getDimension("catan.graphics.interface.ingame.inventory.element.size");
        elementIconPosition = Client.staticConfig.getPoint("catan.graphics.interface.ingame.inventory.element.icon.position");
        elementCountPosition = Client.staticConfig.getPoint("catan.graphics.interface.ingame.inventory.element.count.position");
        elementOffset = Client.staticConfig.getPoint("catan.graphics.interface.ingame.inventory.element.offset");
        elementSpacing = Client.staticConfig.getPoint("catan.graphics.interface.ingame.inventory.element.spacing");
        icons = new GraphicSet("catan.graphics.interface.ingame.inventory.icons", RectangularMask.class);
    }

    private final Player player;

    private final TiledBackground background;
    private final TextLabel username;
    private final Map<GameResource, InventoryElement> elements;

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
        for (GameResource t : player.getInventory().keySet()) {
            InventoryElement element = new InventoryElement(t);
            elements.put(t, element);
            int column = 2 * index / player.getInventory().size();
            add(element).setClickable(this).setPosition(new Point(
                    elementOffset.x + column * elementSpacing.x,
                    elementOffset.y + (index % 3) * elementSpacing.y));
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
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }

    private class InventoryElement extends UIScreenRegion {

        private final GameResource gameResource;
        private int current;

        private final GraphicObject icon;
        private final TextLabel count;

        private InventoryElement(GameResource gameResource) {
            super(2);
            setTransparency(true);
            this.gameResource = gameResource;
            current = 0;
            int index = gameResource.ordinal();
            icon = new GraphicObject(0,icons.getGraphic(index)) {
                @Override
                public String toString() {
                    return "InventoryElementIcon";
                }
            };
            count = new TextLabel(1, UIStyle.FONT_PARAGRAPH, "0") {
                @Override
                public String toString() {
                    return "InventoryElementCountText";
                }
            };
            add(icon).setClickable(this).setPosition(elementIconPosition);
            add(count).setClickable(this).setPosition(elementCountPosition);
            setMask(new RectangularMask(elementSize));
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }

        @Override
        protected void renderContents() {
            current = player.getInventory().get(gameResource);
            count.setText("" + current);
        }

        @Override
        public String toString() {
            return "InventoryElement";
        }
    }
}
