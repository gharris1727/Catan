package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends UIScreenRegion {

    private static final GraphicSet graphics;

    private static final Point offset;
    private static final Point spacing;
    private static final int titleheight;
    private static final int detailheight;

    static {
        graphics = new GraphicSet("catan.graphics.interface.ingame.context.icons", RectangularMask.class);
        offset = Client.staticConfig.getPoint("catan.graphics.interface.ingame.context.offset");
        spacing = Client.staticConfig.getPoint("catan.graphics.interface.ingame.context.spacing");
        titleheight = Client.staticConfig.getInt("catan.graphics.interface.ingame.context.title.y");
        detailheight = Client.staticConfig.getInt("catan.graphics.interface.ingame.context.detail.y");
    }

    private final CatanGame game;
    private final TiledBackground background;
    private final TextLabel title;
    private final TextLabel detail;
    private Object target;

    public ContextRegion(int priority, CatanGame game) {
        super(priority);
        this.game = game;
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "ContextRegionBackground";
            }
        };
        title = new TextLabel(1, UIStyle.FONT_HEADING, "") {
            @Override
            public String toString() {
                return "ContextMenuTitleText";
            }
        };
        detail = new TextLabel(2, UIStyle.FONT_PARAGRAPH, "") {
            @Override
            public String toString() {
                return "ContextMenuDetailText";
            }
        };
        add(background).setClickable(this);
        add(title).setClickable(this);
        add(detail).setClickable(this);
    }

    @Override
    protected void renderContents() {
        //Clear the context region of everything
        clear();
        //If we already clicked on something, update and see if it changed.
        if (target instanceof Tile)
            target = game.getBoard().getTile(((Tile) target).getPosition());
        else if (target instanceof Path)
            target = game.getBoard().getPath(((Path) target).getPosition());
        else if (target instanceof Town)
            target = game.getBoard().getTown(((Town) target).getPosition());
        //Clear the text so that if we unselected, it all goes blank.
        title.setText("");
        detail.setText("");
        if (game.isLocalPlayerActive()) {
            add(new ContextButton(2, 0) {
                @Override
                public String toString() {
                    return "TurnAdvanceButton";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.End_Turn, null);
                }
            }).setPosition(getButtonLocation(0, 0));
        }
        //Decide what to render based on what we clicked.
        if (target instanceof ResourceTile) {
            ResourceTile tile = (ResourceTile) target;
            title.setText("Tile: " + tile.getTerrain());
            if (tile.getResource() != null)
                detail.setText("Produces: " + tile.getResource());
            else
                detail.setText("Produces: Nothing");
            if (game.isLocalPlayerActive() && !tile.hasRobber()) {
                add(new ContextButton(2, 4) {
                    @Override
                    public String toString() {
                        return "Path";
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Tile_Rob, tile.getPosition());
                    }
                }).setPosition(getButtonLocation(1, 0));
            }
        } else if (target instanceof TradeTile) {
            TradeTile tile = (TradeTile) target;
            title.setText("Tile: " + tile.getTradingPostType() + " Port");
            detail.setText("Facing: " + tile.getDirection());
        } else if (target instanceof BeachTile) {
            BeachTile tile = (BeachTile) target;
            title.setText("Tile: Beach");
            detail.setText("Facing: " + tile.getDirection());
        } else if (target instanceof Path) {
            Path path = (Path) target;
            title.setText("Path: " + target);
            detail.setText("Owned by: " + path.getTeam());
            if (game.isLocalPlayerActive() && path.getTeam().equals(Team.None)) {
                add(new ContextButton(2, 5) {
                    @Override
                    public String toString() {
                        return "Path";
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Road_Purchase, path.getPosition());
                    }
                }).setPosition(getButtonLocation(1, 0));
            }
        } else if (target instanceof Town) {
            title.setText("Town: " + target);
            detail.setText("Owned by: " + ((Town) target).getTeam());
            if (game.isLocalPlayerActive()) {
                Town town = (Town) target;
                if (town.getTeam().equals(Team.None)) {
                    add(new ContextButton(2, 1) {
                        @Override
                        public String toString() {
                            return "Town";
                        }

                        @Override
                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Settlement_Purchase, town.getPosition());
                        }
                    }).setPosition(getButtonLocation(1, 0));
                } else if (town.getTeam().equals(game.getTeams().getLocalPlayer().getTeam())) {
                    add(new ContextButton(2, 3) {
                        @Override
                        public String toString() {
                            return "Town";
                        }

                        @Override
                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.City_Purchase, town.getPosition());
                        }
                    }).setPosition(getButtonLocation(1, 0));
                }
            }
        }
        add(background);
        center(add(title)).y = titleheight;
        center(add(detail)).y = detailheight;
    }

    private Point getButtonLocation(int x, int y) {
        return new Point(offset.x + x*spacing.x, offset.y + y*spacing.y);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "ContextScreenArea";
    }

    public void target(Object target) {
        this.target = target;
        forceRender();
    }

    private abstract class ContextButton extends GraphicObject {
        private ContextButton(int priority, int icon) {
            super(priority, graphics.getGraphic(icon));
        }
    }
}
