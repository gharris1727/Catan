package com.gregswebserver.catan.client.ui.game.playing;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEventListener;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.TradeDisplay;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.EnumAccumulator;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends ConfigurableScreenRegion implements Updatable{

    //Required instance information
    private final GameManager manager;

    //Optional interaction modules
    private ContextRegion context;

    //Configuration dependencies
    private RenderMask panelSize;

    //Sub-regions
    private final TradeListContainer container;
    private final TradeControlPanel panel;

    public TradeRegion(GameManager manager) {
        super("TradeRegion", 1, "trade");
        //Save instance details
        this.manager = manager;
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
            for (Trade t : manager.getLocalGame().getTrades(manager.getLocalUsername()))
                add(new Element(t));
            super.renderContents();
        }

        @Override
        public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
            scroll(0, -4 * event.getUnitsToScroll());
        }

        @Override
        public void onMouseDrag(UserEventListener listener, Point p) {
            scroll(0, p.y);
        }

        private class Element extends TradeDisplay {

            private final Trade t;

            private Element(Trade t) {
                super("TradeListElement", 0, "element", t);
                this.t = t;
            }

            @Override
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                if (context != null)
                    context.targetTrade(t);
            }

            @Override
            public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
                TradeList.this.onMouseScroll(listener, event);
            }

            @Override
            public void onMouseDrag(UserEventListener listener, Point p) {
                TradeList.this.onMouseDrag(listener, p);
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
        private final EnumAccumulator<GameResource> diff;

        //Config dependencies
        private Point elementOffset;
        private Point elementSpacing;
        private int buttonSpacing;

        //Sub-regions
        private final TiledBackground background;
        private final Map<GameResource, EnumCounterEditRegion<GameResource>> counters;
        private final Button propose;
        private final Button cancel;

        private TradeControlPanel() {
            super("TradeControlPanel", 2, "panel");
            diff = new EnumAccumulator<>(GameResource.class);
            counters = new EnumMap<>(GameResource.class);
            background = new EdgedTiledBackground();
            propose = new Button("ProposeButton", 1, "propose", "Propose") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    EnumAccumulator<GameResource> request = new EnumAccumulator<>(GameResource.class);
                    EnumAccumulator<GameResource> offer = new EnumAccumulator<>(GameResource.class);
                    for (GameResource resource : GameResource.values()) {
                        if (diff.get(resource) > 0)
                            request.increment(resource, diff.get(resource));
                        else
                            offer.increment(resource, -1*diff.get(resource));
                    }
                    Trade trade = new Trade(manager.getLocalUsername(), offer, request);
                    manager.local(new GameEvent(manager.getLocalUsername(), GameEventType.Offer_Trade, trade));
                }
            };
            cancel = new Button("CancelButton", 2, "cancel", "Cancel") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    manager.local(new GameEvent(manager.getLocalUsername(), GameEventType.Cancel_Trade, null));
                }
            };
            for (GameResource resource : diff) {
                EnumCounterEditRegion<GameResource> request = new EnumCounterEditRegion<>("TradeEditor", 3, "resource", diff, resource);
                counters.put(resource, request);
                add(request);
            }
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
            int index = 0;
            for (GameResource resource : diff) {
                counters.get(resource).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
                index++;
            }
            Point proposePosition = center(propose);
            proposePosition.setLocation(proposePosition.x - buttonSpacing,elementOffset.y + elementSpacing.y);
            Point cancelPosition = center(cancel);
            cancelPosition.setLocation(cancelPosition.x + buttonSpacing, elementOffset.y + elementSpacing.y);
        }

    }

}
