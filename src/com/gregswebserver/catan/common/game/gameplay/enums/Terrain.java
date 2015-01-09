package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;

/**
 * Created by Greg on 8/9/2014.
 * Enum containing terrain textures and resources.
 * Will be used to confer textures for rendering.
 */
public enum Terrain {

    Hill(ResourceLoader.getGraphic(GraphicInfo.TileHill), Resource.Brick),
    Forest(ResourceLoader.getGraphic(GraphicInfo.TileForest), Resource.Lumber),
    Pasture(ResourceLoader.getGraphic(GraphicInfo.TilePasture), Resource.Wool),
    Mountain(ResourceLoader.getGraphic(GraphicInfo.TileMountain), Resource.Ore),
    Field(ResourceLoader.getGraphic(GraphicInfo.TileField), Resource.Grain),
    Desert(ResourceLoader.getGraphic(GraphicInfo.TileDesert), null);

    public final Graphic image;
    public final Resource resource;

    Terrain(Graphic image, Resource resource) {
        this.image = image;
        this.resource = resource;
    }

}
