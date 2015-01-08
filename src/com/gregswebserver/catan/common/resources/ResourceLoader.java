package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.GraphicSource;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.resources.cached.FontInfo;
import com.gregswebserver.catan.common.resources.cached.GameInfo;
import com.gregswebserver.catan.common.resources.cached.GraphicInfo;
import com.gregswebserver.catan.common.resources.cached.GraphicSourceInfo;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public class ResourceLoader {

    private static final Object lock = new Object();
    private static final HashMap<FontInfo, Font> fontCache = new HashMap<>();
    private static final HashMap<GameInfo, GameType> gameCache = new HashMap<>();
    private static final HashMap<GraphicInfo, Graphic> graphicCache = new HashMap<>();
    private static final HashMap<GraphicSourceInfo, GraphicSource> graphicSourceCache = new HashMap<>();
    private static Logger logger;
    private static int resourcesLoaded = 0;

    public ResourceLoader(Logger log) {
        logger = log;
    }

    public static Font getFont(FontInfo info) {
        if (!fontCache.containsKey(info))
            loadFont(info);
        return fontCache.get(info);
    }

    private static void loadFont(FontInfo info) {
        synchronized (info) {
            synchronized (lock) {
                //TODO: font loading.
            }
        }
    }

    public static GameType getGame(GameInfo info) {
        if (!gameCache.containsKey(info))
            loadGame(info);
        return gameCache.get(info);
    }

    private static void loadGame(GameInfo info) {
        synchronized (info) {
            try {
                GameType o = new GameType(info.getPath());
                synchronized (lock) {
                    gameCache.put(info, o);
                    resourcesLoaded++;
                }
            } catch (ResourceLoadException e) {
                logger.log(e, LogLevel.ERROR);
            }
        }
    }

    public static Graphic getGraphic(GraphicInfo info) {
        if (!graphicCache.containsKey(info))
            loadGraphic(info);
        return graphicCache.get(info);
    }

    private static void loadGraphic(GraphicInfo info) {
        synchronized (info) {
            try {
                Graphic s = getGraphicSource(info.getSource());
                RenderMask m = info.getMask();
                Graphic o = new Graphic(s, m, info.getLocation());
                synchronized (lock) {
                    graphicCache.put(info, o);
                    resourcesLoaded++;
                }
            } catch (ResourceLoadException e) {
                logger.log(e, LogLevel.ERROR);
                synchronized (lock) {
                    graphicCache.put(info, new Graphic(new Dimension(1, 1)));
                }
            }
        }
    }

    private static GraphicSource getGraphicSource(GraphicSourceInfo info) throws ResourceLoadException {
        if (!graphicSourceCache.containsKey(info))
            loadGraphicSource(info);
        return graphicSourceCache.get(info);
    }

    private static void loadGraphicSource(GraphicSourceInfo info) throws ResourceLoadException {
        synchronized (info) {
            GraphicSource o = new GraphicSource(info.getPath());
            synchronized (lock) {
                graphicSourceCache.put(info, o);
                resourcesLoaded++;
            }
        }
    }
}
