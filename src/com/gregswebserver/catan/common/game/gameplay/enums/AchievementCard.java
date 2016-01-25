package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.common.resources.GraphicSet;
/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different achievement cards.
 */
public enum AchievementCard implements Graphical {


    //TODO: insert the correct descriptions.
    LongestRoad("Longest Road", "Description for Longest Road"),
    LargestArmy("Largest Army", "Description for Largest Army");

    private static final GraphicSet graphics;

    static {
        graphics = new GraphicSet("catan.graphics.game.achievement", RectangularMask.class);
    }

    private final String name;
    private final String desc;

    AchievementCard(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    @Override
    public Graphic getGraphic() {
        return graphics.getGraphic(ordinal());
    }
}
