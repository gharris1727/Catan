package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.Graphical;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum DevelopmentCard implements Tradeable, Graphical {

    Knight("Knight", 1, Statics.knightCardTexture),
    Progress("Progress Card", 0, Statics.progressCardTexture),
    VictoryPoint("Victory Point Card", 1, Statics.victoryPointCardTexture);

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
