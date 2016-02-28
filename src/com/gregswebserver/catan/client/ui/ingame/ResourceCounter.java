package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.structure.game.EnumCounter;

import java.awt.*;

/**
 * Created by greg on 2/27/16.
 * A resource counting icon for use in multiple areas.
 */
public abstract class ResourceCounter extends UIScreenRegion {

    private static final GraphicSet icons;
    private static final int textSpacing;

    static {
        icons = new GraphicSet(Client.graphicsConfig, "interface.ingame.resource.icons", RectangularMask.class);
        textSpacing = Client.graphicsConfig.getInt("interface.ingame.resource.textspacing");
    }

    private final EnumCounter<GameResource> counter;
    private final GameResource gameResource;

    private final TiledBackground background;
    private final GraphicObject icon;
    protected final TextLabel count;

    protected ResourceCounter(int priority, EnumCounter<GameResource> counter, GameResource gameResource) {
        super(priority);
        this.counter = counter;
        this.gameResource = gameResource;
        setTransparency(true);
        background = new TiledBackground(0, UIStyle.BACKGROUND_TEXT) {
            @Override
            public String toString() {
                return "ResourceCounterBackground";
            }
        };
        icon = new GraphicObject(1,icons.getGraphic(gameResource.ordinal())) {
            @Override
            public String toString() {
                return "InventoryElementIcon";
            }
        };
        count = new TextLabel(2, UIStyle.FONT_HEADING, "0") {
            @Override
            public String toString() {
                return "InventoryElementCountText";
            }
        };
        add(background).setClickable(this);
        add(icon).setClickable(this);
        add(count).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        int current=counter.get(gameResource);
        count.setText("" + current);

        int iconHeight = icons.getMask().getHeight();
        int iconWidth = icons.getMask().getWidth();
        int textHeight = count.getGraphic().getMask().getHeight();

        setMask(new RectangularMask(new Dimension(iconWidth, iconHeight + textSpacing + textHeight)));

        center(count).y = iconHeight + textSpacing;
    }
}
