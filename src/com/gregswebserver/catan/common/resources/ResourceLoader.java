package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.GraphicSource;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.TextStyle;
import com.gregswebserver.catan.client.resources.GameInfo;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.client.resources.GraphicSourceInfo;
import com.gregswebserver.catan.client.resources.TextStyleInfo;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.server.resources.ObjectStoreInfo;

import java.io.*;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public class ResourceLoader {

    private static final ResourceCache<TextStyleInfo, TextStyle> fontCache = new ResourceCache<TextStyleInfo, TextStyle>() {
        @Override
        protected TextStyle load(TextStyleInfo textStyleInfo) throws ResourceLoadException {
            return new TextStyle(textStyleInfo.getName(), textStyleInfo.getStyle(), textStyleInfo.getSize(), textStyleInfo.getColor());
        }
    };

    //TODO: rename GameType to match the other Info schemas.
    private static final ResourceCache<GameInfo, GameType> gameCache = new ResourceCache<GameInfo, GameType>() {
        @Override
        protected GameType load(GameInfo gameInfo) throws ResourceLoadException {
            return new GameType(gameInfo.getPath());
        }
    };

    private static final ResourceCache<GraphicInfo, Graphic> graphicCache = new ResourceCache<GraphicInfo, Graphic>() {
        @Override
        protected Graphic load(GraphicInfo graphicInfo) throws ResourceLoadException {
            GraphicSource s = getGraphicSource(graphicInfo.getSource());
            RenderMask m = graphicInfo.getMask();
            return new Graphic(s, m, graphicInfo.getLocation());
        }
    };

    private static final ResourceCache<GraphicSourceInfo, GraphicSource> graphicSourceCache = new ResourceCache<GraphicSourceInfo, GraphicSource>() {
        @Override
        protected GraphicSource load(GraphicSourceInfo info) throws ResourceLoadException {
            return new GraphicSource(info.getPath());
        }
    };

    private static final ResourceCache<ObjectStoreInfo, Object> objectStoreCache = new ResourceCache<ObjectStoreInfo, Object>() {
        @Override
        protected Object load(ObjectStoreInfo objectStoreInfo) throws ResourceLoadException {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(objectStoreInfo.getPath()));
                Object o = stream.readObject();
                stream.close();
                return o;
            } catch (IOException | ClassNotFoundException e) {
                throw new ResourceLoadException(e);
            }
        }

        @Override
        public void save(ObjectStoreInfo objectStoreInfo) throws ResourceLoadException {
            try {
                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(objectStoreInfo.getPath()));
                stream.writeObject(getObjectStore(objectStoreInfo));
                stream.close();
            } catch (IOException e) {
                throw new ResourceLoadException(e);
            }
        }
    };

    public ResourceLoader() {
        fontCache.clear();
        gameCache.clear();
        graphicCache.clear();
        graphicSourceCache.clear();
        objectStoreCache.clear();
    }

    public static TextStyle getTextStyle(TextStyleInfo info) throws ResourceLoadException {
        return fontCache.get(info);
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

    public static Object getObjectStore(ObjectStoreInfo info) throws ResourceLoadException {
        return objectStoreCache.get(info);
    }

    public static void saveObjectStore(ObjectStoreInfo info) throws ResourceLoadException {
        objectStoreCache.save(info);
    }
}
