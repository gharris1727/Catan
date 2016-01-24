package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.GraphicSource;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.resources.GameInfo;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.client.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.game.gameplay.GameType;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public class ResourceLoader {

    //TODO: rename GameType to match the other Info schemas.
    private static final ResourceCache<GameInfo, GameType> gameCache = new ResourceCache<GameInfo, GameType>() {
        @Override
        protected GameType load(GameInfo info) throws ResourceLoadException {
            try {
                return new GameType(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<GraphicInfo, Graphic> graphicCache = new ResourceCache<GraphicInfo, Graphic>() {
        @Override
        protected Graphic load(GraphicInfo info) throws ResourceLoadException {
            try {
                GraphicSource s = getGraphicSource(info.getSource());
                RenderMask m = info.getMask();
                return new Graphic(s, m, info.getLocation());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<GraphicSourceInfo, GraphicSource> graphicSourceCache = new ResourceCache<GraphicSourceInfo, GraphicSource>() {
        @Override
        protected GraphicSource load(GraphicSourceInfo info) throws ResourceLoadException {
            try {
                return new GraphicSource(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    static {
        gameCache.clear();
        graphicCache.clear();
        graphicSourceCache.clear();
    }

    public static GameType getGame(GameInfo info) throws ResourceLoadException {
        return gameCache.get(info);
    }

    public static Graphic getGraphic(GraphicInfo info) throws ResourceLoadException {
        return graphicCache.get(info);
    }

    private static GraphicSource getGraphicSource(GraphicSourceInfo info) throws ResourceLoadException {
        return graphicSourceCache.get(info);
    }
}
