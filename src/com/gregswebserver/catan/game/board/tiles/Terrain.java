package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.game.gameplay.enums.Resource;
import com.gregswebserver.catan.util.Statics;

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
    Desert(Statics.desertTexture, null),
    Ocean(Statics.oceanTexture, null),
    //TODO: add beach sprites.
    SingleBeach(Statics.oceanTexture, null),
    DoubleBeach(Statics.oceanTexture, null);

    public final Graphic image;
    public final Resource resource;

    Terrain(Graphic image, Resource resource) {
        this.image = image;
        this.resource = resource;
    }

}
