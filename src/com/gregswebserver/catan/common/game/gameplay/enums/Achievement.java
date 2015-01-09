package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.game.gameplay.VictoryFactor;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.client.resources.GraphicInfo.AchievementArmy;
import static com.gregswebserver.catan.client.resources.GraphicInfo.AchievementRoad;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different achievement cards.
 */
public enum Achievement implements Graphical, VictoryFactor {


    //TODO: insert the correct descriptions.
    LongestRoad("Longest Road", "Description for Longest Road", ResourceLoader.getGraphic(AchievementRoad)),
    LargestArmy("Largest Army", "Description for Largest Army", ResourceLoader.getGraphic(AchievementArmy));

    private final String name;
    private final String desc;
    private final Graphic graphic;

    Achievement(String name, String desc, Graphic graphic) {
        this.name = name;
        this.desc = desc;
        this.graphic = graphic;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public int getVictoryPoints() {
        return 2;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
