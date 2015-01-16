package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.util.Direction;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;

/**
 * Created by Greg on 1/15/2015.
 * A background used to render a ui background, that is tiled to fit multiple sizes.
 */
public enum UIBackground {

    Blue(new GraphicInfo[]{UIBlueCenter, UIBlueUp, UIBlueDown, UIBlueLeft, UIBlueRight, UIBlueUpLeft, UIBlueUpRight, UIBlueDownLeft, UIBlueDownRight}),
    Green(new GraphicInfo[]{UIBlueCenter, UIBlueUp, UIBlueDown, UIBlueLeft, UIBlueRight, UIBlueUpLeft, UIBlueUpRight, UIBlueDownLeft, UIBlueDownRight});

    private GraphicInfo[] graphics;

    UIBackground(GraphicInfo[] graphics) {
        this.graphics = graphics;
    }

    public Graphic getGraphic(Direction d) {
        return ResourceLoader.getGraphic(graphics[d.ordinal()]);
    }
}
