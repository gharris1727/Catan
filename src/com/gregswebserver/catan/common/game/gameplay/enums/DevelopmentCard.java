package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum DevelopmentCard implements Tradeable, Graphical {

    Knight("Knight", 1),
    VictoryPoint("Victory Point", 1),
    Monopoly("Monopoly", 0),
    YearOfPlenty("Year of Plenty", 0),
    RoadBuilding("Road Building", 0);

    private static final GraphicSet graphics;

    static {
        graphics = new GraphicSet("catan.graphics.game.development", RoundedRectangularMask.class);
    }

    private final String name;
    private final int vp;

    DevelopmentCard(String name, int vp) {
        this.name = name;
        this.vp = vp;
    }

    public String getName() {
        return name;
    }

    public int getVictoryPoints() {
        return vp;
    }

    @Override
    public Graphic getGraphic() {
        return graphics.getGraphic(ordinal());
    }
}
