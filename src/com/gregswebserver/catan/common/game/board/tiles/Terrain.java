package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.common.game.gameplay.enums.Resource;
import com.gregswebserver.catan.common.util.Statics;

/**
 * Created by Greg on 8/9/2014.
 * Enum containing terrain textures and resources.
 * Will be used to confer textures for rendering.
 */
public enum Terrain {

    Hill(Statics.hillTexture, Resource.Brick),
    Forest(Statics.forestTexture, Resource.Lumber),
    Pasture(Statics.pastureTexture, Resource.Wool),
    Mountain(Statics.mountainTexture, Resource.Ore),
    Field(Statics.fieldTexture, Resource.Grain),
    Desert(Statics.desertTexture, null);

    public final Graphic image;
    public final Resource resource;

    Terrain(Graphic image, Resource resource) {
        this.image = image;
        this.resource = resource;
    }

}
