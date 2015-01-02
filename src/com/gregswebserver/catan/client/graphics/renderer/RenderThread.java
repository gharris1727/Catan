package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.ClientEvent;
import com.gregswebserver.catan.client.event.ClientEventType;
import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.graphics.areas.RootArea;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;

import java.awt.*;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the hitbox information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private Client client;
    private Screen screen;
    private RootArea rootArea;
    private boolean enabled = false;

    public RenderThread(Client client) {
        super(client.logger);
        this.client = client;
        screen = new Screen();
        rootArea = new RootArea(logger);
        client.addEvent(new ClientEvent(this, ClientEventType.Hitbox_Update, rootArea));
    }

    protected void execute() throws ThreadStop {
        //Process the event queue without blocking, allowing the renderer to run more than once per event.
        RenderEvent event = getEvent(false);
        if (event != null) {
            switch (event.getType()) {
                case Game_Create:
                    rootArea.setActiveGame((CatanGame) event.getPayload());
                    break;
                case Game_Update:
                    rootArea.updateMap();
                    break;
                case Window_Resize:
                    enabled = false;
                    Dimension size = (Dimension) event.getPayload();
                    screen.resize(size);
                    rootArea.resize(size);
                    client.addEvent(new ClientEvent(this, ClientEventType.Canvas_Update, screen.getCanvas()));
                    break;
                case Render_Disable:
                    enabled = false;
                    break;
                case Render_Enable:
                    enabled = true;
                    break;
            }
        }
        //TODO: add a better frame limiter/profiling/fps tool.
        if (event == null && enabled) {
            screen.clear();
            rootArea.getGraphic().renderTo(screen, null, new Point(), 0);
            screen.show();
        }
    }

    public String toString() {
        return client + "RenderThread";
    }

}
