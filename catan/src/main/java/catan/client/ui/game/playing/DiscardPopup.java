package catan.client.ui.game.playing;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.TiledBackground;
import catan.client.graphics.ui.UIConfig;
import catan.client.input.UserEventListener;
import catan.client.structure.GameManager;
import catan.client.ui.PopupWindow;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameEventType;
import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.GameResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 6/18/16.
 * Popup displayed to prompt the user to discard resources.
 */
public class DiscardPopup extends PopupWindow {

    //Instance information
    private final EnumAccumulator<GameResource> amount;

    //Config dependencies
    private Point elementOffset;
    private Point elementSpacing;

    //Sub-regions
    private final TiledBackground background;
    private final Map<GameResource, EnumCounterEditRegion<GameResource>> counters;
    private final catan.client.graphics.ui.Button accept;

    public DiscardPopup(GameManager manager, ConfigurableScreenRegion source) {
        super("DiscardPopup", "discard", source);
        amount = new EnumAccumulator<>(GameResource.class);
        background = new EdgedTiledBackground();
        counters = new EnumMap<>(GameResource.class);
        for(GameResource resource : amount) {
            EnumCounterEditRegion<GameResource> counter = new EnumCounterEditRegion<>("DiscardEditor", 1, "resource", amount, resource);
            counters.put(resource, counter);
            add(counter);
        }
        accept = new catan.client.graphics.ui.Button("AcceptButton", 2, "accept", "Accept") {
            @Override
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                EnumAccumulator<GameResource> copy = new EnumAccumulator<>(GameResource.class, amount);
                GameEvent gameEvent = new GameEvent(manager.getLocalUsername(), GameEventType.Discard_Resources, copy);
                manager.local(gameEvent);
            }
        };
        add(background).setClickable(this);
        add(accept);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        elementOffset = config.getLayout().getPoint("offset");
        elementSpacing = config.getLayout().getPoint("spacing");
        setMask(RenderMask.parseMask(config.getLayout().narrow("mask")));
    }

    @Override
    public void update() {
        for (EnumCounterEditRegion<GameResource> counter : counters.values())
            counter.forceRender();
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        int index = 0;
        for (GameResource resource : amount) {
            counters.get(resource).setPosition(new Point(
                elementOffset.x + index * elementSpacing.x,
                elementOffset.y));
            index++;
        }
        center(accept).y = elementOffset.y + elementSpacing.y;
    }
}