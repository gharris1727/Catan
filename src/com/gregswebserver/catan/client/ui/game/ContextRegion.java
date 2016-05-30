package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.locale.game.LocalizedGameEventPrinter;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends ConfigurableScreenRegion {

    //Instance information
    private Coordinate targetCoord;
    private Trade targetTrade;
    private int targetHistory;
    private ContextTarget target;

    //Optional modules
    private Username username; //Used when generating real game-changing events
    private GameManager manager; //Used to control the local game and create game-changing events.

    //Configuration dependencies
    private GraphicSet graphics;
    private LocalizedGameEventPrinter gameEventPrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel title;
    private final TextLabel detail;

    public ContextRegion() {
        super(2, "context");
        //Initialize instance information
        target = ContextTarget.None;
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        title = new TextLabel(1, "title", "");
        detail = new TextLabel(2, "detail", "");
        //Add everything to the screen.
        add(background).setClickable(this);
        add(title).setClickable(this);
        add(detail).setClickable(this);
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public void setGameManager(GameManager manager) {
        this.manager = manager;
    }

    private boolean isLocalPlayerActive() {
        return manager != null && manager.getLocalGame().isPlayerActive(username);
    }

    @Override
    public void loadConfig(UIConfig config) {
        graphics = new GraphicSet(config.getLayout(), "icons", null);
        gameEventPrinter = new LocalizedGameEventPrinter(config.getLocale());
    }

    @Override
    protected void renderContents() {
        //Clear the context region of everything
        clear();
        switch (target) {
            case None:
                //Clear the text to blank when not selecting anything.
                title.setText("");
                detail.setText("");
                break;
            case Tile:
                renderTile();
                break;
            case Path:
                renderPath();
                break;
            case Town:
                renderTown();
                break;
            case Trade:
                renderTrade();
                break;
            case History:
                renderHistory();
                break;
        }
        if (isLocalPlayerActive()) {
            add(new ContextButton(1) {
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

        add(background);

        center(add(title)).y = getConfig().getLayout().getInt("title.y");
        center(add(detail)).y = getConfig().getLayout().getInt("detail.y");
    }

    private void renderTile() {
        Tile targetTile = manager.getLocalGame().getBoard().getTile(targetCoord);
        if (targetTile instanceof ResourceTile) {
            ResourceTile tile = (ResourceTile) targetTile;
            title.setText("Tile: " + tile.getTerrain());
            if (tile.getResource() != null)
                detail.setText("Produces: " + tile.getResource());
            else
                detail.setText("Produces: Nothing");
            if (isLocalPlayerActive() && !tile.hasRobber()) {
                add(new ContextButton(2) {
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
        } else if (targetTile instanceof TradeTile) {
            TradeTile tile = (TradeTile) targetTile;
            title.setText("Tile: " + tile.getTradingPostType() + " Port");
            detail.setText("Facing: " + tile.getDirection());
        } else if (targetTile instanceof BeachTile) {
            BeachTile tile = (BeachTile) targetTile;
            title.setText("Tile: Beach");
            detail.setText("Facing: " + tile.getDirection());
        }
    }

    private void renderPath() {
        Path path = manager.getLocalGame().getBoard().getPath(targetCoord);
        title.setText("Path: " + path);
        detail.setText("Owned by: " + path.getTeam());
        if (isLocalPlayerActive() && path.getTeam().equals(TeamColor.None)) {
            add(new ContextButton(5) {
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
    }

    private void renderTown() {
        Town town = manager.getLocalGame().getBoard().getTown(targetCoord);
        title.setText("Town: " + town);
        detail.setText("Owned by: " + town.getTeam());
        if (isLocalPlayerActive()) {
            if (town.getTeam().equals(TeamColor.None)) {
                add(new ContextButton(3) {
                    @Override
                    public String toString() {
                        return "Town";
                    }
                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Settlement_Purchase, town.getPosition());
                    }
                }).setPosition(getButtonLocation(1, 0));
            } else if (town.getTeam().equals(manager.getLocalGame().getPlayers().getPlayer(username).getTeamColor())) {
                add(new ContextButton(4) {
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

    private void renderTrade() {
        if (isLocalPlayerActive() && manager.getLocalGame().getTrades(username).contains(targetTrade)) {
            add(new ContextButton(8) {
                @Override
                public String toString() {
                    return "ConfirmTrade";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Make_Trade, targetTrade);
                }
            }).setPosition(getButtonLocation(1, 0));
        }
    }

    private void renderHistory() {
        GameEvent event = manager.getEvents().get(targetHistory);
        title.setText("Event: " + gameEventPrinter.getLocalization(event));
        if (event.getOrigin() != null)
            detail.setText(event.getOrigin().toString());
        add(new ContextButton(9) {
            @Override
            public String toString() {
                return "JumpToEvent";
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.History_Jump, targetHistory);
            }
        }).setPosition(getButtonLocation(1,0));
    }

    private Point getButtonLocation(int x, int y) {
        Point offset = getConfig().getLayout().getPoint("offset");
        Point spacing = getConfig().getLayout().getPoint("spacing");
        return new Point(offset.x + x*spacing.x, offset.y + y*spacing.y);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "ContextScreenArea";
    }

    public void targetTile(Coordinate position) {
        targetCoord = position;
        target = ContextTarget.Tile;
        forceRender();
    }

    public void targetPath(Coordinate position) {
        targetCoord = position;
        target = ContextTarget.Path;
        forceRender();
    }

    public void targetTown(Coordinate position) {
        targetCoord = position;
        target = ContextTarget.Town;
        forceRender();
    }

    public void targetTrade(Trade trade) {
        targetTrade = trade;
        target = ContextTarget.Trade;
        forceRender();
    }

    public void targetHistory(int index) {
        targetHistory = index;
        target = ContextTarget.History;
        forceRender();
    }

    private abstract class ContextButton extends GraphicObject {
        private ContextButton(int icon) {
            super(2, graphics.getGraphic(icon));
        }
    }

    private enum ContextTarget {
        None, Tile, Path, Town, Trade, History
    }
}
