package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.game.gameplay.enums.Resource;
import com.gregswebserver.catan.graphics.HexagonalSprite;

/**
 * Created by Greg on 8/9/2014.
 * Enum containing terrain textures and resources.
 * Will be used to confer textures for rendering.
 */
public enum Terrain {

    //TODO: add all of the hexagonal sprites.
    Hill(null, Resource.Brick),
    Forest(null, Resource.Lumber),
    Pasture(null, Resource.Wool),
    Mountain(null, Resource.Ore),
    Field(null, Resource.Grain),
    Desert(null, null),
    Ocean(null, null),
    SingleBeach(null, null),
    DoubleBeach(null, null);

    public final HexagonalSprite sprite;
    public final Resource resource;

    Terrain(HexagonalSprite sprite, Resource resource) {
        this.sprite = sprite;
        this.resource = resource;
    }

}
