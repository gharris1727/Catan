package catan.client.ui.game.playing;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.ScrollingScreenContainer;
import catan.client.graphics.ui.UIConfig;
import catan.client.graphics.ui.Updatable;
import catan.client.structure.GameManager;
import catan.client.ui.ClientScreen;
import catan.client.ui.game.ContextRegion;
import catan.client.ui.game.MapRegion;
import catan.client.ui.game.TimelineRegion;

import java.awt.*;

/**
 * Created by Greg on 1/3/2015.
 * The area that renders all features visible while the client is in a game.
 */
public class PlayingScreenRegion extends ClientScreen implements Updatable {

    private final GameManager manager;
    //Configuration dependencies
    private int sidebarWidth;
    private int inventoryHeight;
    private int contextHeight;
    private int timelineHeight;

    //Sub-regions
    private final ContextRegion context;
    private final ScrollingScreenContainer map;
    private final TradeRegion trade;
    private final InventoryRegion inventory;
    private final TimelineRegion timeline;
    private DiscardPopup discardPopup;

    public PlayingScreenRegion(GameManager manager) {
        super("PlayingScreen", "playing");
        this.manager = manager;
        //Create sub-regions
        context = new ContextRegion(manager);
        MapRegion mapRegion = new MapRegion(manager);
        map = new ScrollingScreenContainer("MapScrollContainer", 0, mapRegion);
        trade = new TradeRegion(manager);
        inventory = new InventoryRegion(manager);
        timeline = new TimelineRegion(manager);
        //Link the context region into everything else
        mapRegion.setContext(context);
        trade.setContext(context);
        timeline.setContext(context);
        //Add everything to the screen
        add(map);
        add(trade);
        add(inventory);
        add(context);
        add(timeline);
    }

    @Override
    public void update() {
        synchronized (manager) {
            map.update();
            timeline.update();
            inventory.update();
            trade.update();
            context.update();
            if (manager.getLocalGame().mustDiscard(manager.getLocalUsername())) {
                if (discardPopup != null) {
                    discardPopup = new DiscardPopup(manager, this);
                    discardPopup.display();
                }
            } else if (discardPopup != null) {
                discardPopup.expire();
            }
        }
    }

    @Override
    public void loadConfig(UIConfig config) {
        sidebarWidth = config.getLayout().getInt("sidebar.width");
        inventoryHeight = config.getLayout().getInt("inventory.height");
        contextHeight = config.getLayout().getInt("context.height");
        timelineHeight = config.getLayout().getInt("timeline.height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int mainWidth = mask.getWidth() - sidebarWidth;
        int timelineWidth = mask.getWidth() - sidebarWidth;
        int mainHeight = mask.getHeight() - timelineHeight;
        int tradeHeight = mask.getHeight() - inventoryHeight - contextHeight;

        inventory.setPosition(new Point(timelineWidth, 0));
        trade.setPosition(new Point(timelineWidth, inventoryHeight));
        context.setPosition(new Point(timelineWidth, inventoryHeight + tradeHeight));
        timeline.setPosition(new Point(0,mainHeight));

        map.setMask(new RectangularMask(new Dimension(mainWidth, mainHeight)));

        trade.setMask(new RectangularMask(new Dimension(sidebarWidth, tradeHeight)));
        inventory.setMask(new RectangularMask(new Dimension(sidebarWidth, inventoryHeight)));

        context.setMask(new RectangularMask(new Dimension(sidebarWidth, contextHeight)));
        timeline.setMask(new RectangularMask(new Dimension(timelineWidth, timelineHeight)));
        map.center();
    }

    @Override
    public Graphic getGraphic() {
        synchronized (manager) {
            return super.getGraphic();
        }
    }
}
