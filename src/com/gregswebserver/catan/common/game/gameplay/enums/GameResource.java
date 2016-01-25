package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.resources.GraphicSet;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum GameResource implements Tradeable, Graphical {

    Brick("Clay"),
    Lumber("Logs"),
    Wool("Sheep"),
    Grain("Wheat"),
    Ore("Rocks"),
    Wildcard("Any");

    private static final GraphicSet graphics;
    private static final GraphicSet icons;

    static {
        graphics = new GraphicSet("catan.graphics.game.resource", RoundedRectangularMask.class);
        icons = new GraphicSet("catan.graphics.trade.icons", RectangularMask.class);
    }

    private final String name;

    GameResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Graphic getGraphic() {
        return graphics.getGraphic(ordinal());
    }

    public Graphic getIcon() {
        return icons.getGraphic(ordinal());
    }
}
