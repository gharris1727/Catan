package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum Resource implements Tradeable {

    Brick("Clay", Statics.brickCardTexture, Statics.brickIconTexture),
    Lumber("Logs", Statics.lumberCardTexture, Statics.lumberIconTexture),
    Wool("Sheep", Statics.woolCardTexture, Statics.woolIconTexture),
    Grain("Wheat", Statics.grainCardTexture, Statics.grainIconTexture),
    Ore("Rocks", Statics.oreCardTexture, Statics.oreIconTexture);

    private final String name;
    private final Graphic graphic;
    private final Graphic icon;

    Resource(String name, Graphic graphic, Graphic icon) {
        this.name = name;
        this.graphic = graphic;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public Graphic getIcon() {
        return icon;
    }
}
