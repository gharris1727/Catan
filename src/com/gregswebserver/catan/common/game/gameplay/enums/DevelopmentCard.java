package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.common.resources.cached.GraphicInfo.*;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum DevelopmentCard implements Tradeable, Graphical {

    Knight("Knight", 1, ResourceLoader.getGraphic(DevelopmentKnight)),
    VictoryPoint("Victory Point", 1, ResourceLoader.getGraphic(DevelopmentVictoryPoint)),
    Monopoly("Monopoly", 0, ResourceLoader.getGraphic(DevelopmentMonopoly)),
    YearOfPlenty("Year of Plenty", 0, ResourceLoader.getGraphic(DevelopmentYearOfPlenty)),
    RoadBuilding("Road Building", 0, ResourceLoader.getGraphic(DevelopmentRoadBuilding));


    private final String name;
    private final int vp;
    private final Graphic graphic;

    DevelopmentCard(String name, int vp, Graphic graphic) {
        this.name = name;
        this.vp = vp;
        this.graphic = graphic;
    }

    public String getName() {
        return name;
    }

    public int getVictoryPoints() {
        return vp;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
