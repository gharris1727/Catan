package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.ClientScreen;
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

    private final CatanGame game;

    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;
    private final TimelineRegion timeline;

    public InGameScreenRegion(Client client) {
        super(client, "ingame");
        //Load relevant details
        GameManager manager = client.getGameManager();
        game = manager.getLocalGame();
        TeamColors teamColors = new TeamColors(client.getTeamColors());
        Dimension borderBuffer = getLayout().getDimension("borderbuffer");
        Insets viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
        map = new ScrollingScreenContainer(0, new MapRegion(getLayout(), game.getBoard(), teamColors), viewInsets) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        Username username = client.getToken().username;
        trade = new TradeRegion(this, game, username);
        inventory = new InventoryRegion(this, game.getTeams().getPlayer(username));
        context = new ContextRegion(this, manager, username);
        timeline = new TimelineRegion(this, manager, teamColors);
        //Add everything to the screen
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
        int sidebarWidth = getLayout().getInt("sidebar.width");
        int inventoryHeight = getLayout().getInt("inventory.height");
        int contextHeight = getLayout().getInt("context.height");
        int timelineHeight = getLayout().getInt("timeline.height");

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
