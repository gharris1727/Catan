package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.util.Screen;
import com.gregswebserver.catan.client.renderer.connect.ConnectScreenRegion;
import com.gregswebserver.catan.client.renderer.ingame.InGameScreenRegion;
import com.gregswebserver.catan.client.state.ClientState;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private Client client;
    private RenderMask screenMask = new RectangularMask(new Dimension(640, 480));
    private Screen screen;
    private HashMap<ClientState, ScreenRegion> areas;
    private ScreenRegion root;
    private boolean enabled;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        areas = new HashMap<>();
        root = null;
        enabled = false;
    }

    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(true);
        switch (event.getType()) {
            case ConnectionList_Create:
                ConnectScreenRegion connect = new ConnectScreenRegion(screenMask, (ServerList) event.getPayload());
                areas.put(ClientState.Disconnected, connect);
                break;
            case Game_Create:
                InGameScreenRegion gameCreate = new InGameScreenRegion(screenMask, (CatanGame) event.getPayload());
                areas.put(ClientState.InGame, gameCreate);
                break;
            case Game_Update:
                InGameScreenRegion gameUpdate = (InGameScreenRegion) areas.get(ClientState.InGame);
                gameUpdate.update();
                break;
            case Window_Resize:
                enabled = false;
                Dimension size = (Dimension) event.getPayload();
                screen.resize(size);
                screenMask = new RectangularMask(size);
                for (ScreenRegion area : areas.values())
                    area.setMask(screenMask);
                client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
                break;
            case Render_Disable:
                break;
            case Render_Enable:
                enabled = true;
                break;
            case Animation_Step:
                if (root != null && root.isAnimated())
                    root.step();
                break;
        }
        if (enabled) {
            ScreenRegion next = areas.get(client.getState());
            screen.clear();
            if (root != next)
                client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, next));
            root = next;
            if (root != null)
                root.getGraphic().renderTo(screen, new Point(), 0);
            screen.show();
        }
    }

    public String toString() {
        return "RenderThread";
    }

}
