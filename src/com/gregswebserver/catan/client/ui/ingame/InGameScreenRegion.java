package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenContainer;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class InGameScreenRegion extends ClientScreen {

    private static final Dimension borderBuffer;
    private static final int sidebarWidth;
    private static final int inventoryHeight;
    private static final int contextHeight;
    private static final int timelineHeight;
    private static final Insets viewInsets;

    static {
        borderBuffer = Client.graphicsConfig.getDimension("interface.ingame.borderbuffer");
        sidebarWidth = Client.graphicsConfig.getInt("interface.ingame.sidebar.width");
        inventoryHeight = Client.graphicsConfig.getInt("interface.ingame.inventory.height");
        contextHeight = Client.graphicsConfig.getInt("interface.ingame.context.height");
        timelineHeight = Client.graphicsConfig.getInt("interface.ingame.timeline.height");
        viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
    }

    private final GameManager manager;
    private final CatanGame game;
    private final TeamGraphics graphics;

    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;
    private final TimelineRegion timeline;

    public InGameScreenRegion(Client client) {
        super(client);
        this.manager = client.getGameManager();
        this.game = manager.getLocalGame();
        this.graphics = new TeamGraphics(client.getTeamColors());
        map = new ScrollingScreenContainer(0, new MapRegion(game.getBoard(), graphics), viewInsets) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        Username username = client.getToken().username;
        trade = new TradeRegion(1, game, username);
        inventory = new InventoryRegion(2, game.getTeams().getPlayer(username));
        context = new ContextRegion(2, manager, username);
        timeline = new TimelineRegion(2, manager, graphics);
        add(map);
        add(trade);
        add(inventory);
        add(context);
        add(timeline);
    }

    public void spaceClicked(Coordinate coord) {
        context.target(game.getBoard().getTile(coord));
    }

    public void edgeClicked(Coordinate coord) {
        context.target(game.getBoard().getPath(coord));
    }

    public void vertexClicked(Coordinate coord) {
        context.target(game.getBoard().getTown(coord));
    }

    public void tradeClicked(Trade trade) {
        context.target(trade);
    }

    public void timelineClicked(Integer index) {
        context.target(index);
    }

    @Override
    public void update() {
        map.update();
        timeline.update();
        inventory.forceRender();
        trade.update();
        context.forceRender();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        inventory.setPosition(new Point(mainWidth, 0));
        trade.setPosition(new Point(mainWidth, inventoryHeight));
        context.setPosition(new Point(mainWidth,inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
        inventory.setMask(new RectangularMask(new Dimension(sidebarWidth, inventoryHeight)));
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(mainWidth, timelineHeight)));

        map.center();
    }

    public String toString() {
        return "InGameScreenRegion";
    }
}
