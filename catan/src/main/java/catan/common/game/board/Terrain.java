package catan.common.game.board;

import catan.common.game.util.GameResource;

/**
 * Created by Greg on 8/9/2014.
 * Enum containing terrain textures and resources.
 * Will be used to confer textures for rendering.
 */
public enum Terrain {

    Hill(GameResource.Brick),
    Forest(GameResource.Lumber),
    Pasture(GameResource.Wool),
    Mountain(GameResource.Ore),
    Field(GameResource.Grain),
    Desert(null);

    public final GameResource gameResource;

    Terrain(GameResource gameResource) {
        this.gameResource = gameResource;
    }

}
