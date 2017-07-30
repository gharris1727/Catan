package catan.common.resources;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.graphics.GraphicSource;
import catan.client.graphics.masks.RenderMask;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.gameplay.layout.DynamicBoardLayout;
import catan.common.game.gameplay.layout.StaticBoardLayout;
import catan.common.game.scoring.rules.GameRules;
import catan.common.game.scoring.rules.StaticGameRules;

/**
 * Created by Greg on 1/7/2015.
 * A construct for loading various resources located on the disk.
 */
public final class ResourceLoader {

    private static final ResourceCache<BoardLayoutInfo, BoardLayout> boardLayoutCache = new ResourceCache<BoardLayoutInfo, BoardLayout>() {
        @Override
        protected synchronized BoardLayout load(BoardLayoutInfo info) {
            try {
                return info.isDynamic() ? new DynamicBoardLayout(info.getSeed()) : new StaticBoardLayout(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<GameRulesInfo, GameRules> gameRuleSetCache = new ResourceCache<GameRulesInfo, GameRules>() {
        @Override
        protected synchronized GameRules load(GameRulesInfo info) {
            try {
                return new StaticGameRules(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<GraphicInfo, Graphic> graphicCache = new ResourceCache<GraphicInfo, Graphic>() {
        @Override
        protected synchronized Graphic load(GraphicInfo info) {
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
        protected synchronized GraphicSource load(GraphicSourceInfo info) {
            try {
                return new GraphicSource(info.getPath());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }
    };

    private static final ResourceCache<PropertiesFileInfo, PropertiesFile> propertiesFileCache = new ResourceCache<PropertiesFileInfo, PropertiesFile>() {
        @Override
        protected synchronized PropertiesFile load(PropertiesFileInfo info) {
            try {
                return new PropertiesFile(info.getPath(), info.getComment());
            } catch (Exception e) {
                throw new ResourceLoadException(info.toString(), e);
            }
        }

        @Override
        public synchronized void save(PropertiesFileInfo info) {
            try {
                PropertiesFile file = get(info);
                file.save();
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

    private ResourceLoader() {
    }

    public static BoardLayout getBoardLayout(BoardLayoutInfo info) {
        return boardLayoutCache.get(info);
    }

    public static Graphic getGraphic(GraphicInfo info) {
        return graphicCache.get(info);
    }

    private static GraphicSource getGraphicSource(GraphicSourceInfo info) {
        return graphicSourceCache.get(info);
    }

    public static GameRules getGameRuleSet(GameRulesInfo info) {
        return gameRuleSetCache.get(info);
    }

    public static PropertiesFile getPropertiesFile(PropertiesFileInfo info) {
        return propertiesFileCache.get(info);
    }

    public static void savePropertiesFile(PropertiesFileInfo info) {
        propertiesFileCache.save(info);
    }
}
