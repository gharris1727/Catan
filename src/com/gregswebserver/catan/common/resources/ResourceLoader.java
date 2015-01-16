package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.GraphicSource;
import com.gregswebserver.catan.client.resources.FontInfo;
import com.gregswebserver.catan.client.resources.GameInfo;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.client.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.server.resources.ObjectStoreInfo;

import java.awt.*;
import java.io.*;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public class ResourceLoader {

    private static final ResourceCache<FontInfo, Font> fontCache = new ResourceCache<FontInfo, Font>() {
        protected Font load(FontInfo fontInfo) throws ResourceLoadException {
            return new Font(fontInfo.getName(), fontInfo.getStyle(), fontInfo.getSize());
        }
    };

    private static final ResourceCache<GameInfo, GameType> gameCache = new ResourceCache<GameInfo, GameType>() {
        protected GameType load(GameInfo gameInfo) throws ResourceLoadException {
            return new GameType(gameInfo.getPath());
        }
    };

    private static final ResourceCache<GraphicInfo, Graphic> graphicCache = new ResourceCache<GraphicInfo, Graphic>() {
        protected Graphic load(GraphicInfo graphicInfo) throws ResourceLoadException {
            GraphicSource s = getGraphicSource(graphicInfo.getSource());
            RenderMask m = graphicInfo.getMask();
            return new Graphic(s, m, graphicInfo.getLocation());
        }
    };

    private static final ResourceCache<GraphicSourceInfo, GraphicSource> graphicSourceCache = new ResourceCache<GraphicSourceInfo, GraphicSource>() {
        protected GraphicSource load(GraphicSourceInfo info) throws ResourceLoadException {
            return new GraphicSource(info.getPath());
        }
    };

    private static final ResourceCache<ObjectStoreInfo, Object> objectStoreCache = new ResourceCache<ObjectStoreInfo, Object>() {
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

    public static Font getFont(FontInfo info) throws ResourceLoadException {
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
