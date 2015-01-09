package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum Resource implements Tradeable, Graphical {

    Brick("Clay", ResourceLoader.getGraphic(ResourceBrick), ResourceLoader.getGraphic(IconBrick)),
    Lumber("Logs", ResourceLoader.getGraphic(ResourceLumber), ResourceLoader.getGraphic(IconLumber)),
    Wool("Sheep", ResourceLoader.getGraphic(ResourceWool), ResourceLoader.getGraphic(IconWool)),
    Grain("Wheat", ResourceLoader.getGraphic(ResourceGrain), ResourceLoader.getGraphic(IconGrain)),
    Ore("Rocks", ResourceLoader.getGraphic(ResourceOre), ResourceLoader.getGraphic(IconOre));

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
