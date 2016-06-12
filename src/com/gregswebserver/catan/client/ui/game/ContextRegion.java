package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.event.GameHistory;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.locale.LocalizedEventPrinter;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends ConfigurableScreenRegion implements Updatable {

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
    private LocalizedEventPrinter gameEventPrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel title;
    private final TextLabel detail;
    private final List<ContextButton> buttons;

    public ContextRegion() {
        super("ContextRegion", 2, "context");
        //Initialize instance information
        target = ContextTarget.None;
        //Create sub-regions
        background = new EdgedTiledBackground();
        title = new TextLabel("Title", 1, "title", "");
        detail = new TextLabel("Detail", 2, "detail", "");
        buttons = new ArrayList<>();
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

    @Override
    public void loadConfig(UIConfig config) {
        graphics = new GraphicSet(config.getLayout(), "icons", null);
        gameEventPrinter = new LocalizedEventPrinter(config.getLocale().narrow("game.event"));
    }

    @Override
    protected void renderContents() {
        //Clear the context region of everything
        clear();
        buttons.clear();
        renderTurnAdvance();
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
        renderDevelopmentCard();
        renderJumpToLive();
        int index = 0;
        for (ContextButton button : buttons) {
            add(button);
            //TODO: make buttons fall to secon
            button.setPosition(getButtonLocation(index++, 0));
        }
        add(background);

        center(add(title)).y = getConfig().getLayout().getInt("title.y");
        center(add(detail)).y = getConfig().getLayout().getInt("detail.y");
    }

    private Point getButtonLocation(int x, int y) {
        Point offset = getConfig().getLayout().getPoint("offset");
        Point spacing = getConfig().getLayout().getPoint("spacing");
        return new Point(offset.x + x*spacing.x, offset.y + y*spacing.y);
    }

    private void renderTurnAdvance() {
        generateButton("TurnAdvance", 0, GameEventType.Turn_Advance, null);
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
            generateButton("RobTile", 1, GameEventType.Player_Move_Robber, targetCoord);
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
        generateButton("PurchaseRoad", 2, GameEventType.Build_Road, targetCoord);
    }

    private void renderTown() {
        Town town = manager.getLocalGame().getBoard().getTown(targetCoord);
        title.setText("Town: " + town);
        detail.setText("Owned by: " + town.getTeam());
        if (town instanceof EmptyTown)
            generateButton("PurchaseSettlement", 3, GameEventType.Build_Settlement, targetCoord);
        else if (town instanceof Settlement)
            generateButton("PurchaseCity", 4, GameEventType.Build_City, targetCoord);
    }

    private void renderTrade() {
        generateButton("ConfirmTradeButton", 5, GameEventType.Make_Trade, targetTrade);
    }

    private void renderDevelopmentCard() {
        generateButton("BuyDevelopment", 6, GameEventType.Buy_Development, null);
    }

    private void renderHistory() {
        GameHistory event = manager.getRemoteGame().getHistory().get(targetHistory);
        Username origin = event.getGameEvent().getOrigin();
        title.setText("Event: " + gameEventPrinter.getLocalization(event.getGameEvent()));
        String detailText = "";
        if (origin != null)
            detailText = origin.toString();
        detail.setText(detailText);
        buttons.add(new ContextButton("HistoryJumpButton", 14, null) {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.History_Jump, targetHistory);
            }
        });
    }

    private void renderJumpToLive() {
        if (!manager.isLive()) {
            buttons.add(new ContextButton("JumpToLive", 15, null) {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.History_Jump, -1);
                }
            });
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
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

    @Override
    public void update() {
        forceRender();
    }

    private void generateButton(String name, int icon, GameEventType type, Object payload) {
        if (username != null) {
            try {
                GameEvent event = new GameEvent(username, type, payload);
                manager.getLocalGame().test(event);
                buttons.add(new ContextButton(name, icon, event));
            } catch (EventConsumerException ignored) {
            }
        }
    }

    private class ContextButton extends GraphicObject {

        private final GameEvent event;

        private ContextButton(String name, int icon, GameEvent event) {
            super(name, 2, graphics.getGraphic(icon));
            this.event = event;
        }

        @Override
        public UserEvent onMouseClick(MouseEvent event) {
            manager.local(this.event);
            return null;
        }
    }

    private enum ContextTarget {
        None, Tile, Path, Town, Trade, History
    }
}
