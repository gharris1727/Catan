package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;

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

    private final String name;

    GameResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Graphic getGraphic() {
        return GraphicSet.ResourceCard.getGraphic(ordinal());
    }

    public Graphic getIcon() {
        return GraphicSet.TradeIcons.getGraphic(ordinal());
    }
}
