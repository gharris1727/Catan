package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenContainer;
import com.gregswebserver.catan.client.ui.ingame.map.MapRegion;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

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
    private static final Insets viewInsets;

    static {
        borderBuffer = Client.staticConfig.getDimension("catan.graphics.interface.ingame.borderbuffer");
        sidebarWidth = Client.staticConfig.getInt("catan.graphics.interface.ingame.sidebar.width");
        inventoryHeight = Client.staticConfig.getInt("catan.graphics.interface.ingame.inventory.height");
        contextHeight = Client.staticConfig.getInt("catan.graphics.interface.ingame.context.height");
        viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
    }

    private final CatanGame game;
    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;

    public InGameScreenRegion(Client client) {
        super(client);
        this.game = client.getActiveGame();
        map = new ScrollingScreenContainer(0, new MapRegion(game.getBoard()), viewInsets) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        trade = new TradeRegion(1, game);
        inventory = new InventoryRegion(2, game.getTeams().getLocalPlayer());
        context = new ContextRegion(3, game);
        add(map);
        add(trade);
        add(inventory);
        add(context);
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

    @Override
    public void update() {
        map.update();
        inventory.forceRender();
        trade.update();
        context.forceRender();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        inventory.setPosition(new Point(mainWidth, 0));
        trade.setPosition(new Point(mainWidth, inventoryHeight));
        context.setPosition(new Point(mainWidth,inventoryHeight + tradeHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mask.getHeight())));
        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
        inventory.setMask(new RectangularMask(new Dimension(sidebarWidth, inventoryHeight)));
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));

        map.center();
    }

    public String toString() {
        return "InGameScreenRegion";
    }
}
