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
public abstract class ResourceCounter extends ConfigurableScreenRegion {

    private GraphicSet icons;
    
    private final EnumCounter<GameResource> counter;
    private final GameResource gameResource;

    private final TiledBackground background;
    private final GraphicObject icon;
    protected final TextLabel count;

    protected ResourceCounter(int priority, EnumCounter<GameResource> counter, GameResource gameResource) {
        super(priority, "resource");
        //Store instance information
        this.counter = counter;
        this.gameResource = gameResource;
        //Create sub-regions
        setTransparency(true);
        background = new EdgedTiledBackground(0, "background");
        icon = new GraphicObject(1, null) {
            @Override
            public String toString() {
                return "InventoryElementIcon";
            }
        };
        count = new TextLabel(2, "count", "0");
        //Add everything to the screen.
        add(background).setClickable(this);
        add(icon).setClickable(this);
        add(count).setClickable(this);
    }

    @Override
    public void loadConfig(UIConfig config) {
        icons = new GraphicSet(config.getLayout(), "icons", RectangularMask.class, null);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        setMask(icons.getMask());
        assertRenderable();
        icon.setGraphic(icons.getGraphic(gameResource.ordinal()));
        int current=counter.get(gameResource);
        count.setText("" + current);
        count.setPosition(new Point(0, icons.getMask().getHeight() - count.getGraphic().getMask().getHeight()));
    }
}
