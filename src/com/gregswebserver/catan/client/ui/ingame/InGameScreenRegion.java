package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenContainer;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
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

    private final CatanGame game;
    private final boolean playing;

    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final ContextRegion context;
    private final TimelineRegion timeline;
    private int sidebarWidth;
    private int inventoryHeight;
    private int contextHeight;
    private int timelineHeight;
    private Dimension borderBuffer;

    public InGameScreenRegion(Client client) {
        super(client, "ingame");
        //Load relevant details
        GameManager manager = client.getGameManager();
        game = manager.getLocalGame();
        TeamColors teamColors = new TeamColors(client.getTeamColors());
        map = new ScrollingScreenContainer(0, "scroll", new MapRegion(game.getBoard(), teamColors)) {
            @Override
            public String toString() {
                return "MapScrollContainer";
            }
        };
        Username username = client.getToken().username;
        playing = game.getTeams().getAllUsers().contains(username);
        trade = new TradeRegion(game, username);
        inventory = new InventoryRegion(game, username);
        context = new ContextRegion(manager, username);
        timeline = new TimelineRegion(manager, teamColors);
        //Add everything to the screen
        add(map);
        if (playing) {
            add(trade);
            add(inventory);
        }
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
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        inventoryHeight = config.getLayout().getInt("inventory.height");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
        borderBuffer = config.getLayout().getDimension("borderbuffer");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth;
        if (playing)
            mainWidth = mask.getWidth() - sidebarWidth;
        else
            mainWidth = mask.getWidth();
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        if (playing) {
            inventory.setPosition(new Point(timelineWidth, 0));
            trade.setPosition(new Point(timelineWidth, inventoryHeight));
        }
        context.setPosition(new Point(timelineWidth, inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));
        if (playing) {
            trade.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
            inventory.setMask(new RectangularMask(new Dimension(sidebarWidth, inventoryHeight)));
        }
        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));

        Insets viewInsets = new Insets(borderBuffer.height, borderBuffer.width, borderBuffer.height, borderBuffer.width);
        map.setInsets(viewInsets);
        map.center();
    }

    public String toString() {
        return "InGameScreenRegion";
    }
}
