package com.gregswebserver.catan.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.Graphical;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different achievement cards.
 */
public enum Achievement implements Graphical {

    LongestRoad("Longest Road", 2, Statics.longestRoadCardTexture),
    LargestArmy("Largest Army", 2, Statics.largestArmyCardTexture);

    private final String name;
    private final int vp;
    private final Graphic graphic;

    Achievement(String name, int vp, Graphic graphic) {
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
