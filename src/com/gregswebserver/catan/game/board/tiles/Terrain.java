package com.gregswebserver.catan.game.board.tiles;

import com.gregswebserver.catan.game.cards.Resource;

/**
 * Created by Greg on 8/9/2014.
 * Enum containing terrain names and resources earned.
 */
public enum Terrain {

    Hill(Resource.Brick),
    Forest(Resource.Lumber),
    Pasture(Resource.Wool),
    Mountain(Resource.Ore),
    Field(Resource.Grain),
    Desert(null),
    Ocean(null);

    private Resource resource;

    Terrain(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
