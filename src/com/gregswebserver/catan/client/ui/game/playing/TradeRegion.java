package com.gregswebserver.catan.client.ui.game.playing;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.TradeDisplay;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.gameplay.trade.TemporaryTrade;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.EnumCounter;
import com.gregswebserver.catan.common.game.util.GameResource;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends ConfigurableScreenRegion implements Updatable{

    //Required instance information
    private final CatanGame game;
    private final Username username;

    //Optional interaction modules
    private ContextRegion context;

    //Configuration dependencies
    private RenderMask panelSize;

    //Sub-regions
    private final TradeListContainer container;
    private final TradeControlPanel panel;

    public TradeRegion(CatanGame game, Username username) {
        super("TradeRegion", 1, "trade");
        //Save instance details
        this.game = game;
        this.username = username;
        //Create sub-regions
        container = new TradeListContainer(new TradeList());
        panel = new TradeControlPanel();
        //Add everything to the screen.
        add(container);
        add(panel);
    }

    public void setContext(ContextRegion context) {
        this.context = context;
    }

    @Override
    public void loadConfig(UIConfig config) {
        panelSize = RenderMask.parseMask(config.getLayout().narrow("panel.mask"));
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    public void update() {
        container.update();
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        int scrollHeight = getMask().getHeight() - panelSize.getHeight();
        RenderMask scrollSize = new RectangularMask(new Dimension(getMask().getWidth(), scrollHeight));
        container.setMask(scrollSize);
        panel.setMask(panelSize);
        panel.setPosition(new Point(0, scrollHeight));
        container.setInsets(new Insets(0,0,0,0));
    }

    private class TradeList extends ScrollingList {

        private TradeList() {
            super("TradeList", 1, "list");
        }

        @Override
        public void loadConfig(UIConfig config) {
            setElementSize(RenderMask.parseMask(config.getLayout().narrow("mask")));
        }

        @Override
        public void update() {
            forceRender();
        }

        @Override
        protected void renderContents() {
            clear();
            for (Trade t : game.getTrades(username))
                add(new Element(t));
            super.renderContents();
        }

        @Override
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            scroll(0, -4 * event.getUnitsToScroll());
            return null;
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            scroll(0, p.y);
            return null;
        }

        private class Element extends TradeDisplay {

            private final Trade t;

            private Element(Trade t) {
                super("TradeListElement", 0, "element", t);
                this.t = t;
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                if (context != null)
                    context.targetTrade(t);
                return null;
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return TradeList.this.onMouseScroll(event);
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return TradeList.this.onMouseDrag(p);
            }
        }
    }

    private class TradeListContainer extends ScrollingScreenContainer {

        private final TiledBackground background;

        private TradeListContainer(ScrollingScreenRegion scroll) {
            super("TradeListContainer", 0, scroll);
            background = new TiledBackground();
            add(background).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            super.resizeContents(mask);
            background.setMask(mask);
        }
    }

    private class TradeControlPanel extends ConfigurableScreenRegion {

        //Instance information
        private final EnumCounter<GameResource> diff;

        //Config dependencies
        private Point elementOffset;
        private Point elementSpacing;
        private int buttonSpacing;

        //Sub-regions
        private final TiledBackground background;
        private final Button propose;
        private final Button cancel;

        private TradeControlPanel() {
            super("TradeControlPanel", 2, "panel");
            diff = new EnumCounter<>(GameResource.class);
            background = new EdgedTiledBackground();
            propose = new Button("ProposeButton", 1, "propose", "Propose") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    TemporaryTrade trade = new TemporaryTrade(username);
                    for (GameResource resource : GameResource.values()) {
                        if (diff.get(resource) > 0)
                            trade.request.increment(resource, diff.get(resource));
                        else
                            trade.offer.increment(resource, -1*diff.get(resource));
                    }
                    return new UserEvent(this, UserEventType.Propose_Trade, trade);
                }
            };
            cancel = new Button("CancelButton", 2, "cancel", "Cancel") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Cancel_Trade, null);
                }
            };
            add(background).setClickable(this);
            add(propose);
            add(cancel);
        }

        @Override
        public void loadConfig(UIConfig config) {
            elementOffset = config.getLayout().getPoint("offset");
            elementSpacing = config.getLayout().getPoint("spacing");
            buttonSpacing = config.getLayout().getInt("buttons");
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            assertRenderable();
            clear();
            int index = 0;
            for (GameResource gameResource : GameResource.values()) {
                EditingResourceCounter request = new EditingResourceCounter(diff, gameResource);
                add(request).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
                request.setConfig(getConfig());
                index++;
            }
            add(background);
            add(propose);
            Point proposePosition = center(propose);
            proposePosition.setLocation(proposePosition.x - buttonSpacing,elementOffset.y + elementSpacing.y);
            add(cancel);
            Point cancelPosition = center(cancel);
            cancelPosition.setLocation(cancelPosition.x + buttonSpacing, elementOffset.y + elementSpacing.y);
        }

        private class EditingResourceCounter extends ConfigurableScreenRegion {

            private final TextLabel count;
            private final EnumCounter<GameResource> counter;
            private final GameResource gameResource;
            private final TiledBackground background;
            private final GraphicObject icon;
            private GraphicSet icons;

            private EditingResourceCounter(EnumCounter<GameResource> counter, GameResource gameResource) {
                super("EditingResourceCounter", 3, "resource");
                //Store instance information
                this.counter = counter;
                this.gameResource = gameResource;
                //Create sub-regions
                enableTransparency();
                background = new EdgedTiledBackground();
                icon = new GraphicObject("ResourceCounterImage", 1, null);
                count = new TextLabel("ResourceCounterCount", 2, "count", "0");
                //Add everything to the screen.
                add(background).setClickable(this);
                add(icon).setClickable(this);
                add(count).setClickable(this);
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                switch (event.getButton()) {
                    case MouseEvent.BUTTON1: //LEFT
                        counter.increment(gameResource, 1);
                        break;
                    case MouseEvent.BUTTON2: //MIDDLE
                        counter.clear(gameResource);
                        break;
                    case MouseEvent.BUTTON3: //RIGHT
                        counter.decrement(gameResource, 1);
                        break;
                }
                TradeControlPanel.this.forceRender();
                return null;
            }

            @Override
            public void loadConfig(UIConfig config) {
                icons = new GraphicSet(config.getLayout(), "icons", null);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
            }

            @Override
            protected void renderContents() {
                setMask(icons.getMask());
                assertRenderable();
                icon.setGraphic(icons.getGraphic(gameResource.ordinal()));
                int current=counter.get(gameResource);
                count.setText("" + current);
                count.setPosition(new Point(0, icons.getMask().getHeight() - count.getGraphic().getMask().getHeight()));
            }
        }
    }

}
