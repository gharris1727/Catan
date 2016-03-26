package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.GraphicSource;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.config.PropertiesFile;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.layout.DynamicBoardLayout;
import com.gregswebserver.catan.common.game.gameplay.layout.StaticBoardLayout;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public class ResourceLoader {

    private static final ResourceCache<BoardLayoutInfo, BoardLayout> boardLayoutCache = new ResourceCache<BoardLayoutInfo, BoardLayout>() {
        @Override
        protected BoardLayout load(BoardLayoutInfo info) throws ResourceLoadException {
            try {
                if (info.isDynamic())
                    return new DynamicBoardLayout(info.getSeed());
                else
                    return new StaticBoardLayout(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<GameRulesInfo, GameRules> gameRuleSetCache = new ResourceCache<GameRulesInfo, GameRules>() {
        @Override
        protected GameRules load(GameRulesInfo info) throws ResourceLoadException {
            try {
                return new GameRules(info.getPath());
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
                Graphic graphic = new Graphic(s, m, info.getLocation(), true);
                int[] swaps = info.getSwaps();
                if (swaps != null)
                    for (int i = 0; i < swaps.length; i +=2 )
                        graphic.swap(swaps[i],swaps[i+1]);
                return graphic;
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

    private static final ResourceCache<PropertiesFileInfo, PropertiesFile> propertiesFileCache = new ResourceCache<PropertiesFileInfo, PropertiesFile>() {
        @Override
        protected PropertiesFile load(PropertiesFileInfo info) throws ResourceLoadException {
            try {
                return new PropertiesFile(info.getPath(), info.getComment());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }

        @Override
        public void save(PropertiesFileInfo info) throws ResourceLoadException {
            try {
                PropertiesFile file = get(info);
                file.close();
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    static {
        boardLayoutCache.clear();
        gameRuleSetCache.clear();
        graphicCache.clear();
        graphicSourceCache.clear();
        propertiesFileCache.clear();
    }

    public static BoardLayout getBoardLayout(BoardLayoutInfo info) throws ResourceLoadException {
        return boardLayoutCache.get(info);
    }

    public static Graphic getGraphic(GraphicInfo info) throws ResourceLoadException {
        return graphicCache.get(info);
    }

    private static GraphicSource getGraphicSource(GraphicSourceInfo info) throws ResourceLoadException {
        return graphicSourceCache.get(info);
    }

    public static GameRules getGameRuleSet(GameRulesInfo info) throws ResourceLoadException{
        return gameRuleSetCache.get(info);
    }

    public static PropertiesFile getPropertiesFile(PropertiesFileInfo info) throws ResourceLoadException {
        return propertiesFileCache.get(info);
    }

    public static void savePropertiesFile(PropertiesFileInfo info) throws ResourceLoadException {
        propertiesFileCache.save(info);
    }
}
