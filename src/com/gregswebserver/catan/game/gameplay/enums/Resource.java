package com.gregswebserver.catan.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum Resource implements Tradeable {

    Brick("Clay", Statics.brickCardTexture),
    Lumber("Logs", Statics.lumberCardTexture),
    Wool("Sheep", Statics.woolCardTexture),
    Grain("Wheat", Statics.grainCardTexture),
    Ore("Rocks", Statics.oreCardTexture);

    private final String name;
    private final Graphic graphic;

    Resource(String name, Graphic graphic) {
        this.name = name;
        this.graphic = graphic;
    }

    public String getName() {
        return name;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
