package catan.client.ui.game;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.client.graphics.ui.*;
import catan.client.input.UserEvent;
import catan.client.input.UserEventListener;
import catan.client.input.UserEventType;
import catan.client.structure.GameManager;
import catan.common.crypto.Username;
import catan.common.game.BoardObserver;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.paths.Path;
import catan.common.game.board.tiles.BeachTile;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.tiles.TradeTile;
import catan.common.game.board.towns.EmptyTown;
import catan.common.game.board.towns.Settlement;
import catan.common.game.board.towns.Town;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameEventType;
import catan.common.game.event.GameHistory;
import catan.common.game.gameplay.trade.Trade;
import catan.common.locale.LocalizedEventPrinter;
import catan.common.resources.GraphicSet;

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
    private final GameManager manager; //Used to control the local game and create game-changing events.
    private Coordinate targetCoord;
    private Trade targetTrade;
    private int targetHistory;
    private GameHistory targetHistoryEvent;
    private ContextTarget target;

    //Configuration dependencies
    private GraphicSet graphics;
    private LocalizedEventPrinter gameEventPrinter;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel title;
    private final TextLabel detail;
    private final List<ContextButton> buttons;
    private final BoardObserver board;

    public ContextRegion(GameManager manager) {
        super("ContextRegion", 2, "context");
        //Initialize instance information
        this.manager = manager;
        board = manager.getLocalGame().getBoardObserver();
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
        Tile targetTile = board.getTile(targetCoord);
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
        Path path = board.getPath(targetCoord);
        title.setText("Path: " + path);
        detail.setText("Owned by: " + path.getTeam());
        generateButton("PurchaseRoad", 2, GameEventType.Build_Road, targetCoord);
    }

    private void renderTown() {
        Town town = board.getTown(targetCoord);
        title.setText("Town: " + town);
        detail.setText("Owned by: " + town.getTeam());
        if (town instanceof EmptyTown)
            generateButton("PurchaseSettlement", 3, GameEventType.Build_Settlement, targetCoord);
        else if (town instanceof Settlement)
            generateButton("PurchaseCity", 4, GameEventType.Build_City, targetCoord);
        generateButton("StealResources", 5, GameEventType.Steal_Resources, targetCoord);
    }

    private void renderTrade() {
        generateButton("ConfirmTradeButton", 6, GameEventType.Make_Trade, targetTrade);
    }

    private void renderDevelopmentCard() {
        generateButton("BuyDevelopment", 7, GameEventType.Buy_Development, null);
        generateButton("UseRoadBuilding", 8, GameEventType.Play_RoadBuilding, null);
    }

    private void renderHistory() {
        Username origin = targetHistoryEvent.getGameEvent().getOrigin();
        title.setText("Event: " + gameEventPrinter.getLocalization(targetHistoryEvent.getGameEvent()));
        String detailText = "";
        if (origin != null)
            detailText = origin.toString();
        detail.setText(detailText);
        buttons.add(new ContextButton("HistoryJumpButton", 14, null) {
            @Override
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                listener.onUserEvent(new UserEvent(this, UserEventType.History_Jump, targetHistory));
            }
        });
    }

    private void renderJumpToLive() {
        if (!manager.isLive()) {
            buttons.add(new ContextButton("JumpToLive", 15, null) {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    listener.onUserEvent(new UserEvent(this, UserEventType.History_Jump, -1));
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

    public void targetHistory(int index, GameHistory event) {
        targetHistory = index;
        targetHistoryEvent = event;
        target = ContextTarget.History;
        forceRender();
    }

    @Override
    public void update() {
        forceRender();
    }

    private void generateButton(String name, int icon, GameEventType type, Object payload) {
        GameEvent event = new GameEvent(manager.getLocalUsername(), type, payload);
        if (manager.test(event))
            buttons.add(new ContextButton(name, icon, event));
    }

    private class ContextButton extends GraphicObject {

        private final GameEvent event;

        private ContextButton(String name, int icon, GameEvent event) {
            super(name, 2, graphics.getGraphic(icon));
            this.event = event;
        }

        @Override
        public void onMouseClick(UserEventListener listener, MouseEvent event) {
            listener.onUserEvent(new UserEvent(this, UserEventType.Game_Event, this.event));
        }
    }

    private enum ContextTarget {
        None, Tile, Path, Town, Trade, History
    }
}
