package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.structure.game.EnumCounter;

import java.awt.*;

/**
 * Created by greg on 2/27/16.
 * A resource counting icon for use in multiple areas.
 */
public abstract class ResourceCounter extends UIScreen {

    private final GraphicSet icons;
    private final int textSpacing;

    static {
    }

    private final EnumCounter<GameResource> counter;
    private final GameResource gameResource;

    private final TiledBackground background;
    private final GraphicObject icon;
    protected final TextLabel count;

    protected ResourceCounter(int priority, UIScreen parent, EnumCounter<GameResource> counter, GameResource gameResource) {
        super(priority, parent, "resource");
        //Load layout information
        icons = new GraphicSet(getLayout(), "icons", RectangularMask.class, null);
        textSpacing = getLayout().getInt("textspacing");
        //Store instance information
        this.counter = counter;
        this.gameResource = gameResource;
        //Create sub-regions
        setTransparency(true);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INVENTORY);
        icon = new GraphicObject(1,icons.getGraphic(gameResource.ordinal())) {
            @Override
            public String toString() {
                return "InventoryElementIcon";
            }
        };
        count = new TextLabel(2, UIStyle.FONT_INVENTORY, "0");
        //Add everything to the screen.
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
        setMask(icons.getMask());
        count.setPosition(new Point(0, icons.getMask().getHeight() - count.getGraphic().getMask().getHeight()));
    }
}
